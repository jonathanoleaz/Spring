package com.bolsadeideas.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.bolsadeideas.demo.errors.UsuarioNoEncontradoException;
import com.bolsadeideas.demo.models.domain.Usuario;
import com.bolsadeideas.demo.services.UsuarioService;

@Controller
public class AppController {
	/*	Inyectamos por la interfaz del service*/
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/index")
	public String index() {
		/*Integer valor=0/0;
		Integer valor= Integer.parseInt("10v");*/
		return "index";
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Integer id, Model model) {
		//Usuario usuario= usuarioService.obtenerPorId(id);
		
		/*De esta forma se obtiene el usuario, sino se encuentra, directamente se devuelve la expresion personalizada*/
		Usuario usuario= usuarioService.obtenerPorIdOptional(id).orElseThrow(() -> new UsuarioNoEncontradoException(id.toString()));
		
		/*if(usuario==null) {
			throw new UsuarioNoEncontradoException(id.toString());
		}*/
		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", "Detalle del usuario en ruta ver");
		return "ver";
	}
}
