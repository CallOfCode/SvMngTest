package com.github.cc.client.rest;

import com.github.cc.gate.gateway.annotation.ApiGateSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @ApiGateSecurity
    @RequestMapping("/test/say")
    public String sayHello(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello!"+new Date().toString();
    }

}
