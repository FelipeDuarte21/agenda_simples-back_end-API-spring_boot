package com.felipeduarte.agenda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.UsuarioDTO;
import com.felipeduarte.agenda.model.dtos.UsuarioSalvarDTO;
import com.felipeduarte.agenda.repository.UsuarioRepository;
import com.felipeduarte.agenda.resource.exceptions.AuthorizationException;
import com.felipeduarte.agenda.security.User;
import com.felipeduarte.agenda.security.UserService;
import com.felipeduarte.agenda.service.exceptions.IllegalParameterException;
import com.felipeduarte.agenda.service.exceptions.ObjectNotFoundFromParameterException;

@Service
public class UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	
	private BCryptPasswordEncoder bCrypt;
	
	@Autowired
	public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder bCrypt) {
		this.usuarioRepository = usuarioRepository;
		this.bCrypt = bCrypt;
	}
	
	public UsuarioDTO salvar(UsuarioSalvarDTO usuarioDTO) {
		
		var optUsuario = this.usuarioRepository.findByEmail(usuarioDTO.getEmail());
		
		if(optUsuario.isPresent())
			throw new IllegalParameterException("Erro! usuário já cadastrado!");
		
		var usuario = new Usuario(usuarioDTO);
		
		usuario.setSenha(this.bCrypt.encode(usuario.getSenha()));
		
		usuario = this.usuarioRepository.save(usuario);
		
		return new UsuarioDTO(usuario);
		
	}
	
	public UsuarioDTO alterar(Long id, UsuarioSalvarDTO usuarioDTO) {
		
		var optUsuario = this.usuarioRepository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! usuário não encontrado para o id informado!");
		
		//Verifica permissão para alterar
		UsuarioServicePermissao.verificaPermissaoAlterarUsuario(id);
		
		try {
			
			var optUsuarioEmail = this.usuarioRepository.findByEmail(optUsuario.get().getEmail());
			
			if(optUsuarioEmail.isPresent()) //verifica se o email pertence o usuario
				UsuarioServicePermissao.verificaIdUsuario(optUsuarioEmail.get().getId());
			
		}catch(AuthorizationException ex) {
			throw new IllegalParameterException("Erro! email já cadastrado!");
			
		}
		
		var usuario = optUsuario.get();
		usuario.setNome(usuarioDTO.getNome());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setTipo(usuarioDTO.getTipo());
		
		usuario = this.usuarioRepository.save(usuario);
		
		return new UsuarioDTO(usuario);
		
	}
	
	public void excluir(Long id) {
	
		var optUsuario = this.usuarioRepository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! usuário não encontrado para o id informado!");
		
		//Verifica permissão para excluir
		UsuarioServicePermissao.verificaPermissaoExcluirUsuario(optUsuario.get().getId());
		
		this.usuarioRepository.delete(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorId(Long id) {
		
		var optUsuario = this.usuarioRepository.findById(id);
		
		if(!optUsuario.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! usuário não encontrado para o id informado!");
		
		UsuarioServicePermissao.verificaIdUsuario(optUsuario.get().getId());
		
		return new UsuarioDTO(optUsuario.get());
		
	}
	
	public UsuarioDTO buscarPorEmail(String email) {
		
		var optUsuario = this.usuarioRepository.findByEmail(email);
		
		if(!optUsuario.isPresent()) return new UsuarioDTO();
		
		return new UsuarioDTO(optUsuario.get());
		
	}
	
	public Page<UsuarioDTO> buscarPorNome(String nome, Pageable paginacao){
		
		Page<Usuario> paginaUsuario;
		
		if(!nome.isEmpty()) {
			paginaUsuario = this.usuarioRepository.findByNomeContaining(nome, paginacao);
			
		}else {
			paginaUsuario = this.usuarioRepository.findAll(paginacao);
			
		}
		
		return paginaUsuario.map(UsuarioDTO::new);
		
	}
	
	public Page<UsuarioDTO> buscarTodos(Pageable paginacao){
		
		var paginaUsuario = this.usuarioRepository.findAll(paginacao);
		
		return paginaUsuario.map(UsuarioDTO::new);
		
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