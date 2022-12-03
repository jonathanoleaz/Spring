package com.test.springboottest.services;

import com.test.springboottest.models.Banco;
import com.test.springboottest.models.Cuenta;
import com.test.springboottest.repositories.BancoRepository;
import com.test.springboottest.repositories.CuentaRepository;

import java.math.BigDecimal;

public class CuentasServiceImpl implements CuentaService{
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentasServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId);

        return banco.getTotalTransferencia();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId);
        int totalTransferencias = banco.getTotalTransferencia();
        banco.setTotalTransferencia(++totalTransferencias);
        bancoRepository.update(banco);

        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen);
        cuentaOrigen.debito(monto);
        cuentaRepository.update(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino);
        cuentaDestino.credito(monto);
        cuentaRepository.update(cuentaDestino);
    }
}
