package com.bolsadeideas.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /* Clase utilizada para mostrar la imagen */
    /*
     * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * WebMvcConfigurer.super.addResourceHandlers(registry);
     * 
     * String resourcePath =
     * Paths.get("uploads").toAbsolutePath().toUri().toString(); //se va a mpear un
     * directorio, los dos asteriscos indican que sera un nombre variable
     * registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath)
     * ; }
     */
    /* Metodo para manejar paginas de error (403, 404, etc) */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error_403");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));
        return localeResolver;
    }

    //Interceptor para cambiar idioma
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    //Registramos interceptor de cambio de idioma
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(localeChangeInterceptor());
    }


    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        /*Se especfica clase (elemento) root o raiz del xml a obtener */
        marshaller.setClassesToBeBound(new Class[] {com.bolsadeideas.springboot.app.view.xml.ClienteList.class});


        return marshaller;
    }
}
