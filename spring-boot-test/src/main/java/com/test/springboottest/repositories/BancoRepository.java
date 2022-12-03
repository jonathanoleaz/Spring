package com.test.springboottest.repositories;

import com.test.springboottest.models.Banco;

import java.util.List;

public interface BancoRepository {
    Banco findById(Long id);
    List<Banco> findAll(Long id);
    void update(Banco banco);
}
