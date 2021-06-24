package com.mss.gtr.lrpc.core;

import com.mss.gtr.lrpc.core.util.LRpcFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LRpcRequestHolder {
    private static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, LRpcFuture<LRpcResponse>> REQUEST_MAP
            = new ConcurrentHashMap<>();
}
