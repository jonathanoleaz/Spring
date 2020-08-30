package com.bolsadeindeas.springboot.form.app.models.domain;

public class Rol {
    private Integer id;
    private String nombre;
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Rol(Integer id, String nombre, String role) {
        this.id = id;
        this.nombre = nombre;
        this.role = role;
    }

    public Rol() {
    }

    /**Para poder comparar entre roles y que thymeleaf pueda marcar como predefinido el rol */
    @Override
    public boolean equals(Object obj){

        if(this==obj){
            return true;
        }

        if(!(obj instanceof Rol)){
            return false;
        }
        Rol role=(Rol) obj;

        return this.id !=null && this.id.equals(role.getId());
    }
    
}