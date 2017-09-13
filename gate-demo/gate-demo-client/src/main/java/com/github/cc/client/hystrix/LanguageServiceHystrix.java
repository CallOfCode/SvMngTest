package com.github.cc.client.hystrix;

import com.github.cc.client.rpc.ILanguageService;
import org.springframework.stereotype.Component;

@Component
public class LanguageServiceHystrix implements ILanguageService {

    @Override
    public String sayChineseHelloWorld() {
        return "hystrix 哑巴！";
    }

    @Override
    public String sayEnglishHelloWorld() {
        return "hystrix 哑巴！";
    }
}
