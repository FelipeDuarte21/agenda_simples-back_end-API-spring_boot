package com.felipeduarte.agenda.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Contato;
import com.felipeduarte.agenda.repository.ContatoRepository;

@Service
public class ContatoService {
	
	@Autowired
	private ContatoRepository repository;
	
	public Contato salvar(Contato contato) {
		
		contato = this.repository.save(contato);
		
		if(contato != null) {
			return contato;
		}
		
		return null;
		
	}
	
	public Contato alterar(Contato contato) {
		
		Optional<Contato> c = this.repository.findById(contato.getId());
		
		if(c.isPresent()) {
		
			contato = this.repository.save(contato);
		
			if(contato != null) {
				return contato;
			}
			
		}
		
		return null;
	}
	
	public boolean excluir(Long id) {
		
		Optional<Contato> contato = this.repository.findById(id);
		
		if(contato.isPresent()) {
			
			this.repository.delete(contato.get());
			
			return true;
			
		}
		
		return false;
	}
	
	public Page<Contato> buscarPorNome(String nome,Integer page, Integer size){
		
		PageRequest pg = PageRequest.of(page, size,Direction.ASC,"nome");
		
		Page<Contato> contatos;
		
		if(!nome.isEmpty()) {
			contatos = this.repository.findByNomeContaining(nome, pg);
		}else {
			contatos = this.buscarTodos(page, size);
		}
			
		if(contatos != null) {
			return contatos;
		}
		
		return null;
	}
	
	public Contato buscarPorId(Long id){
		
		Optional<Contato> contato = this.repository.findById(id);
		
		if(contato.isPresent()) {
			return contato.get();
		}
		
		return null;
	}
	
	public Page<Contato> buscarTodos(Integer page,Integer size){
		
		PageRequest pg = PageRequest.of(page, size,Direction.ASC,"nome");
		
		Page<Contato> contatos = this.repository.findAll(pg);
		
		if(contatos != null) {
			return contatos;
		}
		
		return null;
	}
	
}
