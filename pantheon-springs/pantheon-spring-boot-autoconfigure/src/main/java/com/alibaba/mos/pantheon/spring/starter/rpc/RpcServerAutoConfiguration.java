package com.alibaba.mos.pantheon.spring.starter.rpc;

import com.alibaba.mos.pantheon.extend.rpc.provider.DefaultServerInvoker;
import com.alibaba.mos.pantheon.extend.rpc.provider.ServerInvoker;
import com.alibaba.mos.pantheon.extend.rpc.spring.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RpcServerServlet.class})
public class RpcServerAutoConfiguration {


    @ConditionalOnMissingBean
    @Bean
    public ProviderDiscoverer defaultGrpcServiceDiscoverer() {
        return new AnnotationProviderDiscoverer();
    }

    @ConditionalOnMissingBean
    @Bean
    public ServerInvoker serverInvoker(List<ProviderDiscoverer> discoverers) {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new StdDateFormat())
                .setLocale(Locale.CHINA)
                .setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultServerInvoker invoker = new DefaultServerInvoker(mapper);
        for (ProviderDiscoverer discoverer : discoverers) {
            invoker.addAll(discoverer.findProviders());
        }
        return invoker;
    }

    @ConditionalOnMissingBean
    @Bean
    public RpcServerServlet rpcServerServlet(ServerInvoker invoker) {
        return new RpcServerServlet(invoker);
    }

    @ConditionalOnMissingBean
    @Bean
    public ServletRegistrationBean<RpcServerServlet> rpcServerServletServletRegistrationBean(RpcServerServlet rpcServerServlet) {
        ServletRegistrationBean<RpcServerServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.setName("api");
        registrationBean.addUrlMappings("/api/*");
        registrationBean.setServlet(rpcServerServlet);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }
}
