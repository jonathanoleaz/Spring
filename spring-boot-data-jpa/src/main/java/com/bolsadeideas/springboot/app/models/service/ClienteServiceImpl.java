package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*Se agrega Service para seguir el patron Fachade (Facade) para poder tener en esta capa */
@Service
public class ClienteServiceImpl implements IClienteService {
    
    @Autowired
    private IClienteDao clienteDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {        
        return (List<Cliente>) clienteDao.findAll();
    }
    
    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        /*Como ahora se usa crudRepository y no el dao implementado por nosotros, se devuelve un optional, es decir, 
        un objeto que envuelve el objeto devuelto Cliente. Por ello, se obtiene el Cliente con .get()*/
        return clienteDao.findById(id).get();
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
        
    }
    
}
