package com.bolsadeideas.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*Notar que para el caso de formulario de login, Spring ya manda un atributo 'error' o 'logout' si la peticion de login no fue satisfactoria */
@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(value="error", required=false) String error,
                        @RequestParam(value="logout", required=false) String logout,
                        Model model, 
                        Principal principal, 
                        RedirectAttributes flash){
    
                            /*Si viene de cerrar sesion */
        if(logout!=null){
            flash.addFlashAttribute("info", "Cerró sesión con éxito.");
        }
        
        /*Si ya inicio sesion */
        if(principal!=null){
            flash.addFlashAttribute("info", "Ya había iniciado sesión");
            return "redirect:/";
        }

        if(error != null){
            model.addAttribute("error", "Error en el login: nombre de usuario o contrasenia incorrectos.");
        }

        

        return "login";
    }
}
