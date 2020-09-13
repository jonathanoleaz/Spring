package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*Con sessionAttributes se marcan atributos generalmente del model que persistiran mientras dure la peticion, y hasta marcar status.setComplete(); se limpiaran  */
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value="clienteId") Long clienteId, 
                Map<String, Object> model, 
                RedirectAttributes flash){

        Cliente cliente = clienteService.findOne(clienteId);
        if(cliente==null){
            flash.addFlashAttribute("error", "El cliente no existe en la BD");
            return "redirect:/listar";
        }

        Factura factura=new Factura();
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("titulo", "Crear factura");

        return "factura/form";
    }

    /*Con responseBody se suprime el devolver un cargado de una vista de thymeleaf, devolviendo el json (en este caso)*/
    @GetMapping(value="/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
        return clienteService.findByNombre(term);
    } 
}
