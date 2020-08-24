package com.bolsadeindeas.springboot.form.app.editors;

import org.springframework.beans.propertyeditors.PropertiesEditor;

public class NombreMayusculasEditor extends PropertiesEditor{

    @Override
    public void setAsText(String text) throws IllegalArgumentException{

        setValue(text.toUpperCase().trim());
    }
    
}