package com.felipeduarte.agenda.model.dtos;

import java.util.HashSet;
import java.util.Set;

import com.felipeduarte.agenda.model.Usuario;

import io.swagger.annotations.ApiModelProperty;

public class UsuarioDTO {
	
	@ApiModelProperty(value = "Identificação do usuario")
	private Long id;
	
	@ApiModelProperty(value = "Nome do usuario")
	private String nome;
	
	@ApiModelProperty(value = "Email do usuario")
	private String email;
	
	@ApiModelProperty(value = "Perfis do usuário")
	private Set<Integer> tipo = new HashSet<>();
	
	public UsuarioDTO() {
		
	}
	
	public UsuarioDTO(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
		this.email = usuario.getEmail();
		this.tipo = usuario.getTipo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Integer> getTipo() {
		return tipo;
	}

	public void setTipo(Set<Integer> tipo) {
		this.tipo = tipo;
	}

}
