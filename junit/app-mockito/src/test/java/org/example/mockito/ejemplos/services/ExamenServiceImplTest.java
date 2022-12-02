package org.example.mockito.ejemplos.services;

import org.example.mockito.ejemplos.Datos;
import org.example.mockito.ejemplos.models.Examen;
import org.example.mockito.ejemplos.repositories.ExamenRepository;
import org.example.mockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.example.mockito.ejemplos.repositories.PreguntaRepository;
import org.example.mockito.ejemplos.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
    ExamenRepositoryImpl repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;
    //Con la anotacion @InjectMocks se inyectac los mocks
    @InjectMocks
    ExamenServiceImpl service;

    //Lo siguiente es equivalente a ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    @Captor
    ArgumentCaptor<Long> captor2;

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

    @Test
    void testManejoExcepcion(){
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombreConPreguntas("Matematicas");
        });
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //Comprobacion de argumentos
        verify(preguntaRepository).findPreguntasPorExamenId(ArgumentMatchers.argThat(arg -> arg.equals(5L)));
        //Comprobacion de argumentos equivalente a ArgumentMatchers.argThat(arg -> arg.equals(5L))
        verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //Comprobacion de argumentos
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //Comprobacion de argumentos
        verify(preguntaRepository).findPreguntasPorExamenId(argThat((argument) -> argument != null && argument != 0));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long>{
        private Long argument;

        @Override
        public boolean matches(Long argument){
            this.argument = argument;
            return argument != null && argument != 0;
        }

        @Override
        public String toString(){
            return "clase para mensaje personalizado de error qie se imprime al fallar el test: Debe ser entero positivo "+argument;
        }
    }

    @Test
    void testArgumentCaptor(){
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombreConPreguntas("Matematicas");

        //captor se declara de forma "manual"
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

        //captor2 se declara a traves de anotaciones
        verify(preguntaRepository).findPreguntasPorExamenId(captor2.capture());

        assertEquals(-5L, captor.getValue());
    }

    @Test
    void testDoThrow(){
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        //Do throw es util para lanzar excepciones en los mock con metodos void
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer(){

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //Ejemplo sobre como devolver una respuesta mas personalizada, en este caso, una respuesta con condicion
        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id==5L? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testDoAnswerGuardarExamen() {
        // Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        /*when(repository.guardar(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });*/

        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardar(any(Examen.class));

        //When
        Examen examen = service.guardar(newExamen);

        // Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());
        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //Con el metodo doCallRealMethod se pueden invocar metodos reales en vez de metodos mockeados, pero debe ejecutarse sobre una clase concreta o implementacion, no sobre una interfaz
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void testSpy() {
        //el mock es 100% simulado, todos sus metodos son mockeados
        //con spy, solo los metodos especificados son mockeados, la invocación de los demas metodos es real,
        // por lo tanto al instanciar un spy, debemos hacerlo con clases concretas o implementaciones, no con interfaces
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("aritmetica");
        //Cuando trabajamos con Spy, no se sugiere usar when porque when invoca al metodo real, se sugiere el do
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testOrdenDeInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Español");

        InOrder inOrder = inOrder(preguntaRepository);
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);
    }

    @Test
    void testOrdenDeInvocaciones2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenPorNombreConPreguntas("Matematicas");
        service.findExamenPorNombreConPreguntas("Español");

        InOrder inOrder = inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findAll();

        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(5L);

        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamenId(6L);
    }

    @Test
    void numeroDeInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");

        verify(preguntaRepository).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, times(1 )).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeast(1 )).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atMost(1)).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }

    /*
    @Test
    void numeroDeInvocaciones2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombreConPreguntas("Matematicas");

        //verify(preguntaRepository).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, times(2 )).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeast(2 )).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(5L);
        verify(preguntaRepository, atMost(2)).findPreguntasPorExamenId(5L);
        //verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(5L);
    }*/
}