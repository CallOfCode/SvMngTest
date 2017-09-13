package com.github.cc.provider.api;

import com.github.cc.gate.gateway.annotation.ApiGateSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/language")
@ApiGateSecurity
public class LanguageController {
    @Value("${service.version}")
    String version;

    @RequestMapping(value = "/chinese",method = RequestMethod.GET)
    public String sayChineseHelloWorld() throws InterruptedException {
        return "你好，世界！"+version;
    }
    @RequestMapping(value = "/english",method = RequestMethod.GET)
    public String sayEnglishHelloWorld(){
        return "Hello World！"+version;
    }

}
