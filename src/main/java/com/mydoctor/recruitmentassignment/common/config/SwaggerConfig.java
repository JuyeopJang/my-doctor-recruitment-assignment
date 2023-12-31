package com.mydoctor.recruitmentassignment.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customOpenAPI() {
        return GroupedOpenApi.builder()
                .group("my-doctor-recruitment-v1-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI myDoctorRecruitmentOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("my-doctor-recruitment")
                        .version("v1"));
    }
}
