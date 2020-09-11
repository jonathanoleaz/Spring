package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    public Resource load(String filename) throws MalformedURLException;

    /**Metodo para dejar foto en el servidor y devuelv eel nombre actualizado del archivo co el UUID */
    public String copy(MultipartFile file) throws IOException;

    public boolean delete(String filename);
}
