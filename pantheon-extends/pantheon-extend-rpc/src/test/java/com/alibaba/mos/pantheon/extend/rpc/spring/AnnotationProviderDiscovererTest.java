package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.rpc.api.Method;
import com.alibaba.mos.pantheon.rpc.api.Param;
import com.alibaba.mos.pantheon.rpc.api.Rpc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringJUnitConfig
class AnnotationProviderDiscovererTest {

    @Autowired
    private AnnotationRpcProviderDiscoverer discoverer;

    @Test
    void get() {
        Collection<RpcProviderDefinition> defs = discoverer.findProviders();
        Assertions.assertEquals(defs.size(), 6);

        Map<String, RpcProviderDefinition> groups = defs.stream()
                .collect(Collectors.toMap(o -> o.getServiceName() + "." + o.getMethodName(), o -> o));
        Assertions.assertNotNull(groups.get("demo.say"));
        Assertions.assertNotNull(groups.get("demo.saywithname"));
        Assertions.assertNotNull(groups.get("demo.saywithfirstnameandlastname"));
        Assertions.assertNotNull(groups.get("demo.returnvoidmethod"));
        Assertions.assertNotNull(groups.get("demo.bothvoidmethod"));
        Assertions.assertNotNull(groups.get("com.alibaba.mos.pantheon.extend.rpc.spring.annotationproviderdiscoverertest$demo2service.method"));

        RpcProviderDefinition def = groups.get("demo.say");
        assertTrue(def.getParamTypes().isEmpty());
        assertEquals(String.class, def.getReturnType());

        def = groups.get("demo.saywithname");
        assertFalse(def.getParamTypes().isEmpty());
        assertEquals(String.class, def.getReturnType());
    }

    @Rpc("demo")
    public interface Demo1Service {

        @Method("say")
        String sayHello();

        @Method("sayWithName")
        String sayHello(String name);

        @Method("sayWithFirstNameAndLastName")
        String sayHello(@Param("firstName") String firstName, @Param("lastName") String lastName);

        void returnVoidMethod(String name);

        void bothVoidMethod();
    }


    @Rpc
    public interface Demo2Service {
        Point method(@Param("name") String name, @Param("point") Point point);
    }

    @Configuration
    static class Config {

        @Bean
        public AnnotationRpcProviderDiscoverer annotationRpcProviderDiscoverer() {
            return new AnnotationRpcProviderDiscoverer();
        }

        @Bean
        public Demo1Service demo1Service() {
            return new Demo1ServiceImpl();
        }

        @Bean
        public Demo2Service demo2Service() {
            return new Demo2ServiceImpl();
        }
    }

    public static class Demo1ServiceImpl implements Demo1Service {

        @Override
        public String sayHello() {
            return "";
        }

        @Override
        public String sayHello(String name) {
            return "";
        }

        @Override
        public String sayHello(String firstName, String lastName) {
            return firstName + lastName;
        }

        @Override
        public void returnVoidMethod(String name) {
        }

        @Override
        public void bothVoidMethod() {
        }
    }

    public static class Demo2ServiceImpl implements Demo2Service {

        @Override
        public Point method(String name, Point point) {
            return new Point(1, 1);
        }
    }

}