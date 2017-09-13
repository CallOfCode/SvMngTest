package com.github.cc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.FeignClient;

@SpringBootApplication
@EnableEurekaClient
@FeignClient
public class ClientBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ClientBootstrap.class, args);
    }
}
