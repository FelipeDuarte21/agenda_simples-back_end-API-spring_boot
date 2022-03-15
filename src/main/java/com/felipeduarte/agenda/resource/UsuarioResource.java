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

import com.felipeduarte.agenda.model.dtos.UsuarioDTO;
import com.felipeduarte.agenda.model.dtos.UsuarioSalvarDTO;
import com.felipeduarte.agenda.resource.exceptions.ObjectBadRequestException;
import com.felipeduarte.agenda.resource.exceptions.ObjectNotFoundException;
import com.felipeduarte.agenda.service.UsuarioService;
import com.felipeduarte.agenda.service.exceptions.ObjectNotFoundFromParameterException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {
	
	private UsuarioService usuarioService;
	
	@Autowired
	public UsuarioResource(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@ApiOperation(value = "Cadastra um novo usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Usuario cadastrado com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros recebidos"),
	})
	@PostMapping(produces = "application/json", consumes = "application/json")
	public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioSalvarDTO usuarioDTO,
			UriComponentsBuilder uriBuilder){
		
		try {
			
			var usuario = this.usuarioService.salvar(usuarioDTO);
			
			var uri = uriBuilder.path("/api/usuarios/{id}")
					.buildAndExpand(usuario.getId()).toUri();
			
			return ResponseEntity.created(uri).body(usuario);
			
		}catch(IllegalArgumentException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Atualiza um usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuario atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Erro nos parâmetros recebidos"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuario não encontrado"),
	})
	@PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<UsuarioDTO> alterar(@PathVariable(name = "id") Long id, 
			@RequestBody @Valid UsuarioSalvarDTO usuarioDTO){
		
		try {
			
			var usuario = this.usuarioService.alterar(id, usuarioDTO);
			
			return ResponseEntity.ok(usuario);
			
		}catch(IllegalArgumentException ex) {
			throw new ObjectBadRequestException(ex.getMessage());
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "Exclui um usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuario excluido com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuario não encontrada"),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable(name = "id") Long id){
		
		try {
			
			this.usuarioService.excluir(id);
			
			return ResponseEntity.ok().build();
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "busca um usuario pela identificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuario encontado com sucesso"),
			@ApiResponse(code = 401, message = "Acesso não autorizado"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 404, message = "Usuario não encontrado"),
	})
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable(name = "id") Long id){
		
		try {
			
			var usuario = this.usuarioService.buscarPorId(id);
			
			return ResponseEntity.ok(usuario);
			
		}catch(ObjectNotFoundFromParameterException ex) {
			throw new ObjectNotFoundException(ex.getMessage());
			
		}
		
	}
	
	@ApiOperation(value = "busca um usuario pelo email")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuario encontrado com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@GetMapping(value = "/email", produces = "application/json")
	public ResponseEntity<UsuarioDTO> buscarPorEmail(
			@RequestParam(name = "value", defaultValue = "") String email){
		
		var usuario = this.usuarioService.buscarPorEmail(email);
		
		return ResponseEntity.ok(usuario);
		
	}
	
	
	@ApiOperation(value = "busca uma pagina de usuarios por nome")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuarios encontrados com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/nome", produces = "application/json")
	public ResponseEntity<Page<UsuarioDTO>> buscarPorNome(@RequestParam(defaultValue = "") String nome, 
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao){
		
		var pageUsuario = this.usuarioService.buscarPorNome(nome, paginacao);
		
		return ResponseEntity.ok(pageUsuario);
		
	}
	
	
	@ApiOperation(value = "busca uma pagina de usuarios")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuarios encontrados com sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
	})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UsuarioDTO>> buscarTodos(
			@PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "nome") Pageable paginacao){
		
		var paginaUsuario = this.usuarioService.buscarTodos(paginacao);
		
		return ResponseEntity.ok(paginaUsuario);
		
	}

}
