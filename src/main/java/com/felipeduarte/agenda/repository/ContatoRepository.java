package com.felipeduarte.agenda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeduarte.agenda.model.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato,Long>{
	
	public Page<Contato> findByNomeContaining(String nome,Pageable page);
	
}
