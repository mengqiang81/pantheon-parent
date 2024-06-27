package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.rpc.api.Method;
import com.alibaba.mos.pantheon.rpc.api.Param;
import com.alibaba.mos.pantheon.rpc.api.Rpc;
import com.alibaba.mos.pantheon.rpc.api.UnSupportMultipleInterfacesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


@SpringJUnitConfig
class AnnotationProviderDiscoverer4Test {

    @Autowired
    private AnnotationRpcProviderDiscoverer discoverer;

    @Test
    void get() {
        Assertions.assertThrows(UnSupportMultipleInterfacesException.class, () -> {
            discoverer.findProviders();
        });
    }

    @Rpc("demo")
    public interface DemoService {
        @Method("say")
        String sayHello(@Param("firstName") String firstName, String lastName);
    }


    @Rpc("demo2")
    public interface Demo2Service {
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

    public static class DemoServiceImpl implements DemoService, Demo2Service {

        @Override
        public String sayHello(String firstName, String lastName) {
            return firstName + lastName;
        }

        public void test() {

        }
    }
}