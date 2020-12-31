package com.felipeduarte.agenda.model.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ContatoDTO {

	private Long id;
	
	@NotEmpty(message = "Nome é obrigatório")
	@Size(min = 3, max = 50, message = "Nome deve estar entre 3 a 50 caracteres")
	private String nome;
	
	@NotEmpty(message = "Telefone é obrigatório")
	@Size(min = 10, max = 10, message = "Telefone são necessariamente 10 caracteres")
	private String telefone;
	
	@NotEmpty(message = "Celular é obrigatório")
	@Size(min = 11, max = 11, message = "Celular são necessariamente 11 caracteres")
	private String celular;
	
	@NotEmpty(message = "Email é obrigatório")
	@Email(message = "Email inválido!")
	@Size(max = 80, message = "Email deve ter até 80 caracteres")
	private String email;

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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
