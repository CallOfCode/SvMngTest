package com.github.cc.gate.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class GatewayBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(GatewayBootstrap.class, args);
    }
}
