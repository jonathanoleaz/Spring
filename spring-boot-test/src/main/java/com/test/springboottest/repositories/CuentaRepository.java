package com.test.springboottest.repositories;

import com.test.springboottest.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();
    Cuenta findById(Long id);

    void update(Cuenta cuenta);

}
