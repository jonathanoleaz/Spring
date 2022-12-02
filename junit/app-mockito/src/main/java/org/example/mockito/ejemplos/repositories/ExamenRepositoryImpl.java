package org.example.mockito.ejemplos.repositories;

import org.example.mockito.ejemplos.Datos;
import org.example.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public Examen guardar(Examen examen) {
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        //return Arrays.asList(new Examen(5L, "Matematicas"), new Examen(6L, "Español"), new Examen(7L, "Historia"));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.EXAMENES;
    }
}
