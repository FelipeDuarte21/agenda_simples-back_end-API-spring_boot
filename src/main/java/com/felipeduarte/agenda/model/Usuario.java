package com.felipeduarte.agenda.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.felipeduarte.agenda.model.dtos.UsuarioSalvarDTO;

@Entity
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50)
	private String nome;
	
	@Column(length = 80, unique = true)
	private String email;
	
	private String senha;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tipo_usuario")
	private Set<Integer> tipo = new HashSet<>();
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Contato> contatos = new ArrayList<>();
	
	public Usuario() {
		
	}
	
	public Usuario(UsuarioSalvarDTO usuarioDTO) {
		this.nome = usuarioDTO.getNome();
		this.email = usuarioDTO.getEmail();
		this.senha = usuarioDTO.getSenha();
		this.tipo = usuarioDTO.getTipo();
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

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
