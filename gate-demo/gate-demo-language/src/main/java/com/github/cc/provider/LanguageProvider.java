package com.github.cc.provider;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LanguageProvider {
    public static void main(String[] args){
        new SpringApplicationBuilder(LanguageProvider.class).web(true).run(args);
    }

}
