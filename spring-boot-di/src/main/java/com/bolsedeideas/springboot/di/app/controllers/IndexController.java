package com.bolsedeideas.springboot.di.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsedeideas.springboot.di.app.models.service.IServicio;
import com.bolsedeideas.springboot.di.app.models.service.MiServicio;

/*Inyeccion de dependencias: no se instancia el objeto (pues esta acoplado en el contenedor), sino se aplica el principio
 * Hollywood, no nos llanmes, nosotros te llamaremos, en el cual, el contenedor de Spring trae el objeto sin instanciarlo, p
 * para cambiar el codigo a futuro, de ser necesario mas facilmente*/
@Controller
public class IndexController {
	
	/*Notacion Autowired sirve para inyectar un objeto del contenedor de Spring*/
	/*Qualifier: para definir de que clase se usara si hay mas de una*/
	@Autowired
	//@Qualifier("miServicioSimple")
	private IServicio servicio;
	
	@Autowired
	public IndexController(IServicio servicio) {
		super();
		this.servicio = servicio;
	}


	@GetMapping({"/","","/index"})
	public String index(Model model) {
		model.addAttribute("objeto", servicio.operacion());
		return "index";
	}


	
	
}
