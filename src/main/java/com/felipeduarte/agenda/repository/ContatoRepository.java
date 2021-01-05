package com.felipeduarte.agenda.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeduarte.agenda.model.Contato;
import com.felipeduarte.agenda.model.Usuario;

@Repository
public interface ContatoRepository extends JpaRepository<Contato,Long>{
	
	public Optional<Contato> findByTelefoneAndCelularAndEmailAndUsuario(String telefone,String celular,
			String email, Usuario usuario);
	
	public Page<Contato> findByUsuarioAndNomeContaining(Usuario usuario, String nome,Pageable pagina);
	
	public Page<Contato> findByUsuario(Usuario usuario, Pageable pagina);
	
}
