package com.bolsadeideas.springboot.app.models.dao;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long>{

	@Query("select cli from Cliente cli left join fetch cli.facturas fac where cli.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);

	/**Implementacion de funcipon para obtener todas las facturas del cliente, y no una por una de forma peresoza */
	/* ya no usamos el dao implementado a mano, sino la interfaz CrudRepository
	public List<Cliente> findAll();

	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);

	public void delete(Long id);
	*/
}
