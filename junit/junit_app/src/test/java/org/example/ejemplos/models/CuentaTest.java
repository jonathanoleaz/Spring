package org.example.ejemplos.models;

import org.example.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(){

        //instanciar los objetos de esta forma (en un metodo BeforeEach es mala practica porque podria tenderse a poner estado de este objeto en este metodo,
        // y se acoplarian los metodos
        this.cuenta = new Cuenta("Jonathan", new BigDecimal("3.14159"));
        System.out.println("metodo inicial");
    }

    @AfterEach
    void afterMetodoTest(){
        System.out.println("Finalizando el metodo de prueba");
    }

    //Este metodo debe ser estatico porque se ejecuta en el momento en que aun no se ha creado la instancia de CuentaTest
    @BeforeAll
    static void beforeAll(){
        System.out.println("Iniciando test");
    }

    //Este metodo debe ser estatico porque se ejecuta en el momento en que aun no se ha creado la instancia de CuentaTest
    @AfterAll
    static void afterAll(){
        System.out.println("Terminando test");
    }

    @Nested
    class CuentaTestAtributosCuenta{
        @Test
        @DisplayName("Probando el nombre de la cuenta corriente")
        void testNombreCuenta() {
            cuenta = new Cuenta();
            cuenta.setPersona("Jonathan");
            String esperado = "Jonathan";
            String real = cuenta.getPersona();
            //Con el siguiente ejemplo, SI se instancia la cadena del mensaje de error aunque no suceda el error
            assertEquals(esperado, real, "El nombre de la cuenta no es el que se esperaba");
            //Con el siguiente ejemplo, NO se instancia la cadena del mensaje de error si no sucede el error
            assertEquals(esperado, real, () -> "El nombre de la cuenta no es el que se esperaba");
            assertTrue(real.equals("Jonathan"), "El nombre de la cuenta esperada debe ser igual a la real");
        }

        @Test
        void testSaldoCuenta() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            //otra opcion de forma programatica en vez de usar anotaciones condicionales
            assumeTrue(esDev);
            assertEquals(3.14159, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        }

        @Test
        void testSaldoCuenta2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            //otra opcion de forma programatica en vez de usar anotaciones condicionales, para un bloque de codigo,
            // con esta forma, el metodo si se considera en el reporte
            assumingThat(esDev, () -> {
                assertEquals(3.14159, cuenta.getSaldo().doubleValue());
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            });

        }

        @Test
        void testReferenciaCuenta() {
            cuenta = new Cuenta("Juan", new BigDecimal("6.021023"));
            Cuenta cuenta2 = new Cuenta("Juan Z", new BigDecimal("6.021023"));
            assertNotEquals(cuenta, cuenta2);
            //assertEquals(cuenta, cuenta2);
        }



    }

    @Nested
    class CuentaOperacionesTest{
        @Test
        void testTransferirDineroCuenta() {
            Cuenta cuenta1 = new Cuenta("Jonathan", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Apstref", new BigDecimal("1500"));
            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }

        @Test
        void testDebitoCuenta() {
            cuenta = new Cuenta("Andres", new BigDecimal("1000.123"));
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.123", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCuenta() {
            cuenta = new Cuenta("Andres", new BigDecimal("1000.123"));
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.123", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testDineroInsuficienteExcepcionCuenta() {
            cuenta = new Cuenta("Andres", new BigDecimal("1000.123"));
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(2000));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero insuficiente";
            assertEquals(esperado, actual);
        }
    }




    @Test
    @Disabled
    void testRelacionBancoCuentas() {
        //Metodo que asegura se marque error
        fail();
        Cuenta cuenta1 = new Cuenta("Jonathan", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Apstref", new BigDecimal("1500"));
        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        //Metodo para realizar pruebas sobre todos los assert, si varios assert fallan, junit reportara las fallas de todos los que fallen,
        // a diferencia de ponerlos secuencialmente, en donde reporta solo el primero que falle
        assertAll(
                () ->
                    assertEquals("1000", cuenta2.getSaldo().toPlainString())
                ,
                () ->
                    assertEquals("3000", cuenta1.getSaldo().toPlainString())
                ,
                () ->
                    assertEquals(2, banco.getCuentas().size())
                ,
                () ->
                    assertEquals("Banco del Estado", cuenta1.getBanco().getNombre())
                ,
                () ->
                    assertEquals("Apstref", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Apstref"))
                            .findFirst()
                            .get().getPersona())
                ,
                () ->
                    assertTrue(banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Apstref"))
                            .findFirst()
                            .isPresent())
                ,
                () ->
                    assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Apstref")))

        );
    }

    @Nested
    class SistemaOperativoTest{
        @Test
        @EnabledOnOs({OS.WINDOWS, OS.MAC})
        void testSoloWindowsMac(){

        }

        @Test
        @DisabledOnOs({OS.WINDOWS})
        void testNoWindows(){

        }
    }

    @Nested
    class JavaVersionTest{
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8(){

        }

        @Test
        @EnabledOnJre(JRE.JAVA_15)
        void soloJDK15(){

        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*15.*")
        void testJavaVersion(){

        }
    }

    @Nested
    class VariableAmbiente{
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDevEnv(){

        }
    }

    @RepeatedTest(value=5, name="Repeticion numero {currentRepetition} de {totalRepetition}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition()==3){
            System.out.println("Tercera iteracion de prueba");
        }
        cuenta = new Cuenta("Andres", new BigDecimal("1000.123"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.123", cuenta.getSaldo().toPlainString());
    }


    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "500", "700"})
    void testDebitoCuenta(String monto) {
        cuenta = new Cuenta("Andres", new BigDecimal("1000.123"));
        BigDecimal saldoOriginal = cuenta.getSaldo();
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());

        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        assertEquals(saldoOriginal.subtract(new BigDecimal(monto)), cuenta.getSaldo());
    }

}