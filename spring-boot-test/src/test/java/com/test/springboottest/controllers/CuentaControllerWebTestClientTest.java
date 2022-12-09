package com.test.springboottest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springboottest.models.Cuenta;
import com.test.springboottest.models.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

//Esta prueba se considera de integracion, por lo que los metodos pueden definirse con un orden arbitrario
// en caso de que algunos metodos de prueba modifiquen datos utilizados por otros metodos
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentaControllerWebTestClientTest {
    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException {
        //Given
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigenId(1L);
        transaccionDto.setCuentaDestinoId(2L);
        transaccionDto.setBancoId(1L);
        transaccionDto.setMonto(new BigDecimal("100"));


        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia exitosa");
        response.put("transaccion", transaccionDto);
        //When
        client.post().uri("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(respuesta -> {
                    try {
                        JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                        assertEquals("Transferencia exitosa", json.path("mensaje").asText());
                        assertEquals(1L, json.path("transaccion").path("cuentaOrigenId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaccion").path("monto").asText());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .jsonPath("$.mensaje").isNotEmpty()
                .jsonPath("$.mensaje").value(is("Transferencia exitosa"))
                .jsonPath("$.mensaje").value(valor -> {
                    assertEquals("Transferencia exitosa", valor);
                })
                .jsonPath("$.mensaje").isEqualTo("Transferencia exitosa")
                .jsonPath("$.transaccion.cuentaOrigenId").isEqualTo(transaccionDto.getCuentaOrigenId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(2)
    void testDetalle() throws JsonProcessingException {
        Cuenta cuenta = new Cuenta(1L, "Andres", new BigDecimal("900"));

        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Andres")
                .jsonPath("$.saldo").isEqualTo(900)
                .json(objectMapper.writeValueAsString(cuenta));
    }

    @Test
    @Order(3)
    void testDetalle2() {
        client.get().uri("/api/cuentas/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response ->{
                    Cuenta cuenta = response.getResponseBody();
                    assertEquals("Jhon", cuenta.getPersona());
                    assertEquals("2100.00", cuenta.getSaldo().toPlainString());
                });
    }

    @Test
    @Order(4)
    void testListar(){
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("Andres")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].saldo").isEqualTo(900)
                .jsonPath("$[1].persona").isEqualTo("Jhon")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].saldo").isEqualTo(2100)
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }

    @Test
    @Order(5)
    void testListar2(){
        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectBodyList(Cuenta.class)
                .consumeWith(response -> {
                    List<Cuenta> cuentas = response.getResponseBody();
                    assertEquals(2, cuentas.size());
                    assertEquals("Andres", cuentas.get(0).getPersona());
                    assertEquals(1L, cuentas.get(0).getId());
                    assertEquals("900.0", cuentas.get(0).getSaldo().toPlainString());
                    assertEquals("Jhon", cuentas.get(1).getPersona());
                    assertEquals(2L, cuentas.get(1).getId());
                    assertEquals("2100.0", cuentas.get(1).getSaldo().toPlainString());
                });
    }

    @Test
    @Order(6)
    void testGuardar() {
        //Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));

        //When
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.persona").isEqualTo("Pepe")
                .jsonPath("$.saldo").isEqualTo(3000);
    }

    @Test
    @Order(7)
    void testGuardar2() {
        //Given
        Cuenta cuenta = new Cuenta(null, "Apep", new BigDecimal("3500"));

        //When
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                //Then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Cuenta.class)
                .consumeWith(response -> {
                    Cuenta c = response.getResponseBody();
                    assertNotNull(c);
                    assertEquals(4L, c.getId());
                    assertEquals("3500", c.getSaldo().toPlainString());

                });
    }

    @Test
    @Order(8)
    void testEliminar() {
        client.get().uri("/api/cuentas").exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectBodyList(Cuenta.class)
                                .hasSize(4);
        client.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/cuentas").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)
                .hasSize(3);

        client.get().uri("/api/cuentas/3").exchange()
                //.expectStatus().is5xxServerError();
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}