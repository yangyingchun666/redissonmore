package com.yyc.lock_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yyc.lock_demo.mapper")
@EnableEurekaClient
public class LockDemo11Application {

    public static void main(String[] args) {
        SpringApplication.run(LockDemo11Application.class, args);
    }

    @Bean
    Redisson redissonSentinel() {
        // 支持单机，主从，哨兵，集群等模式
        // 此为哨兵模式
        Config config = new Config();
        config
                .useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setPassword("12345");
        return (Redisson) Redisson.create(config);
    }
}
