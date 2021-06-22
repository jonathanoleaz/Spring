package com.bolsedeideas.springboot.di.app.models.service;

/** Una interfaz define que operaciones implementaran las clases concretas
 *por ejemplo con DAOs: uno que lo haga con consultas nativas, otro con Hibernate, otro con JPA, etc. */
public interface IServicio {

	String operacion();

}
