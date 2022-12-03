package com.test.springboottest.services;

import com.test.springboottest.models.Banco;
import com.test.springboottest.models.Cuenta;

import java.math.BigDecimal;

public class Datos {
    public static final Cuenta CUENTA_001 = new Cuenta(1L, "Andres", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L, "Jhon", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(1L, "Financiero", 0);
}
