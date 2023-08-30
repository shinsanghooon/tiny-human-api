package com.tinyhuman.tinyhumanapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfiguration {

    @Bean
    public OpenAPI tinyHumanOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tiny Human API")
                        .description("Tiny Human Application")
                        .version("v0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Tiny Human API Github Repository")
                        .url("https://github.com/shinsanghooon/tiny-human-api"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("tinyhuman-public")
                .pathsToMatch("/api/v1/**")
                .packagesToScan("com.tinyhuman.tinyhumanapi")
                .build();
    }

//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("tinyhuman-admin")
//                .pathsToMatch("/admin/**")
//                .addOpenApiMethodFilter(method -> method.isAnnotationPresent(Admin.class))
//                .build();
//    }

}
