package com.coherent.unnamed.logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LogicServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogicServiceApplication.class, args);
    }

}
