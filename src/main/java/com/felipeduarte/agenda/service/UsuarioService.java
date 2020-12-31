package com.felipeduarte.agenda.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.UsuarioDTO;
import com.felipeduarte.agenda.model.enums.TipoUsuario;
import com.felipeduarte.agenda.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;

	
	public Usuario salvar(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = Usuario.converteUsuarioDTOParaUsuario(usuarioDTO);
		
		Optional<Usuario> usu = this.usuarioRepository.findByEmail(usuario.getEmail());
		
		if(usu.isPresent()) {
			usuario.setEmail(null);
			return usuario;
		}
		
		usuario.setSenha(this.bCrypt.encode(usuario.getSenha()));
		
		Set<Integer> tipos = usuario.getTipo();
		for(Integer tipo: tipos) {
			if(tipo == TipoUsuario.ADMIN.getCodigo()) 
				usuario.getTipo().add(TipoUsuario.USUARIO.getCodigo());
		}
		
		usuario = this.usuarioRepository.save(usuario);
		
		return usuario;
	}
	
	public Usuario alterar(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = Usuario.converteUsuarioDTOParaUsuario(usuarioDTO);
		
		if(usuario.getId() == null) {
			usuario.setId(null);
			return usuario;
		}
		
		Optional<Usuario> usu = this.usuarioRepository.findById(usuario.getId());
		
		if(usu.isEmpty()) {
			usuario.setNome(null);
			return usuario;
		}
		
		usuario.setTipo(usu.get().getTipo());
		
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
	
	public Usuario buscarPorEmail(String email) {
		
		Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);
		
		if(usuario.isEmpty()) return null;
		
		return usuario.get();
	}
	
	public Page<Usuario> buscarTodos(Integer pagina, Integer qtdPorPagina){
		
		PageRequest page = PageRequest.of(pagina, qtdPorPagina,Sort.by(Direction.ASC, "nome"));
		
		Page<Usuario> paginaUsuario = this.usuarioRepository.findAll(page);
		
		return paginaUsuario;
	}
	
}
