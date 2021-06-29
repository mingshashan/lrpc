package com.mss.gtr.lrpc.protocol.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求任务提交
 */
public class LRpcRequestProcessor {

    private static ThreadPoolExecutor threadPoolExecutor;

    public static void submitRequest(Runnable task) {
        if (null == threadPoolExecutor) {
            threadPoolExecutor = new ThreadPoolExecutor(
                    10, 20, 60, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(Integer.MAX_VALUE)
            );
        }
        threadPoolExecutor.submit(task);
    }
}
