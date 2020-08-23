package com.bolsedeideas.springboot.di.app.models.domain;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

/*Componente de Spring*/
/*RequestScope: componente en ambito de la peticion o request: dura lo que dura una peticion http*/
/*SessionScope: debe implementar la interfaz serializable.*/
/*ApplicationScope: se guarda en contexto del servlet, no de la aplicacion de Spring*/
@Component
@RequestScope
public class Factura implements Serializable{

	/**
	 * Es un atributo que se maneja como identificador
	 */
	private static final long serialVersionUID = -4511426373268604849L;

	@Value("${factura.descripcion}")
	private String descripcion;
	
	@Autowired
	private Cliente cliente;
	
	@Autowired
	@Qualifier(value = "itemFacturaOficina")
	private List<ItemFactura> items;
	
	/*@PostConstruct se ejecuta justo despues de crear el objeto y al haber inyectado las dependencias*/
	@PostConstruct
	public void inicializar() {
		cliente.setNombre(cliente.getNombre().concat(" ").concat("Se&ntilde;or"));
		descripcion = descripcion.concat(" del cliente: ").concat(cliente.getNombre());
	}
	/*@PreDestroy: se ejecuta al destruir el objeto o instancia*/
	@PreDestroy
	public void destruir() {
		System.out.println("Factura destruida");
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<ItemFactura> getItems() {
		return items;
	}
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	public Factura(String descripcion, Cliente cliente, List<ItemFactura> items) {
		this.descripcion = descripcion;
		this.cliente = cliente;
		this.items = items;
	}
	public Factura() {
	}
	
	
	
}
