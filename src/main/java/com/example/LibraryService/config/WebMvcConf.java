package com.example.LibraryService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConf implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**").allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin",
                        "X-Requested-With", "Content-Type", "Accept", "X-Auth-Token",
                        "X-Csrf-Token", "Authorization")
                .exposedHeaders("token_header1", "token_header2")
                .allowCredentials(false)
                .maxAge(3_600);
    }

}
