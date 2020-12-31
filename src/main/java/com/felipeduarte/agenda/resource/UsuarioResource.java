package com.felipeduarte.agenda.resource;

import javax.validation.Valid;

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

import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.UsuarioDTO;
import com.felipeduarte.agenda.resource.exceptions.ObjectBadRequestException;
import com.felipeduarte.agenda.resource.exceptions.ObjectNotFoundException;
import com.felipeduarte.agenda.service.UsuarioService;

@CrossOrigin
@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<Usuario> salvar(@RequestBody @Valid UsuarioDTO usuarioDTO){
		
		Usuario usuario = this.usuarioService.salvar(usuarioDTO);
		
		if(usuario == null) throw new ObjectBadRequestException("Erro ao cadastrar usuário!");
		
		if(usuario.getEmail() == null) throw new ObjectBadRequestException("Usuario já cadastrado!");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}
	
	@PutMapping
	public ResponseEntity<Usuario> alterar(@RequestBody @Valid UsuarioDTO usuarioDTO){
		
		Usuario usuario = this.usuarioService.alterar(usuarioDTO);
		
		if(usuario == null) throw new ObjectBadRequestException("Erro ao alterar usuário!");
		
		if(usuario.getId() == null) throw new ObjectBadRequestException("Id do usuario não informado!");
		
		if(usuario.getNome() == null) throw new ObjectNotFoundException(
				"Usuario não encontrado, verifique o id informado!");
		
		return ResponseEntity.status(HttpStatus.OK).body(usuario);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		
		boolean resp = this.usuarioService.excluir(id);
		
		if(resp == false) throw new ObjectNotFoundException("Erro ao excluir usuário, verifique o id informado!");
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<Usuario>> buscarTodos(
			@RequestParam(defaultValue = "0") Integer pagina, 
			@RequestParam(defaultValue = "4") Integer qtdPorPagina){
		
		Page<Usuario> paginaUsuario = this.usuarioService.buscarTodos(pagina, qtdPorPagina);
		
		return ResponseEntity.status(HttpStatus.OK).body(paginaUsuario);
		
	}

}
