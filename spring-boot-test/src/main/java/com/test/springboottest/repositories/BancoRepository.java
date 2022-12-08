package com.test.springboottest.repositories;

import com.test.springboottest.models.Banco;
import com.test.springboottest.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BancoRepository extends JpaRepository<Banco, Long> {
    //Banco findById(Long id);
    //List<Banco> findAll(Long id);
    //void update(Banco banco);
}
