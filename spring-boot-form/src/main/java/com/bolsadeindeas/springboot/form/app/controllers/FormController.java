package com.bolsadeindeas.springboot.form.app.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeindeas.springboot.form.app.models.domain.Usuario;
import com.bolsadeindeas.springboot.form.app.validations.UsuarioValidador;

/*SessionAtributes: permite guardar los atributos que no sean mapeados por el formulario */
@Controller
@SessionAttributes("usuario")
public class FormController {
	
	@Autowired
	private UsuarioValidador validador;
	
	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario= new Usuario();
		usuario.setNombre("Nombre por defecto");
		usuario.setApellido("Apellido por defecto");
		
		/*Este dato no esta mapeado en el formulario */
		usuario.setIdentificador("123.123.1-K");
		model.addAttribute("titulo", "Formulario usuarios");
		model.addAttribute("usuario", usuario);
		return "form";
	}
	
	
	/*@ModelAtribute: cambia el nombre del objeto pero unicamente en la vista para que en los th:value usemos usuario y no usuario, que es como se llama tal objeto aca, en el controlador*/
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model, SessionStatus status
	) {

		validador.validate(usuario, result);

		if(result.hasErrors()) {
			/*Forma manual de obtener mensajes de error*/
			/*
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err->{
				errores.put(err.getField(), "El campo ".concat(err.getField().concat(" ").concat(err.getDefaultMessage())));
				
			});
			model.addAttribute("error", errores);
			*/
			model.addAttribute("titulo", "Resultado Form (Hay errores en los campos)");
			return "form";
		}
		
		/*Se pasan atributos a la vista*/
		model.addAttribute("titulo", "Resultado Form");
		model.addAttribute("usuario", usuario);
		
		status.setComplete();
		
		return "resultado";
	}
}
