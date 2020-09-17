package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Factura;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFacturaDao extends CrudRepository<Factura, Long> {

    /*
     * Metodo paraobtener todas las lineas de la factura de una sola vezcon JOIN y
     * no una por una
     */
    @Query("select fact from Factura fact join fetch fact.cliente client join fetch fact.items line join fetch line.producto where fact.id=?1")
    public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);

}
