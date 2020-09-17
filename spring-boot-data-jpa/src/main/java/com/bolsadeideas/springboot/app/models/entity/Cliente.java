package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// @Column(name="nombre_cliente", length = )
	@NotEmpty
	/* @Size(min=4, max=12) */
	private String nombre;

	@NotEmpty
	private String apellido;

	@NotEmpty
	@Email
	private String email;

	@NotNull
	@Column(name = "created_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;

	/** Un cliente tiene muchas facturas */
	/**
	 * Con lazy, las clases se traen de forma que se van requiriendo, no se cargan
	 * las clases desde el principio. cascade para especificar si un delete o update
	 * aplicara a todas las clases "hijas" mappedBy: se debe usar cuando la relacion
	 * es bidireccional entre las clases, especificando el nombre del atributo en la
	 * clase apuntada, esto implica que en Factura, se cree la llave foranea del
	 * cliente. No usamos JoinColumn porque la relacion se especifica como
	 * bidireccional.
	 */
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Factura> facturas;

	private String foto;

	// @PrePersist metodo llamado justo ants de invocar el metodo persist, de
	// insertarlo en la bd
	/*
	 * @PrePersist public void prePersist() { createdAt = new Date(); }
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public List<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}

	public void addFactura(Factura factura) {
		facturas.add(factura);
	}

	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	@Override
	public String toString() {
		return "Cliente [apellido=" + apellido + ", createdAt=" + createdAt + ", email=" + email + ", foto=" + foto
				+ ", id=" + id + ", nombre=" + nombre + "]";
	}

}
