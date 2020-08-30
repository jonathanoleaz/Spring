package com.bolsadeindeas.springboot.form.app.editors;

import java.beans.PropertyEditorSupport;

import com.bolsadeindeas.springboot.form.app.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolesEditor extends PropertyEditorSupport{

    @Autowired
    private RoleService service;

    /*El argumento text es un id de rol */
    @Override
    public void setAsText(String text) throws IllegalArgumentException{
        try {
            Integer id = Integer.parseInt(text);
            setValue(service.obtenerPorId(id));
        } catch (NumberFormatException e) {
            setValue(null);
        }
        
    }
    
}