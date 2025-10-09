package com.athlos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AthlosBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AthlosBackendApplication.class, args);
    }
}

