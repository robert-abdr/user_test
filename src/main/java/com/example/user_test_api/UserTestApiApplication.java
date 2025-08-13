package com.example.user_test_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@ConfigurationPropertiesScan
public class UserTestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTestApiApplication.class, args);
    }
}
