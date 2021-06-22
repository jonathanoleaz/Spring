package com.bolsedeideas.springboot.di.app.models.service;

//import org.springframework.stereotype.Component;

/**Toda clase con @Component, debe tener al menos un constructor vacio.*/
/**Anotacion de component implica que una clase sera singleton
 * Anotacion service es similar a la de Component, patron de diseño Facade. */
//@Component("miServicioSimple")
public class MiServicio implements IServicio{

	/*Accede a DAOs, APIS con cliente http, etc.
	 * Override se implementa o sbrecarga el metodo*/
	@Override
	public String operacion() {
		return "ejecutando un proceso importante desde el servicio simple";
	}
}
