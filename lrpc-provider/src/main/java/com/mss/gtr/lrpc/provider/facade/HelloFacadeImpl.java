package com.mss.gtr.lrpc.provider.facade;

import com.mss.gtr.lrpc.core.annotation.RpcService;
import com.mss.gtr.lrpc.facade.HelloFacade;
import org.springframework.stereotype.Component;

@Component
@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0")
public class HelloFacadeImpl implements HelloFacade {

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }
}
