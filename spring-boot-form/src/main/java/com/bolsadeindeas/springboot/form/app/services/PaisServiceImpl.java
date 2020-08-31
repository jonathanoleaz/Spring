package com.bolsadeindeas.springboot.form.app.services;

import java.util.Arrays;
import java.util.List;

import com.bolsadeindeas.springboot.form.app.models.domain.Pais;

import org.springframework.stereotype.Service;

/*Lo anotamos y registramos como un componente de Spring para poder inyectarlo */
@Service
public class PaisServiceImpl implements PaisService {

	private List<Pais> lista;

	public PaisServiceImpl() {

		this.lista = Arrays.asList(new Pais(1, "MX", "México"), new Pais(2, "CH", "Chile"),
				new Pais(3, "CO", "Colombia"), new Pais(4, "CA", "Canada"));

	}

	@Override
	public List<Pais> listar() {
		return lista;
	}

	@Override
	public Pais obtenerPorId(Integer id) {
		Pais resultado = null;
		for (Pais pais : this.lista) {
			if (id == pais.getId()) {
				resultado = pais;
				break;
			}
		}
		return resultado;
	}

}