package com.test.springboottest.services;

import com.test.springboottest.models.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);
}
