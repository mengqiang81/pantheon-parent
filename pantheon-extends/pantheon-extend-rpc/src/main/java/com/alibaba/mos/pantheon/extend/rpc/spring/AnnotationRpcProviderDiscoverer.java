package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.rpc.api.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Parameter;
import java.util.*;

public class AnnotationRpcProviderDiscoverer implements ApplicationContextAware, RpcProviderDiscoverer {

    private ApplicationContext applicationContext;

    @Override
    public Collection<RpcProviderDefinition> findProviders() {
        String[] beanNames = this.applicationContext.getBeanNamesForAnnotation(Rpc.class);
        List<RpcProviderDefinition> definitions = new ArrayList<>();
        for (String beanName : beanNames) {
            Rpc serviceAnnotation = applicationContext.findAnnotationOnBean(beanName, Rpc.class);
            Object beanInstant = this.applicationContext.getBean(beanName);
            List<Class<?>> interfaces = Arrays.stream(beanInstant.getClass().getInterfaces())
                    .filter(o -> Objects.nonNull(o.getAnnotation(Rpc.class)))
                    .toList();
            if (interfaces.isEmpty()) {
                throw new NotFoundInterfaceException("%s not found RpcProvider interface".formatted(beanName));
            }

            if (interfaces.size() != 1) {
                throw new UnSupportMultipleInterfacesException("%s found Multiple RpcProvider interface".formatted(beanName));
            }

            Class<?> interType = interfaces.getFirst();
            String serviceName = interType.getName();
            if (serviceAnnotation != null && Objects.nonNull(serviceAnnotation.value()) && !serviceAnnotation.value().isBlank()) {
                serviceName = serviceAnnotation.value();
            }
            java.lang.reflect.Method[] methods = ReflectionUtils.getDeclaredMethods(interType);
            for (java.lang.reflect.Method method : methods) {
                String methodName = method.getName();
                Method methodAnnotation = method.getAnnotation(Method.class);
                if (Objects.nonNull(methodAnnotation) && Objects.nonNull(methodAnnotation.value()) && !methodAnnotation.value().isBlank()) {
                    methodName = methodAnnotation.value();
                }
                RpcProviderDefinition definition = new RpcProviderDefinition();
                definition.setServiceName(serviceName.toLowerCase());
                definition.setServiceInstance(beanInstant);
                definition.setMethodName(methodName.toLowerCase());
                definition.setMethodInstance(method);
                definition.setParamTypes(extractParams(interType, method));
                definition.setReturnType(method.getReturnType());
                definitions.add(definition);
            }
        }
        return definitions;
    }

    private Map<String, Class<?>> extractParams(Class<?> interType, java.lang.reflect.Method method) {
        Map<String, Class<?>> paramTypes = new HashMap<>();
        if (method.getParameters().length > 1) {
            boolean isAllAnnotation = Arrays.stream(method.getParameters())
                    .allMatch(o ->
                            Objects.nonNull(o.getAnnotation(Param.class))
                            && Objects.nonNull(o.getAnnotation(Param.class).value())
                            && !o.getAnnotation(Param.class).value().isBlank());
            if (!isAllAnnotation) {
                throw new InvalidParamNameException("%s,%s should user @RpcParam annotation for method all parameters".formatted(interType.getName(), method.getName()));
            }
        }

        for (Parameter parameter : method.getParameters()) {
            String paramName = parameter.getName();
            Param paramAnnotation = parameter.getAnnotation(Param.class);
            if (Objects.nonNull(paramAnnotation) && Objects.nonNull(paramAnnotation.value()) && !paramAnnotation.value().isBlank()) {
                paramName = paramAnnotation.value();
            }
            if (paramTypes.containsKey(paramName)) {
                throw new DuplicateParamNameException("%s has duplicate param name [%s]".formatted(interType.getName(), paramName));
            } else {
                paramTypes.put(paramName, parameter.getType());
            }
        }
        return paramTypes;
    }

    @Override
    public void setApplicationContext(@SuppressWarnings("NullableProblems") ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
