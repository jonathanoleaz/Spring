package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    public Resource load(String filename) throws MalformedURLException;

    /**
     * Metodo para dejar foto en el servidor y devolver el nombre actualizado del
     * archivo con el UUID
     */
    public String copy(MultipartFile file) throws IOException;

    public boolean delete(String filename);

    public void deleteAll();

    public void init() throws IOException;
}
