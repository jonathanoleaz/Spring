package org.example.mockito.ejemplos.services;

import org.example.mockito.ejemplos.models.Examen;
import org.example.mockito.ejemplos.repositories.ExamenRepository;
import org.example.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.example.mockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


class ExamenServiceImplTest {
    //Con la anotacion @Mock se crean como mocks
    @Mock
    ExamenRepository repository;
    @Mock
    PreguntaRepository preguntaRepository;
    //Con la anotacion @InjectMocks se inyectac los mocks
    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp(){
        //Habilitamos el uso de anotaciones de Mockito
        MockitoAnnotations.openMocks(this);
        //Creamos una simulacion al vuelo de ExamenRepository, solo se puede hacer mock de los metodos publicos o default en el mismo package
        //repository = Mockito.mock(ExamenRepositoryImpl.class); //new ExamenRepositoryImpl();
        //preguntaRepository = mock(PreguntaRepository.class);
        //service = new ExamenServiceImpl(repository, preguntaRepository);
    }


    @Test
    void findExamenPorNombre() {
        //Mockeamos o simulamos los repositorios que proporcionan datos a los services
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen= service.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.orElseThrow().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia() {
        //Creamos una simulacion al vuelo de ExamenRepository, solo se puede hacer mock de los metodos publicos o default en el mismo package
        List<Examen> datos = Collections.emptyList();

        //Mockeamos o simulamos los repositorios que proporcionan datos a los services
        when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examen= service.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        //Mockeamos o simulamos los repositorios que proporcionan datos a los services
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Historia");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
    }

    @Test
    void testPreguntasExamenVerify() {
        //Mockeamos o simulamos los repositorios que proporcionan datos a los services
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        //Verificamos que en repository se invoque el metodo findAll
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testPretestNoExisteExamenVerify() {
        //Mockeamos o simulamos los repositorios que proporcionan datos a los services
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertNull(examen);

        //Verificamos que en repository se invoque el metodo findAll
        verify(repository).findAll();
        //verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testGuardarExamen() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }
}