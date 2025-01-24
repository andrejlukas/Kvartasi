package com.mojkvart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MojKvartApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MojKvartApplication.class, args);
    }
}