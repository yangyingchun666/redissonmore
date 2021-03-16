package com.example.redisson.euraka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurakaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurakaApplication.class, args);
    }

}
