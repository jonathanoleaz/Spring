package com.bolsadeindeas.springboot.form.app.models.domain;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bolsadeindeas.springboot.form.app.validations.IdentificadorRegex;
import com.bolsadeindeas.springboot.form.app.validations.RequeridoRegex;

import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

	//@Pattern(regexp = "[0-9]{2}[.][\\d]{3}[.][\\d]{3}[-][a-zA-Z]{1}")
	@IdentificadorRegex
	private String identificador;

	@NotBlank
	@Size(min=3, max=8)
	private String username;

	@NotEmpty
	private String password;

	@NotEmpty
	@Email
	private String email;

	//@NotEmpty(message = "el nombre no puede ser vacio")
	private String nombre;

	//@NotEmpty
	@RequeridoRegex
	private String apellido;

	/*NotNull para objetos, NotEmpty para primitivos */
	@NotNull
	@Min(5)
	@Max(5000)
	private Integer cuenta;

	@NotNull
	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private Date fechaNacimiento;

	/*@Valid especifica que tambien deben validarse las propiedades de este atributo (pais)
	que al ser una clase a la que le creamos una validacion en su id, la tomara en cuenta */
	@NotNull
	private Pais pais;
	
	public String getNombre() {
		return nombre;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCuenta() {
		return cuenta;
	}

	public void setCuenta(Integer cuenta) {
		this.cuenta = cuenta;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}
}
