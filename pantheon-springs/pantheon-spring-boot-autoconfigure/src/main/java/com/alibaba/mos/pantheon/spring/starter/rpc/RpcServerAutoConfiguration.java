package com.alibaba.mos.pantheon.spring.starter.rpc;

import com.alibaba.mos.pantheon.extend.rpc.spring.AnnotationRpcProviderDiscoverer;
import com.alibaba.mos.pantheon.extend.rpc.spring.RpcInvokeServlet;
import com.alibaba.mos.pantheon.extend.rpc.spring.RpcProviderDiscoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RpcInvokeServlet.class})
public class RpcServerAutoConfiguration {


    @ConditionalOnMissingBean
    @Bean
    public RpcProviderDiscoverer defaultGrpcServiceDiscoverer() {
        return new AnnotationRpcProviderDiscoverer();
    }

    @ConditionalOnMissingBean
    @Bean
    public RpcInvokeServlet rpcInvokeServlet() {
        return new RpcInvokeServlet();
    }

    @ConditionalOnMissingBean
    @Bean
    public ServletRegistrationBean<RpcInvokeServlet> rpcServerServlet(RpcInvokeServlet rpcInvokeServlet) {
        ServletRegistrationBean<RpcInvokeServlet> registrationBean = new ServletRegistrationBean<>();
        registrationBean.addUrlMappings("/api");
        registrationBean.setServlet(rpcInvokeServlet);
        return registrationBean;
    }
}
