package com.alibaba.mos.pantheon.extend.rpc.spring;

import com.alibaba.mos.pantheon.extend.rpc.provider.ProviderDefinition;

import java.util.Collection;

@FunctionalInterface
public interface ProviderDiscoverer {

    Collection<ProviderDefinition> findProviders();

}
