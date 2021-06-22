package com.bolsadeindeas.springboot.form.app.interceptors;

//import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**Se le da un nombre para especificar, si hay mas de un interceptor que implemente estos metodos, Spring sepa cual inyectar */
@Component("tiempoTranscurridoInterceptor")
public class TiempoTranscurridoInterceptor implements HandlerInterceptor{

    /*Atributo para poder ir registrando en la traza o log */
    private static final Logger logger = LoggerFactory.getLogger(TiempoTranscurridoInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
    
        if(handler instanceof HandlerMethod){
            HandlerMethod metodo =  (HandlerMethod) handler;
            logger.info("es un metodo del controlador: "+metodo.getMethod().getName());
        }

        logger.info("TiempoTranscurridoInterceptor: preHandle() entrando ...");
        long tiempoInicio = System.currentTimeMillis();
        
        /*Guardamos en atributo de request el tiempo de inicio */
        request.setAttribute("tiempoInicio", tiempoInicio);

        //Random random=new Random();
        /*entero aleatorio entre 0 y 499 */
        //Integer demora = random.nextInt(500);
        //Thread.sleep(demora);

        return true;
        /*Se  continua con la ejecucion del controlador y demas interceptores*/
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

        long tiempoFin = System.currentTimeMillis();
        long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
        long tiempoTranscurrido = tiempoFin - tiempoInicio;

        if(handler instanceof HandlerMethod && modelAndView!=null){
            modelAndView.addObject("tiempoTranscurrido", tiempoTranscurrido);
        }

        logger.info("Tiempo transcurrido: "+tiempoTranscurrido+" milisegundos");
        logger.info("TiempoTranscurridoInterceptor: postHandle() saliendo ...");
        /*Se pueden pasar datos a la vista con el modelAndView en este tipo de interceptor */
    }

}