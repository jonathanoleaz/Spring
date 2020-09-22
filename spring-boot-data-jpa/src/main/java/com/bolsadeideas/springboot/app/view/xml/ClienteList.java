package com.bolsadeideas.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

/*Clase wraper para envolver la lista de clientes y poder pasarlos en XML*/
@XmlRootElement(name="clientes")
public class ClienteList {
    
    @XmlElement(name="cliente")
    public List<Cliente> clientes;

    public ClienteList() {
    }

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    /*public List<Cliente> getClientes() {
        return clientes;
    }*/

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
