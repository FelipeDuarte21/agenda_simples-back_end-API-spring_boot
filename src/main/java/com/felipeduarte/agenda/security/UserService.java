package com.felipeduarte.agenda.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.service.UsuarioService;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = this.usuarioService.buscarPorEmail(email);
		
		if(usuario == null) throw new UsernameNotFoundException(email);
		
		return new User(usuario.getId(),usuario.getNome(),usuario.getEmail(),
				usuario.getSenha(),usuario.getTipo());
	}
	
	public User loadUserById(Long id) {
		
		Usuario usuario = this.usuarioService.buscarPorId(id);
		
		if(usuario == null) return null;
		
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
