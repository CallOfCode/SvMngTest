package com.github.cc.client.rpc;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "gate-demo-provider" )
public interface ILanguageService {
    @RequestMapping(value = "/language/chinese",method = RequestMethod.GET)
    public String sayChineseHelloWorld();
    @RequestMapping(value = "/language/english",method = RequestMethod.GET)
    public String sayEnglishHelloWorld();
}
