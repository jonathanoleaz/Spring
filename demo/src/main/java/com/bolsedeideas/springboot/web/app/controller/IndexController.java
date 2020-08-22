package com.bolsedeideas.springboot.web.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bolsedeideas.springboot.web.app.models.Usuario;

@Controller
@RequestMapping("/app") /*Ruta generica para todos los metodos del controlador*/
public class IndexController {

	@Value("${texto.indexcontroller.index.titulo}")
	private String textoIndex;
	
	@Value("${texto.indexcontroller.perfil.titulo}")
	private String textoPerfil;
	
	@Value("${texto.indexcontroller.listar.titulo}")
	private String textoListar;
	
	/*Encargado de manejar peticiones del usuario*/
	/*Cada metodo maneja una peticion*/
	/*Debe crearse una plantilla llamada index*/
	
	/*Cada metodo se debe mapear a una ruta con la notacion request mapping, el metodo http por defecto es get
	 * Tambien puede ser con GetMapping o PostMapping*/
	/*Varias rutas pueden mapear a un metodo*/
	
	/*Datos de control a la vista: ModelMap y Model no tienen diferencia al usarlos, pero estan implementados de forma diferente
	 * tambien puede usarse Map, pero se usa de forma diferente
	 * Otra forma es con ModelAndView*/
	@RequestMapping(value={"/index", "/", "", "/home"}, method=RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("titulo", textoIndex);
		return "index";
	}
	
	@RequestMapping("/perfil")
	public String perfil(Model model) {
		Usuario usuario=new Usuario();
		usuario.setNombre("Juan");
		usuario.setApellido("Apellido");
		model.addAttribute("usuario", usuario);
		
		model.addAttribute("titulo", textoPerfil.concat(usuario.getNombre()));
		return "perfil";
	}
	
	@RequestMapping("/listar")
	public String listar(Model model) {
		List<Usuario> usuarios=new ArrayList<>();
		usuarios.add(new Usuario("Andres", "Guzman", "andres@correo.com"));
		usuarios.add(new Usuario("Tornado", "Roe", "andres@correo.com"));
		usuarios.add(new Usuario("Sergio", "Cuevas", "andres@correo.com"));
		model.addAttribute("titulo", textoListar);
		
		return "listar";
	}
	
	/*De esta forma se deja el objeto usuarios, comun a todos los metodos del controlador, como los select que son filtros*/
	@ModelAttribute("usuarios")
	public List<Usuario> poblarUsuarios(){
		List<Usuario> usuarios=new ArrayList<>();
		usuarios.add(new Usuario("Andres", "Guzman", "andres@correo.com"));
		usuarios.add(new Usuario("Tornado", "Roe", "andres@correo.com"));
		usuarios.add(new Usuario("Sergio", "Cuevas", "andres@correo.com"));
	
		return usuarios;
	}
}
