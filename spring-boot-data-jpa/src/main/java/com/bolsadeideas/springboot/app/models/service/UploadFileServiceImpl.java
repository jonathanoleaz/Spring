package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

    @Override
    public Resource load(String filename) throws MalformedURLException {
        Path pathFoto = this.getPath(filename);
        Resource recurso = null;

        recurso = new UrlResource(pathFoto.toUri());
        if (!recurso.exists() && !recurso.isReadable()) {
            throw new RuntimeException("No se puede cargar imagen " + pathFoto.toString());
        }

        return recurso;
    }

    @Override
    public String copy(MultipartFile file) throws IOException {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path rootPath = this.getPath(uniqueFileName);

        // Path absolutePath = rootPath.toAbsolutePath();
        // log.info("rootPath: " + rootPath);
        // log.info("rootAbsolutePath: " + absolutePath);
        /*
         * byte[] bytes = foto.getBytes(); Path
         * rutaCompleta=Paths.get(rootPath+"//"+foto.getOriginalFilename());
         * Files.write(rutaCompleta, bytes);
         */

        Files.copy(file.getInputStream(), rootPath);

        return uniqueFileName;
    }

    @Override
    public boolean delete(String filename) {
        Path rootPath = getPath(filename);
			File archivo = rootPath.toFile();
			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					return true;
				}
			}
        return false;
    }

    public Path getPath(String filename) {
        return Paths.get("uploads").resolve(filename).toAbsolutePath();
    }
}
