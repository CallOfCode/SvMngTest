package com.github.cc.gate.gateway.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = "com.github.cc.gate.gateway")
@EnableCaching(proxyTargetClass = true)
public class CacheConfig implements CachingConfigurer {
    @Bean
    @Override
    public CacheManager cacheManager() {
        try {
            net.sf.ehcache.CacheManager ehCacheManager = new net.sf.ehcache.CacheManager(new ClassPathResource("ehcache.xml").getInputStream());
            EhCacheCacheManager cacheManager = new EhCacheCacheManager(ehCacheManager);
            return cacheManager;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }
}
