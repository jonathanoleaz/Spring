package com.bolsadeindeas.springboot.form.app;

//import com.bolsadeindeas.springboot.form.app.interceptors.TiempoTranscurridoInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**La anotacion Configuration sirve para poder registrar beans en el contenedor de Spring*/
@Configuration
public class MvcConfig implements WebMvcConfigurer{
    
    @Autowired
    @Qualifier("tiempoTranscurridoInterceptor")
    private HandlerInterceptor tiempoTranscurridoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        /*De esta forma el interceptor se aplica solo a ciertas rutas */
        //registry.addInterceptor(tiempoTranscurridoInterceptor).addPathPatterns("/form/**");
        registry.addInterceptor(tiempoTranscurridoInterceptor);
    }
}