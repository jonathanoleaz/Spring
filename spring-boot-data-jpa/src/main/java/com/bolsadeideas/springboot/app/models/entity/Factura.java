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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String descripcion;

    @NotEmpty
    private String observacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    /* Muchas facturas a un cliente */
    /**
     * Con lazy, los objetos se traen de forma que se van requiriendo, no se cargan
     * los objetos desde el principio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    /** Una factura a muchos item */
    /**
     * En este caso, a diferencia de la relacion entre facturas y clientes, si
     * usamos la anotacion JoinColumn pues debemos especificar el atributo que ira
     * en itemFactura
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    /* Definimos la fecha de creacion justo ants de */
    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /*
     * Anotacion para evitar que al generar xml, se genere ciclo infinito, del tipo:
     * un cliente tiene uan factura y una factura tiene un cliente... pues la
     * relacion es bidireccional.
     * 
     * @JsonBackReference: atributo trasero de la referencia, se omitira de la serializacion a json
     */
    @XmlTransient()
    @JsonBackReference
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFactura item) {
        this.items.add(item);
    }

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    public Double getTotal() {
        Double total = 0.0;

        int size = items.size();
        for (int i = 0; i < size; i++) {
            total += items.get(i).calcularImporte();
        }

        return total;
    }

    @Override
    public String toString() {
        return "Factura [cliente=" + cliente + ", createdAt=" + createdAt + ", descripcion=" + descripcion + ", id="
                + id + ", items=" + items + ", observacion=" + observacion + "]";
    }
}
