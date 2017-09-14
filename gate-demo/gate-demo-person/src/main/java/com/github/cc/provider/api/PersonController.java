package com.github.cc.provider.api;

import com.github.cc.gate.gateway.annotation.ApiGateSecurity;
import com.github.cc.provider.rpc.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.hystrix.security.HystrixSecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
@ApiGateSecurity
public class PersonController {

    @Autowired
    @Qualifier("languageService")
    private ILanguageService languageService;

    @RequestMapping(value = "/sayHello",method = RequestMethod.GET)
    public String sayHello() throws InterruptedException {
        return "person say:"+languageService.sayEnglishHelloWorld();
    }

}
