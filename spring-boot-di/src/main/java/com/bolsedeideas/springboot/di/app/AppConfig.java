package com.bolsedeideas.springboot.di.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.bolsedeideas.springboot.di.app.models.domain.ItemFactura;
import com.bolsedeideas.springboot.di.app.models.domain.Producto;
import com.bolsedeideas.springboot.di.app.models.service.IServicio;
import com.bolsedeideas.springboot.di.app.models.service.MiServicio;
import com.bolsedeideas.springboot.di.app.models.service.MiServicioComplejo;

/*De este forma se registran los componentes en una configuracion general (y no en cada clase con las anotaciones @Component("miServicioComplejo") y opcionalmente @Primary)*/
@Configuration
public class AppConfig {

	@Bean("miServicioSimple")
	public IServicio registrarMiServicio() {
		return new MiServicio();
	}
	
	@Bean("miServicioComplejo")
	@Primary
	public IServicio registrarMiServicioComplejo() {
		return new MiServicioComplejo();
	}
	
	@Bean("itemFactura")
	public List<ItemFactura> registrarItems(){
		Producto producto1=new Producto("Camara", 300);
		Producto producto2=new Producto("Camara 2", 600);
		
		ItemFactura linea1= new ItemFactura(producto1, 2);
		ItemFactura linea2= new ItemFactura(producto2, 3);
		
		return Arrays.asList(linea1, linea2);		
	}
	
	@Bean("itemFacturaOficina")
	public List<ItemFactura> registrarItemsOficina(){
		Producto producto1=new Producto("Monitor LCD 24", 400);
		Producto producto2=new Producto("Notebook", 600);
		Producto producto3=new Producto("Escritorio", 900);
		Producto producto4=new Producto("Bocina", 900);
		
		ItemFactura linea1= new ItemFactura(producto1, 2);
		ItemFactura linea2= new ItemFactura(producto2, 3);
		ItemFactura linea3= new ItemFactura(producto3, 3);
		ItemFactura linea4= new ItemFactura(producto4, 1);
		
		return Arrays.asList(linea1, linea2, linea3, linea4);		
	}
}
