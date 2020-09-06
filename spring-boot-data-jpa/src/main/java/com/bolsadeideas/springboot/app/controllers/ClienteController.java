package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
public class ClienteController {

	/* Con Qualifier se especifica que implementacion de la interfaz se inyectara */
	@Autowired
	// @Qualifier(value = "clienteServiceImpl")
	private IClienteService clienteService;

	@RequestMapping(value = "listar", method = RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de cliente");
		return "form";
	}

	/*Se debe agregar anotacion @Valid para que se valide el objeto reciido*/
	/*Cliente (objeto que se recibe de formulario) y BindingResult siempre van seguidos o juntos*/
	/*Como el atributo cliente se llama igual en el metodo crear() y en el metodo guardar(), no es necesario volver a pasarlo,
	 * pero si tuviera nombre distinto, despues de @Valid se usa @ModelAttribute("clienteOtro")*/
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente (hubo errores)");
			return "form";
		}
		
		clienteService.save(cliente);
		return "redirect:listar";
	}
	
	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model) {
		Cliente cliente = null;
		if(id>0) {
			cliente= clienteService.findOne(id);
		}else {
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("Editar cliente", "Formulario de cliente");
		/*if(result.hasErrors()) {
			model.put("titulo", "Formulario de cliente (hubo errores)");
			return "form";
		}*/
		
		return "form";
	}

	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id){
		if(id>0){
			clienteService.delete(id);
		}

		return "redirect:/listar";
	}
}
