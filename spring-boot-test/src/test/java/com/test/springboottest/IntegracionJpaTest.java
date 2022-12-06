package com.test.springboottest;

import com.test.springboottest.models.Cuenta;
import com.test.springboottest.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//Se realiza un rollback al finalizar cada metodo test para que no se afecten entre si las operaciones SQL de cada metodo test.
@DataJpaTest
public class IntegracionJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andres", cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andres");
        assertTrue(cuenta.isPresent());
        assertEquals("Andres", cuenta.orElseThrow().getPersona());
        assertEquals("1000.00", cuenta.orElseThrow().getSaldo().toPlainString());
    }

    @Test
    void testFindByPersonaThrowException(){
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Rodr");
        assertThrows(NoSuchElementException.class, () -> {
            cuenta.orElseThrow();
        });
        assertTrue(!cuenta.isPresent());
    }

    @Test
    void testFindAll(){
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2, cuentas.size());
    }

    @Test
    void testSave(){
        // Given
        Cuenta cuentaJ = new Cuenta(null, "Jojo", new BigDecimal("3000"));
        Cuenta cuenta = cuentaRepository.save(cuentaJ);

        // When
        //Cuenta cuenta = cuentaRepository.findByPersona("Jojo").orElseThrow();
        //Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        // Then
        assertEquals("Jojo", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3, cuenta.getId());
    }

    @Test
    void testUpdate(){
        // Given
        Cuenta cuentaJ = new Cuenta(null, "Jojo", new BigDecimal("3000"));
        Cuenta cuenta = cuentaRepository.save(cuentaJ);

        // When
        //Cuenta cuenta = cuentaRepository.findByPersona("Jojo").orElseThrow();
        //Cuenta cuenta = cuentaRepository.findById(save.getId()).orElseThrow();

        // Then
        assertEquals("Jojo", cuenta.getPersona());
        assertEquals("3000", cuenta.getSaldo().toPlainString());
        //assertEquals(3, cuenta.getId());

        //When
        cuenta.setSaldo(new BigDecimal("3800"));
        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        //Then
        assertEquals("Jojo", cuentaActualizada.getPersona());
        assertEquals("3800", cuentaActualizada.getSaldo().toPlainString());
    }

    @Test
    void testDelete() {
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        System.out.println(cuenta.toString());
        assertEquals("Jhon", cuenta.getPersona());

        cuentaRepository.delete(cuenta);

        assertThrows(NoSuchElementException.class, () ->{
            cuentaRepository.findByPersona("Jhon").orElseThrow();
        });

        assertEquals(1, cuentaRepository.findAll().size());
    }
}
