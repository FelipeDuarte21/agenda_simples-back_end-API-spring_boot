package com.felipeduarte.agenda.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.felipeduarte.agenda.model.dtos.ContatoDTO;
import com.felipeduarte.agenda.resource.exceptions.ObjectBadRequestException;
import com.felipeduarte.agenda.resource.exceptions.ObjectNotFoundException;
import com.felipeduarte.agenda.service.ContatoService;

@CrossOrigin
@RestController
@RequestMapping("/contato")
public class ContatoResource {
	
	@Autowired
	private ContatoService contatoService;
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping
	public ResponseEntity<Contato> salvar(@RequestBody @Valid ContatoDTO contatoDTO){
		
		Contato contato = this.contatoService.salvar(contatoDTO);
		
		if(contato == null) throw new ObjectBadRequestException("Erro ao cadastrar contato!"); 
		
		if(contato.getNome() == null) throw new ObjectBadRequestException("Contato já cadastrado!");
			
		return ResponseEntity.status(HttpStatus.CREATED).body(contato);
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping
	public ResponseEntity<Contato> alterar(@RequestBody @Valid ContatoDTO contatoDTO){
		
		Contato contato = this.contatoService.alterar(contatoDTO);
		
		if(contato == null) throw new ObjectBadRequestException("Erro ao alterar contato");
		
		if(contato.getId() == null) throw new ObjectBadRequestException("Id não informado!");
		
		if(contato.getNome() == null) throw new ObjectNotFoundException(
				"Contato não encontrado, verifique id informado!");
			
		return ResponseEntity.status(HttpStatus.OK).body(contato);
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		
		boolean resp = this.contatoService.excluir(id);
		
		if(resp == false) throw new ObjectNotFoundException(
				"Contato não encontrado, verifique o id informado!");
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/search")
	public ResponseEntity<Page<Contato>> buscarPorNome(
			@RequestParam String nome,
			@RequestParam(defaultValue = "0") Integer pagina,
			@RequestParam(defaultValue = "5") Integer qtdPorPagina){
		
		Page<Contato> paginaContatos = this.contatoService.buscarPorNome(nome,pagina,qtdPorPagina);
		
		return ResponseEntity.status(HttpStatus.OK).body(paginaContatos);
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/{id}")
	public ResponseEntity<Contato> buscarPorId(@PathVariable Long id){
		
		Contato contato = this.contatoService.buscarPorId(id);
		
		if(contato == null) throw new ObjectNotFoundException("Contato não encontrado!");
			
		return ResponseEntity.status(HttpStatus.OK).body(contato);
	}

	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public ResponseEntity<Page<Contato>> buscarTodos(
			@RequestParam(defaultValue = "0") Integer pagina,
			@RequestParam(defaultValue = "5") Integer qtdPorPagina){
		
		Page<Contato> paginaContatos = this.contatoService.buscarTodos(pagina,qtdPorPagina);
		
		return ResponseEntity.status(HttpStatus.OK).body(paginaContatos);
	}
	
}
