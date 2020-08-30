package com.bolsadeindeas.springboot.form.app.services;

import java.util.ArrayList;
import java.util.List;
import com.bolsadeindeas.springboot.form.app.models.domain.Rol;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{
    public List<Rol>listar(){
        return roles;
    }
    public Rol obtenerPorId(Integer id){
        Rol resultado=null;
        for(Rol role: roles){
            if(id==role.getId()){
                resultado=role; 
                break;
            }
        }
        return resultado;
    }

    private List<Rol> roles;
    public RoleServiceImpl() {
        this.roles=new ArrayList<>();
        this.roles.add(new Rol(1, "Administrador", "ROLE_ADMIN"));
        this.roles.add(new Rol(2, "Usuario", "ROLE_USER"));
        this.roles.add(new Rol(3, "Moderador", "ROLE_MODERATOR"));
    }

    
}