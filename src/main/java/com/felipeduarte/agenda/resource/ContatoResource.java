package com.felipeduarte.agenda.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.felipeduarte.agenda.model.dtos.ContatoDTO;
import com.felipeduarte.agenda.model.dtos.ContatoSalvarDTO;
import com.felipeduarte.agenda.resource.exceptions.ObjectBadRequestException;
import com.felipeduarte.agenda.resource.exceptions.ObjectNotFoundException;
import com.felipeduarte.agenda.service.ContatoService;
import com.felipeduarte.agenda.service.exceptions.IllegalParameterException;
import com.felipeduarte.agenda.service.exceptions.ObjectNotFoundFromParameterException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/contatos")
public class ContatoResource {
	
	private ContatoService contatoService;
	
	@Autowired
	public ContatoResource(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	@ApiOperation(value = "Cadastra um novo contato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Contato cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros recebidos"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<ContatoDTO> salvar(@RequestBody @Valid ContatoSalvarDTO contatoDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var contato = this.contatoService.salvar(contatoDTO);
			
			var uri = uriBuilder.path("/api/contatos/{id}")
					.buildAndExpand(contato.getId()).toUri();
			
			return ResponseEntity.created(uri).body(contato);
			
		}catch(IllegalParameterException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Atualiza um contato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Contato atualizado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Contato não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@PutMapping(value = "{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ContatoDTO> alterar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid ContatoSalvarDTO contatoDTO){
		
		try {
			
			var contato = this.contatoService.alterar(id, contatoDTO);
			
			return ResponseEntity.ok(contato);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui um contato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Contato excluido com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuario não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		
		try {
			
			this.contatoService.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Busca uma pagina de contatos por nome")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagina encontrada com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/nome", produces = "application/json")
	public ResponseEntity<Page<ContatoDTO>> buscarPorNome(@RequestParam(name = "value", defaultValue = "") String nome,
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao){
		
		var paginaContatos = this.contatoService.buscarPorNome(nome,paginacao);
		
		return ResponseEntity.ok(paginaContatos);
		
	}
	
	@ApiOperation(value = "Buscar um contato pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Contato encontrado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Contato não encontrado"),
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ContatoDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var contato = this.contatoService.buscarPorId(id);
			
			return ResponseEntity.ok(contato);
			
		}catch (ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}

	@ApiOperation(value = "Busca uma pagina de contatos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagina encontrada com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado")
	})
	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping(produces = "application/json")
	public ResponseEntity<Page<ContatoDTO>> buscarTodos(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao){
		
		var paginaContatos = this.contatoService.buscarTodos(paginacao);
		
		return ResponseEntity.ok(paginaContatos);
		
	}
	
}
