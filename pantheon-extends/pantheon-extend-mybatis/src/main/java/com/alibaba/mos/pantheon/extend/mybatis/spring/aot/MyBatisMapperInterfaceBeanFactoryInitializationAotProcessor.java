package com.alibaba.mos.pantheon.extend.mybatis.spring.aot;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyBatisMapperInterfaceBeanFactoryInitializationAotProcessor implements BeanFactoryInitializationAotProcessor {

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanNamesForType(MapperFactoryBean.class);
        if (beanNames.length == 0) {
            return null;
        }

        return (context, code) -> {
            RuntimeHints hints = context.getRuntimeHints();

            for (String beanName : beanNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName.substring(1));
                Object mapperInterface = beanDefinition.getPropertyValues().get("mapperInterface");
                if (Objects.isNull(mapperInterface)) {
                    return;
                }

                Class<?> type = (Class<?>) mapperInterface;
                hints.proxies().registerJdkProxy(type);

                registerReflectionTypeIfNecessary(type, hints);
                registerMapperRelationships(type, hints);
            }
        };
    }

    private void registerReflectionTypeIfNecessary(Class<?> type, RuntimeHints hints) {
        if (type.isPrimitive() || type.getName().startsWith("java")) {
            return;
        }
        hints.reflection().registerType(type, MemberCategory.values());
    }

    private void registerMapperRelationships(Class<?> type, RuntimeHints hints) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(type);
        for (Method method : methods) {
            if (method.getDeclaringClass() == Objects.class) {
                continue;
            }

            registerSqlProviderTypes(method, hints, SelectProvider.class, SelectProvider::value, SelectProvider::type);
            registerSqlProviderTypes(method, hints, InsertProvider.class, InsertProvider::value, InsertProvider::type);
            registerSqlProviderTypes(method, hints, UpdateProvider.class, UpdateProvider::value, UpdateProvider::type);
            registerSqlProviderTypes(method, hints, DeleteProvider.class, DeleteProvider::value, DeleteProvider::type);

            registerReflectionTypeIfNecessary(resolveReturnClass(type, method), hints);
            resolveParameterClasses(type, method)
                    .forEach(x -> registerReflectionTypeIfNecessary(x, hints));
        }
    }

    private Set<Class<?>> resolveParameterClasses(Class<?> type, Method method) {
        return Stream.of(TypeParameterResolver.resolveParamTypes(method, type))
                .map(x -> typeToClass(x, x instanceof Class ? (Class<?>) x : Object.class))
                .collect(Collectors.toSet());
    }

    private Class<?> resolveReturnClass(Class<?> type, Method method) {
        Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, type);
        return typeToClass(resolvedReturnType, method.getReturnType());
    }

    private static Class<?> typeToClass(Type src, Class<?> fallback) {
        Class<?> result = null;
        if (src instanceof Class<?> aClass) {
            if (aClass.isArray()) {
                result = ((Class<?>) src).getComponentType();
            } else {
                result = (Class<?>) src;
            }
        } else if (src instanceof ParameterizedType parameterizedType) {
            int index = (parameterizedType.getRawType() instanceof Class
                    && Map.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())
                    && parameterizedType.getActualTypeArguments().length > 1) ? 1 : 0;
            Type actualType = parameterizedType.getActualTypeArguments()[index];
            result = typeToClass(actualType, fallback);
        }

        return Objects.isNull(result) ? fallback : result;
    }

    @SafeVarargs
    private <T extends Annotation> void registerSqlProviderTypes(Method method,
                                                                 RuntimeHints hints,
                                                                 Class<T> annotationType,
                                                                 Function<T, Class<?>>... providerTypeResolvers) {
        for (T annotation : method.getAnnotationsByType(annotationType)) {
            for (Function<T, Class<?>> providerTypeResolver : providerTypeResolvers) {
                registerReflectionTypeIfNecessary(providerTypeResolver.apply(annotation), hints);
            }
        }
    }
}
