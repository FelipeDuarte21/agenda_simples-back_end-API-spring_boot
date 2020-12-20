package com.felipeduarte.agenda.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.felipeduarte.agenda.model.Contato;
import com.felipeduarte.agenda.service.ContatoService;

@CrossOrigin
@RestController
@RequestMapping("/contato")
public class ContatoResource {
	
	@Autowired
	private ContatoService service;
	
	@PostMapping
	public ResponseEntity<Contato> salvar(@RequestBody Contato contato){
		
		contato = this.service.salvar(contato);
		
		if(contato != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(contato);
		}
			
		throw new RuntimeException("Erro!");
		
	}
	
	@PutMapping
	public ResponseEntity<Contato> alterar(@RequestBody Contato contato){
		
		contato = this.service.alterar(contato);
		
		if(contato != null) {
			return ResponseEntity.status(HttpStatus.OK).body(contato);
		}
			
		throw new RuntimeException("Erro!");
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		
		boolean certo = this.service.excluir(id);
		
		if(certo) return ResponseEntity.status(HttpStatus.OK).build();
		
		throw new RuntimeException("Erro!");
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<Page<Contato>> buscarPorNome(@RequestParam String nome,
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size){
		
		Page<Contato> contatos = this.service.buscarPorNome(nome,page,size);
		
		if(contatos != null) {
			return ResponseEntity.status(HttpStatus.OK).body(contatos);
		}
		
		throw new RuntimeException("Erro!");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Contato> buscarPorId(@PathVariable Long id){
		
		Contato contato = this.service.buscarPorId(id);
		
		if(contato != null) {
			return ResponseEntity.status(HttpStatus.OK).body(contato);
		}
		
		throw new RuntimeException("Contato não encontrado!");
	}

	@GetMapping
	public ResponseEntity<Page<Contato>> buscarTodos(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size){
		
		Page<Contato> contatos = this.service.buscarTodos(page,size);
		
		if(contatos != null) {
			return ResponseEntity.status(HttpStatus.OK).body(contatos);
		}
		
		throw new RuntimeException("Nada Encontrado!");
	}
	
}
