package com.alibaba.mos.pantheon.extend.rpc.spring;

import java.util.Collection;

@FunctionalInterface
public interface RpcProviderDiscoverer {

    Collection<RpcProviderDefinition> findProviders();

}
