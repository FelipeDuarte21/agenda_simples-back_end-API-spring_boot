package com.felipeduarte.agenda.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.enums.TipoUsuario;
import com.felipeduarte.agenda.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	
	public Usuario salvar(Usuario usuario) {
		
		//Encriptar a senha aqui
		
		Set<Integer> tipos = usuario.getTipo();
		for(Integer tipo: tipos) {
			if(tipo == TipoUsuario.ADMIN.getCodigo()) 
				usuario.getTipo().add(TipoUsuario.USUARIO.getCodigo());
		}
		
		usuario = this.usuarioRepository.save(usuario);
		
		return usuario;
	}
	
	public Usuario alterar(Usuario usuario) {
		
		if(usuario.getId() == null) return null;
		
		usuario = this.usuarioRepository.save(usuario);
		
		return usuario;
	}
	
	public boolean excluir(Long id) {
	
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		
		if(usuario.isEmpty()) return false;
		
		this.usuarioRepository.deleteById(id);
		
		return true;
	}
	
	public Usuario buscarPorId(Long id) {
		
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		
		if(usuario.isEmpty()) return null;
		
		return usuario.get();
		
	}
	
	public Usuario buscarPorEmailESenha(String email,String senha) {
		
		Optional<Usuario> usuario = this.usuarioRepository.findByEmailAndSenha(email, senha);
		
		if(usuario.isEmpty()) return null;
		
		return usuario.get();
	}
	
	public Page<Usuario> buscarTodos(Integer pagina, Integer qtdPorPagina){
		
		PageRequest page = PageRequest.of(pagina, qtdPorPagina,Sort.by(Direction.ASC, "nome"));
		
		Page<Usuario> paginaUsuario = this.usuarioRepository.findAll(page);
		
		return paginaUsuario;
	}
	
}
