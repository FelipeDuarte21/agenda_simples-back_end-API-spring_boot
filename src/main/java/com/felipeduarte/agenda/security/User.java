package com.felipeduarte.agenda.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.felipeduarte.agenda.model.enums.TipoUsuario;

public class User implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	private Collection<? extends GrantedAuthority> authorities;

	public User(Long id, String nome, String email, String senha,Set<Integer> tipos) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.authorities = tipos.stream().map(
				t -> new SimpleGrantedAuthority(TipoUsuario.toEnum(t).getDescricao()) ).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String nome() {
		return this.nome;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
