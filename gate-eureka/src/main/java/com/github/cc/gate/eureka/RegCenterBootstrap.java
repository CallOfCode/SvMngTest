package com.github.cc.gate.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class RegCenterBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(RegCenterBootstrap.class, args);
    }
}
