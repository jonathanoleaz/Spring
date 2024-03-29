package com.bolsedeideas.springboot.di.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bolsedeideas.springboot.di.app.models.domain.Factura;

@Controller
@RequestMapping("/factura")
public class FacturaController {

	/** Por defecto los componentes son singleton: una sola instancia en toda la aplicacion*/
	/** Se inyecta en el controlador la clase Factura*/
	@Autowired
	private Factura factura;
	
	@GetMapping("/ver")
	public String var(Model model) {
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Ejemplo de Factura con inyeccion de dependencia");
		return "factura/ver";
	}
}
