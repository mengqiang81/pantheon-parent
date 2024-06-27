package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.extend.rpc.Resp;
import com.alibaba.mos.pantheon.extend.rpc.exception.DuplicateParamNameException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultServerInvoker implements ServerInvoker {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new StdDateFormat())
            .setLocale(Locale.CHINA)
            .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final Map<String, ProviderDefinition> definitionMap = new ConcurrentHashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void invoke(String service, String method, Map<String, String> metas, InputStream input, OutputStream output) throws IOException {
        Resp resp = new Resp();
        resp.setCode("ok");
        if (Objects.isNull(service) || service.isBlank()) {
            resp.setCode("service-missing");
            resp.setMessage("服务不能为空");
            MAPPER.writeValue(output, resp);
            return;
        }
        if (Objects.isNull(method) || method.isBlank()) {
            resp.setCode("method-missing");
            resp.setMessage("方法不能为空");
            MAPPER.writeValue(output, resp);
            return;
        }

        String key = getFormatted(service, method);
        ProviderDefinition provider = definitionMap.get(key);
        if (Objects.isNull(provider)) {
            resp.setCode("method-off");
            resp.setMessage("API不存在或API已下线");
            MAPPER.writeValue(output, resp);
            return;
        }
        List<Object> parmas = new ArrayList<>();
        if (provider.getParamTypes().size() == 1) {
            for (Map.Entry<String, Class<?>> entry : provider.getParamTypes().entrySet()) {
                parmas.add(MAPPER.readValue(input, entry.getValue()));
            }
        } else {
            JsonNode nodes = MAPPER.readTree(input);
            for (Map.Entry<String, Class<?>> entry : provider.getParamTypes().entrySet()) {
                JsonNode node = nodes.get(entry.getKey());
                if (Objects.isNull(node) || node.isNull()) {
                    parmas.add(null);
                } else {
                    parmas.add(MAPPER.convertValue(node, entry.getValue()));
                }
            }
        }
        Object ret = null;
        try {
            if (parmas.isEmpty()) {
                ret = provider.getMethodInstance().invoke(provider.getServiceInstance());
            } else {
                ret = provider.getMethodInstance().invoke(provider.getServiceInstance(), parmas.toArray());
            }
        } catch (Exception e) {
            log.error("method:{} invoke error", key);
            resp.setCode("service-error");
            resp.setMessage("服务调用失败，请稍后重试");
            MAPPER.writeValue(output, resp);
        }
        if (void.class.equals(provider.getReturnType())) {
            MAPPER.writeValue(output, resp);
        } else {
            resp.setData(ret);
            MAPPER.writeValue(output, resp);
        }
    }


    public void addAll(Collection<ProviderDefinition> providers) {
        for (ProviderDefinition provider : providers) {
            String key = getFormatted(provider.getServiceName(), provider.getMethodName());
            if (definitionMap.containsKey(key)) {
                throw new DuplicateParamNameException(String.format("%s is duplicated", provider.getServiceName() + provider.getMethodName()));
            }
            definitionMap.put(key, provider);
        }
    }

    private static String getFormatted(String serviceName, String methodName) {
        return "%s.%s".formatted(serviceName.toLowerCase(), methodName.toLowerCase());
    }
}
