package com.mss.gtr.lrpc.registry.loadbalancer;

import java.util.Collection;
import java.util.List;

/**
 * 负载均衡选择器
 *
 * @param <T>
 */
public interface ServiceLoadBalancer<T> {


    T select(Collection<T> servers, int hashCode);
}
