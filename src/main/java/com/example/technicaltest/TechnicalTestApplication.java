package com.example.technicaltest;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Data
@SpringBootApplication
public class TechnicalTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechnicalTestApplication.class, args);
    }
}
