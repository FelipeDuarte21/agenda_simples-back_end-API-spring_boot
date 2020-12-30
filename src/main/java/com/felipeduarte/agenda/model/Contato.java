package com.felipeduarte.agenda.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.felipeduarte.agenda.model.dtos.ContatoDTO;

@Entity
public class Contato implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50)
	@NotEmpty(message = "Nome é obrigatório")
	@Size(min = 3, max = 50, message = "Nome deve estar entre 3 a 50 caracteres")
	private String nome;
	
	@Column(length = 10)
	@NotEmpty(message = "Telefone é obrigatório")
	@Size(min = 10, max = 10, message = "Telefone são necessariamente 10 caracteres")
	private String telefone;
	
	@Column(length = 11)
	@NotEmpty(message = "Celular é obrigatório")
	@Size(min = 11, max = 11, message = "Celular são necessariamente 10 caracteres")
	private String celular;
	
	@Column(length = 80)
	@NotEmpty(message = "Email é obrigatório")
	@Email(message = "Email inválido!")
	@Size(max = 80, message = "Email deve ter até 80 caracteres")
	private String email;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	public Contato() {
		
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public static Contato converteContatoDTOParaContato(ContatoDTO contato, Usuario usuario) {
		Contato c = new Contato();
		c.setId(contato.getId()); 
		c.setNome(contato.getNome());
		c.setTelefone(contato.getTelefone());
		c.setCelular(contato.getCelular());
		c.setEmail(contato.getEmail());
		c.setUsuario(usuario);
		
		return c;
	}

}
