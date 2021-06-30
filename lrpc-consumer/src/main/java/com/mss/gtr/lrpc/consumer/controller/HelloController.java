package com.mss.gtr.lrpc.consumer.controller;

import com.mss.gtr.lrpc.consumer.annotation.RpcReference;
import com.mss.gtr.lrpc.facade.HelloFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @RpcReference(serviceVersion = "1.0", registryAddr = "127.0.0.1:12181",
            registryType = "ZOOKEEPER", timeout = 300000)
    private HelloFacade helloFacade;

    @GetMapping()
    public String hello(@RequestParam String name) {
        return helloFacade.hello(name);
    }

}
