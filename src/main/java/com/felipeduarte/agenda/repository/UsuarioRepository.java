package com.felipeduarte.agenda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeduarte.agenda.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
	
	public Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
}
