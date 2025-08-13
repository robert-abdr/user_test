package com.example.user_test_api;

import org.springframework.boot.SpringApplication;

public class TestUserTestApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(UserTestApiApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
