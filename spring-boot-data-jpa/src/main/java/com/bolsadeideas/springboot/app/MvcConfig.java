package com.bolsadeideas.springboot.app;


import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
/*Clase utilizada para mostrar la imagen */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
        //se va a mpear un directorio, los dos asteriscos indican que sera un nombre variable
        registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
    }*/
    
}
