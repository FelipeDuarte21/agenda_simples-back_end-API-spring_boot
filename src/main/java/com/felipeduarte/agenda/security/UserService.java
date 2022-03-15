package com.felipeduarte.agenda.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.repository.UsuarioRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		var optUsuario = this.usuarioRepository.findByEmail(email);
		
		if(optUsuario.isEmpty()) throw new UsernameNotFoundException(email);
		
		var usuario = optUsuario.get();
		
		return new User(usuario.getId(),usuario.getNome(),usuario.getEmail(),
				usuario.getSenha(),usuario.getTipo());
	}
	
	public User loadUserById(Long id) {
		
		var optUsuario = this.usuarioRepository.findById(id);
		
		if(optUsuario.isEmpty()) return null;
		
		var usuario = optUsuario.get();
		
		User user = new User(usuario.getId(),usuario.getNome(),usuario.getEmail(),
				usuario.getSenha(),usuario.getTipo());
		
		return user;
	}
	
	public static User getUser() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		}catch(Exception e) {
			return null;
		}
	}

}
