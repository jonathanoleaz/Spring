package com.bolsadeideas.demo.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bolsadeideas.demo.errors.UsuarioNoEncontradoException;

/*Para capturar lanzamiento de excepciones, en ve de mapear a una ruta, se mapea a una excepcion*/
@ControllerAdvice
public class ErrorHandlerController {

	@ExceptionHandler(ArithmeticException.class)
	public String aritmeticaError(Exception ex, Model model) {
		model.addAttribute("error", "Error de aritmética");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		return "error/aritmetica";
	}
	
	@ExceptionHandler(NumberFormatException.class)
	public String numberFormatError(NumberFormatException ex, Model model) {
		model.addAttribute("error", "Error de parseo de Integer");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		return "error/numero-formato";
	}
	
	@ExceptionHandler(UsuarioNoEncontradoException.class)
	public String usuarioNoEncontrado(UsuarioNoEncontradoException ex, Model model) {
		model.addAttribute("error", "Usuario no encontrado");
		model.addAttribute("message", ex.getMessage());
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", new Date());
		return "error/numero-formato";
	}
}
