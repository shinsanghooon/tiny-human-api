package com.tinyhuman.tinyhumanapi.auth.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "spring.jwt")
public class JwtProperties {

    private String issuer;
    private String secretKey;

}
