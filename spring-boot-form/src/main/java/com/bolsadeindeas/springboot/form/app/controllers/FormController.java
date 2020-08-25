package com.bolsadeindeas.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeindeas.springboot.form.app.editors.NombreMayusculasEditor;
import com.bolsadeindeas.springboot.form.app.editors.PaisPropertyEditor;
import com.bolsadeindeas.springboot.form.app.models.domain.Pais;
import com.bolsadeindeas.springboot.form.app.models.domain.Usuario;
import com.bolsadeindeas.springboot.form.app.services.PaisService;
import com.bolsadeindeas.springboot.form.app.validations.UsuarioValidador;

/*SessionAtributes: permite guardar los atributos que no sean mapeados por el formulario */
@Controller
@SessionAttributes("usuario")
public class FormController {
	
	@Autowired
	private UsuarioValidador validador;

	@Autowired
	private PaisService paisService;

	@Autowired
	private PaisPropertyEditor paisEditor;

	/*Cuando se iniciliza el binder, binder es como un portafolio donde se realizan validaciones */
	@InitBinder
	public void InitBinder(WebDataBinder binder){
		binder.addValidators(validador);

		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		/*Si el analizador es estricto o tolerante, lo ideal es ser estricto para evitar ambiguedad en la fecha */
		dateFormat.setLenient(false);
		 /*El 2do argumento es opcional, y es para especificar a que atributo se aplicara, sino se especifica, se aplicara a todos 
		 los atributos tipo fecha */
		binder.registerCustomEditor(Date.class, "fechaNacimiento",new CustomDateEditor(dateFormat, false));

		//binder.registerCustomEditor(String.class, "nombre", new NombreMayusculasEditor());
		binder.registerCustomEditor(String.class, new NombreMayusculasEditor());
	
		binder.registerCustomEditor(Pais.class, "pais", paisEditor);
	}

	@ModelAttribute("paises")
	public List<String> paises(){
		return Arrays.asList("México", "Chile", "Colombia", "Canada");
	}

	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap(){
		Map<String, String> paises= new HashMap<String, String>();
		paises.put("MX", "México");
		paises.put("CH", "Chile");
		paises.put("CO", "Colombia");
		paises.put("CA", "Canada");
		return paises;
	}

	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return paisService.listar();
		/**Ahora usamos el service a traves de una interfaz */
		/*return Arrays.asList(
			new Pais(1, "MX","México"), 
			new Pais(2, "CH", "Chile"), 
			new Pais(3, "CO", "Colombia"), 
			new Pais(4, "CA", "Canada")
		);*/
	}

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
