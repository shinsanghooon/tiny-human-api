package com.tinyhuman.tinyhumanapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TinyHumanApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyHumanApiApplication.class, args);
    }
}

