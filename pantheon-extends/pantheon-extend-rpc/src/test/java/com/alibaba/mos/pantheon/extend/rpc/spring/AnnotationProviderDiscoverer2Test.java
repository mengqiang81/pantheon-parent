package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.extend.rpc.exception.InvalidParamNameException;
import com.alibaba.mos.pantheon.rpc.api.Method;
import com.alibaba.mos.pantheon.rpc.api.Param;
import com.alibaba.mos.pantheon.rpc.api.Rpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig
class AnnotationProviderDiscoverer2Test {

    @Autowired
    private AnnotationProviderDiscoverer discoverer;

    @Test
    void get() {
        Assertions.assertThrows(InvalidParamNameException.class, () -> discoverer.findProviders());
    }

    @Rpc("demo")
    public interface DemoService {
        @Method("say")
        String sayHello(@Param("firstName") String firstName, String lastName);
    }

    @Configuration
    static class Config {

        @Bean
        public AnnotationProviderDiscoverer annotationRpcProviderDiscoverer() {
            return new AnnotationProviderDiscoverer();
        }

        @Bean
        public DemoService demoService() {
            return new DemoServiceImpl();
        }

    }

    public static class DemoServiceImpl implements DemoService {

        @Override
        public String sayHello(String firstName, String lastName) {
            return firstName + lastName;
        }

        public void test() {

        }
    }
}