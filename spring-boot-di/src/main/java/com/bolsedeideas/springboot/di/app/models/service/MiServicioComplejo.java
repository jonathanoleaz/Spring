package com.bolsedeideas.springboot.di.app.models.service;

//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Component;

/**Toda clase con @Component, debe tener al menos un constructor vacio. */
/**Anotacion de component implica una clase singleton
 * Anotacion service es similar a la de Component, service va orientado al patron de diseño Facade
 * Anotacion Primary: para que se el metodo (implementacion) por defecto en la interfaz*/
//@Component("miServicioComplejo")
//@Primary
public class MiServicioComplejo implements IServicio{

	/*Accede a DAOs, APIS con cliente http, etc.
	 * Override se implementa o sobrecarga el metodo*/
	@Override
	public String operacion() {
		return "ejecutando un proceso importante desde servicio complejo";
	}
}
