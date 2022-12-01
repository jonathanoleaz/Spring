package org.example.mockito.ejemplos.services;

import org.example.mockito.ejemplos.models.Examen;
import org.example.mockito.ejemplos.repositories.ExamenRepository;
import org.example.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ExamenServiceImplTest {

    @Test
    void findExamenPorNombre() {
        //Creamos una simulacion al vuelo de ExamenRepository, solo se puede hacer mock de los metodos publicos o default en el mismo package
        ExamenRepository repository = Mockito.mock(ExamenRepositoryImpl.class); //new ExamenRepositoryImpl();
        ExamenService service = new ExamenServiceImpl(repository);

        List<Examen> datos = Arrays.asList(new Examen(5L, "Matematicas"),
                new Examen(6L, "Español"),
                new Examen(7L, "Historia"));

        when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examen= service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }
}