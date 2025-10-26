package com.maysu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cuando alguien pida /images/productos/**,
        // Spring buscará en D:/imagenes/
        registry.addResourceHandler("/images/productos/**")
                .addResourceLocations("file:D:/imagenes/");
    }
}
