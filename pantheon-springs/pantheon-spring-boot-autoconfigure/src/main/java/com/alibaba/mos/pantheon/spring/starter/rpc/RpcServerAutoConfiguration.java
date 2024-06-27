package com.alibaba.mos.pantheon.spring.starter.rpc;

import com.alibaba.mos.pantheon.extend.rpc.spring.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
        DefaultServerInvoker invoker = new DefaultServerInvoker();
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
        registrationBean.addUrlMappings("/api/*");
        registrationBean.setServlet(rpcServerServlet);
        return registrationBean;
    }
}
