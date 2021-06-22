package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Siempre que se trate de un componente de vista, debe especificarse sunombre
 * con el cual se mapeara desde el controller
 */
@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

    @Override
    protected Object filterModel(Map<String, Object> model) {
        model.remove("titulo");
        model.remove("page");

        /*
         * Se obtiene solo la lista para dejarla en el json, y no el objetos Page pues
         * devuelve atributos que no son de utilidad, solo de busca devolver la lista de
         * los clientes
         */
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");
        model.put("clientes", clientes.getContent());

        return super.filterModel(model);
    }

}
