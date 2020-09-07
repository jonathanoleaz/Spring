package com.bolsadeideas.springboot.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
public class ClienteController {

	/* Con Qualifier se especifica que implementacion de la interfaz se inyectara */
	@Autowired
	// @Qualifier(value = "clienteServiceImpl")
	private IClienteService clienteService;

	@RequestMapping(value = "listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 3);

		Page<Cliente> clientes= clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
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
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente (hubo errores)");
			return "form";
		}
		
		clienteService.save(cliente);
		//status.setComplete();
		/*Los atributos de flash estan disponibles en el request del redirect */
		flash.addAttribute("success", "Se guardo el cliente");
		return "redirect:/listar";
	}
	
	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if(id>0) {
			cliente= clienteService.findOne(id);
			if(cliente==null){
				flash.addAttribute("error", "El ID del cliente no exise en BD");
				return "redirect:/listar";
			}
			
		}else {
			flash.addAttribute("error", "El ID del cliente no puede ser cero");
			return "redirect:/listar";
		}

		clienteService.save(cliente);
		flash.addAttribute("success", "Se edito el cliente");
		model.put("cliente", cliente);
		model.put("Editar cliente", "Formulario de cliente");
		/*if(result.hasErrors()) {
			model.put("titulo", "Formulario de cliente (hubo errores)");
			return "form";
		}*/
		
		return "form";
	}

	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash){
		if(id>0){
			clienteService.delete(id);
			flash.addAttribute("success", "Se eliminó el cliente");
		}

		return "redirect:/listar";
	}
}
