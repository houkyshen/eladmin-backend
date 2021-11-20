package com.jeff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EladminBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(EladminBackendApplication.class, args);
        System.out.println("http://localhost:8080");
    }
}
