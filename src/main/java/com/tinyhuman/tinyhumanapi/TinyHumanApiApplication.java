package com.tinyhuman.tinyhumanapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties
public class TinyHumanApiApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(TinyHumanApiApplication.class, args);
//    }


    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml";
//            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TinyHumanApiApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}

