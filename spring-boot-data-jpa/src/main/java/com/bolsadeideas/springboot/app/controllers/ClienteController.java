package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.UploadFileServiceImpl;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
public class ClienteController {

	/* Con Qualifier se especifica que implementacion de la interfaz se inyectara */
	@Autowired
	// @Qualifier(value = "clienteServiceImpl")
	private IClienteService clienteService;

	@Autowired
	private UploadFileServiceImpl uploadFileServiceImpl;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/*
	 * Se usa expresion regular en la url para que spring no trunce o corte la
	 * extension del archivo
	 */
	/*
	 * Implementacion manual o programatica para enviar una imagen, notar que el
	 * metodo mapea a la url '/uploads/{filename}' , misma que esta contenida en el
	 * atriuto th:src del <img> de la vista ver
	 */
	/* Notar que */
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileServiceImpl.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename())
				.body(recurso);
	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);
		if (cliente == null) {
			flash.addFlashAttribute("error", "No se encontró el cliente");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle del cliente: " + cliente.getNombre());

		return "ver";
	}

	@RequestMapping(value = "listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 3);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

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

	/* Se debe agregar anotacion @Valid para que se valide el objeto reciido */
	/*
	 * Cliente (objeto que se recibe de formulario) y BindingResult siempre van
	 * seguidos o juntos
	 */
	/*
	 * Como el atributo cliente se llama igual en el metodo crear() y en el metodo
	 * guardar(), no es necesario volver a pasarlo, pero si tuviera nombre distinto,
	 * despues de @Valid se usa @ModelAttribute("clienteOtro")
	 */
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de cliente (hubo errores)");
			return "form";
		}

		if (!foto.isEmpty()) {

			// cliente existe y ya tiene foto, entonce se elimina foto previa
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadFileServiceImpl.delete(cliente.getFoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileServiceImpl.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cliente.setFoto(uniqueFileName);

			flash.addFlashAttribute("info", "Has subido correctamente la foto " + cliente.getFoto());
			// Path directorioRecursos = Paths.get("src//main//resources/static/uploads");

		}
		clienteService.save(cliente);
		status.setComplete();
		/* Los atributos de flash estan disponibles en el request del redirect */
		flash.addFlashAttribute("success", "Se guardo el cliente");
		return "redirect:/listar";
	}

	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no exise en BD");
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
			return "redirect:/listar";
		}

		clienteService.save(cliente);
		flash.addFlashAttribute("success", "Se edito el cliente");
		model.put("cliente", cliente);
		model.put("Editar cliente", "Formulario de cliente");
		/*
		 * if(result.hasErrors()) { model.put("titulo",
		 * "Formulario de cliente (hubo errores)"); return "form"; }
		 */

		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			boolean borrado = uploadFileServiceImpl.delete(cliente.getFoto());
			if (borrado) {
				clienteService.delete(id);
				flash.addFlashAttribute("success", "Se eliminó el cliente");
			}

		}

		return "redirect:/listar";
	}
}
