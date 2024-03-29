package com.felipeduarte.agenda.model.dtos;

import com.felipeduarte.agenda.model.Contato;

import io.swagger.annotations.ApiModelProperty;

public class ContatoDTO {
	
	@ApiModelProperty(value = "identificação do contato")
	private Long id;
	
	@ApiModelProperty(value = "nome do contato")
	private String nome;
	
	@ApiModelProperty(value = "telefone do contato")
	private String telefone;
	
	@ApiModelProperty(value = "celular do contato")
	private String celular;
	
	@ApiModelProperty(value = "email do contato")
	private String email;
	
	public ContatoDTO() {
		
	}
	
	public ContatoDTO(Contato contato) {
		this.id = contato.getId();
		this.nome = contato.getNome();
		this.telefone = contato.getTelefone();
		this.celular = contato.getCelular();
		this.email = contato.getEmail();
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
