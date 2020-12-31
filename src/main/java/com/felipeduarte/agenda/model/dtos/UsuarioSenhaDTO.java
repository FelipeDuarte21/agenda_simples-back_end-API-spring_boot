package com.felipeduarte.agenda.model.dtos;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UsuarioSenhaDTO {
	
	private Long id;
	
	@NotEmpty(message = "Nome é obrigatório")
	@Size(min = 3, max = 50, message = "Nome deve estar entre 3 a 50 caracteres")
	private String nome;
	
	@NotEmpty(message = "Email é obrigatório")
	@Email(message = "Email inválido!")
	@Size(max = 80, message = "Email deve ter até 80 caracteres")
	private String email;
	
	@NotEmpty(message = "Senha é obrigatório")
	@Size(min = 8, max = 16, message = "Senha deve ter entre 8 a 16 caracteres")
	private String senha;
	
	@NotEmpty(message = "Tipo do usuário é obrigatório")
	private Set<Integer> tipo = new HashSet<>();
	
	public UsuarioSenhaDTO() {
		
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Set<Integer> getTipo() {
		return tipo;
	}

	public void setTipo(Set<Integer> tipo) {
		this.tipo = tipo;
	}
	
}
