package com.example.demo.interceptor;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component("horario")
public class HorarioInterceptor implements HandlerInterceptor {
	
	/*Atributo para poder ir registrando en la traza o log */
    private static final Logger logger = LoggerFactory.getLogger(HorarioInterceptor.class);

	@Value("${config.horario.apertura}")
	private Integer apertura;
	@Value("${config.horario.cierre}")
	private Integer cierre;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Calendar calendar = Calendar.getInstance();
		int hora = calendar.get(Calendar.HOUR_OF_DAY);

		logger.info("entro interceptor");
		//if (hora >= apertura && hora < cierre) {
			StringBuilder mensaje = new StringBuilder("Bienvenido al horario de atencion a clientes");
			mensaje.append(", atendemos desde las ");
			mensaje.append(apertura);
			mensaje.append(" hasta las ");
			mensaje.append(cierre);
			mensaje.append(" Gracias por la visita ");

			request.setAttribute("mensaje", mensaje.toString());

			//return true;
		//}
		response.sendRedirect(request.getContextPath().concat("/cerrado"));
		return false;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		String mensaje = (String) request.getAttribute("mensaje");
		
		if(modelAndView != null && handler instanceof HandlerMethod) {
			modelAndView.addObject("horario", mensaje);
		}
	}

}
