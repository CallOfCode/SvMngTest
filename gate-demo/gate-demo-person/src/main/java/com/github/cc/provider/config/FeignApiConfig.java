package com.github.cc.provider.config;

import com.github.cc.gate.gateway.interceptor.FeignInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignApiConfig {
    @Value("${gate.client.clientId}")
    private String clientId;
    @Value("${gate.client.secret}")
    private String secret;
    @Value("${gate.client.authHeader}")
    private String authHeader;
    @Value("${gate.client.authHost}")
    private String authHost;

    @Bean
    public FeignInterceptor getFeignInterceptor(){
        return new FeignInterceptor(clientId,secret,authHeader,authHost);
    }
}
