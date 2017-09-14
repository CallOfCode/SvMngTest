package com.github.cc.provider.rpc;

import com.github.cc.provider.hystrix.LanguageServiceHystrix;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "gate-demo-language",fallback = LanguageServiceHystrix.class)
@Qualifier("languageService")
public interface ILanguageService {
    @RequestMapping(value = "/language/chinese",method = RequestMethod.GET)
    public String sayChineseHelloWorld();
    @RequestMapping(value = "/language/english",method = RequestMethod.GET)
    public String sayEnglishHelloWorld();
}
