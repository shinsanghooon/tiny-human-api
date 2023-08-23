package com.tinyhuman.tinyhumanapi.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    /**
     * 사용자 패스워드 저장 및 인증에 사용되는 Bean
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        // auto salting
        return new BCryptPasswordEncoder(4);
    }

}
