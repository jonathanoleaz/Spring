package org.example.mockito.ejemplos.repositories;

import java.util.List;

public interface PreguntaRepository {
    List<String> findPreguntasPorExamenId(Long id);
    void guardarVarias(List<String> preguntas);
}
