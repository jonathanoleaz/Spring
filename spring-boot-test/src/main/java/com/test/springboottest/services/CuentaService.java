package com.test.springboottest.services;

import com.test.springboottest.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    List<Cuenta> findAll();

    Cuenta save(Cuenta cuenta);
    Cuenta findById(Long id);
    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);

    void deleteById(Long id);
}
