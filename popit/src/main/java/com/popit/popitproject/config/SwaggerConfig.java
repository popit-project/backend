package com.popit.popitproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        Predicate<RequestHandler> userControllerPredicate = RequestHandlerSelectors.basePackage("com.popit.popitproject.user.controller");
        Predicate<RequestHandler> itemControllerPredicate = RequestHandlerSelectors.basePackage("com.popit.popitproject.item.controller");

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(userControllerPredicate.or(itemControllerPredicate))
                .paths(PathSelectors.any())
                .build();
    }
}