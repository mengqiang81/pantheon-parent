package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.rpc.api.NotFoundInterfaceException;
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
class AnnotationProviderDiscoverer3Test {

    @Autowired
    private AnnotationRpcProviderDiscoverer discoverer;

    @Test
    void get() {
        Assertions.assertThrows(NotFoundInterfaceException.class, () -> {
            discoverer.findProviders();
        });
    }

    public interface DemoService {
        @Method("say")
        String sayHello(@Param("firstName") String firstName, String lastName);
    }

    @Configuration
    static class Config {

        @Bean
        public AnnotationRpcProviderDiscoverer annotationRpcProviderDiscoverer() {
            return new AnnotationRpcProviderDiscoverer();
        }

        @Bean
        public DemoService demoService() {
            return new DemoServiceImpl();
        }

    }

    @Rpc("demo")
    public static class DemoServiceImpl implements DemoService {

        @Override
        public String sayHello(String firstName, String lastName) {
            return firstName + lastName;
        }

        public void test() {

        }
    }
}