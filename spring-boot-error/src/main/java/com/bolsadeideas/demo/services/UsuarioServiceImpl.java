package com.bolsadeideas.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bolsadeideas.demo.models.domain.Usuario;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private List<Usuario> lista;
	
	
	public UsuarioServiceImpl() {
		this.lista=new ArrayList<>();
		this.lista.add(new Usuario(1, "Humberto", "Loera"));
		this.lista.add(new Usuario(2, "Aldo", "Flores"));
		this.lista.add(new Usuario(3, "Petronilo", "Sevilla"));
	}

	@Override
	public List<Usuario> listar() {
		return this.lista;
	}

	@Override
	public Usuario obtenerPorId(Integer id) {
		Usuario resultado=null;
		for(Usuario us: this.lista) {
			if(us.getId().equals(id)) {
				resultado=us;
				break;
			}
		}
		return resultado;
	}
	
	/*Con Optional, se retorna el usuario encontrado, sino se encontró devuelve null*/
	@Override
	public Optional<Usuario> obtenerPorIdOptional(Integer id) {
		Usuario usuario=this.obtenerPorId(id);
		return Optional.ofNullable(usuario);
	}

}
