package com.felipeduarte.agenda.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.UsuarioDTO;
import com.felipeduarte.agenda.repository.UsuarioRepository;
import com.felipeduarte.agenda.resource.exceptions.AuthorizationException;
import com.felipeduarte.agenda.security.User;
import com.felipeduarte.agenda.security.UserService;

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
		
		usuario = this.usuarioRepository.save(usuario);
		
		return usuario;
	}
	
	public Usuario alterar(UsuarioDTO usuarioDTO) {
		
		Usuario usuario = Usuario.converteUsuarioDTOParaUsuario(usuarioDTO);
		
		if(usuario.getId() == null) {
			usuario.setId(null);
			return usuario;
		}
		
		UsuarioServicePermissao.verificaPermissaoAlterarUsuario(usuario.getId());
		
		Optional<Usuario> usu = this.usuarioRepository.findById(usuario.getId());
		
		if(usu.isEmpty()) {
			usuario.setNome(null);
			return usuario;
		}
		
		usuario.setSenha(this.bCrypt.encode(usuario.getSenha()));
		
		usuario.setTipo(usu.get().getTipo());
		
		usuario = this.usuarioRepository.save(usuario);
		
		return usuario;
	}
	
	public boolean excluir(Long id) {
	
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		
		if(usuario.isEmpty()) return false;
		
		UsuarioServicePermissao.verificaPermissaoExcluirUsuario(usuario.get().getId());
		
		//Fazer a ContatoService excluir os contatos do usuario se for do tipo USER
		
		this.usuarioRepository.deleteById(id);
		
		return true;
	}
	
	public Usuario buscarPorId(Long id, boolean verificaPermissao) {
		
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		
		if(usuario.isEmpty()) return null;
		
		if(verificaPermissao) UsuarioServicePermissao.verificaIdUsuario(usuario.get().getId());
		
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
	
	private static class UsuarioServicePermissao{
		
		public static void verificaPermissaoAlterarUsuario(Long id){
			
			User user = UserService.getUser();
			
			if(user == null || !user.getId().equals(id))
				throw new AuthorizationException("Usuario não tem permissão para "
						+ "alterar dados dos outros usuarios");
		}
		
		public static void verificaPermissaoExcluirUsuario(Long id) {
			
			User user = UserService.getUser();
			
			if(user == null || (user.hasRole("ROLE_USER") && !user.getId().equals(id)) ) {
				throw new AuthorizationException("Usuário não tem permissão para" 
					+ "excluir outros usuários");
			}
		
		}
		
		public static void verificaIdUsuario(Long id) {
			
			User user = UserService.getUser();
			
			if(user == null) {
				throw new AuthorizationException("Usuario não logado!");
			}
			
			if(!user.getId().equals(id)) {
				throw new AuthorizationException("Este id não pertence ao usuario logado!");
			}
			
		}
		
		
	}
	
	
}