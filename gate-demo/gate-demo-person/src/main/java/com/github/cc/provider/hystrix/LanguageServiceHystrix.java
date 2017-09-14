package com.github.cc.provider.hystrix;

import com.github.cc.provider.rpc.ILanguageService;
import org.springframework.stereotype.Component;


@Component
public class LanguageServiceHystrix implements ILanguageService{
    public String sayChineseHelloWorld() {
        return "hystrix 哑巴！";
    }

    public String sayEnglishHelloWorld() {
        return "hystrix dummy!";
    }
}
