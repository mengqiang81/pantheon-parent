package com.alibaba.mos.pantheon.extend.mybatis.spring.aot;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import java.util.Objects;


public class MybatisMapperFactoryBeanPostProcessor implements MergedBeanDefinitionPostProcessor, BeanFactoryAware {

    private static final String MAPPER_FACTORY_BEAN = "org.mybatis.spring.mapper.MapperFactoryBean";

    private ConfigurableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(@SuppressWarnings("NullableProblems") BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableBeanFactory configurableBeanFactory) {
            this.beanFactory = configurableBeanFactory;
        } else {
            throw new IllegalStateException("Cannot do auto-TargetSource creation with a BeanFactory that doesn't implement ConfigurableBeanFactory: " + beanFactory.getClass());
        }
    }

    @Override
    public void postProcessMergedBeanDefinition(@SuppressWarnings("NullableProblems") RootBeanDefinition beanDefinition,
                                                @SuppressWarnings("NullableProblems") Class<?> beanType,
                                                @SuppressWarnings("NullableProblems") String beanName) {
        if (!ClassUtils.isPresent(MAPPER_FACTORY_BEAN, this.beanFactory.getBeanClassLoader())) {
            return;
        }

        if (!beanDefinition.hasBeanClass() || !MapperFactoryBean.class.isAssignableFrom(beanDefinition.getBeanClass())) {
            return;
        }

        if (!beanDefinition.getResolvableType().hasUnresolvableGenerics()) {
            return;
        }

        Object mapperInterface = beanDefinition.getPropertyValues().get("mapperInterface");
        if (Objects.isNull(mapperInterface)) {
            return;
        }

        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(mapperInterface);
        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
        beanDefinition.setTargetType(ResolvableType.forClassWithGenerics(beanDefinition.getBeanClass(), (Class<?>) mapperInterface));
    }
}
