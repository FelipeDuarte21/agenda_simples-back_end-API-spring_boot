package com.felipeduarte.agenda.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.felipeduarte.agenda.resource.exceptions.ObjectBadRequestException;
import com.felipeduarte.agenda.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<Usuario> salvar(@RequestBody @Valid Usuario usuario){
		
		usuario = this.usuarioService.salvar(usuario);
		
		if(usuario == null) throw new ObjectBadRequestException("Erro ao cadastrar usuário");
		
		return ResponseEntity.status(HttpStatus.OK).body(usuario);
	}
	
	@PutMapping
	public ResponseEntity<Usuario> alterar(@RequestBody @Valid Usuario usuario){
		
		usuario = this.usuarioService.alterar(usuario);
		
		if(usuario == null) throw new ObjectBadRequestException("Erro ao alterar usuário");
		
		return ResponseEntity.status(HttpStatus.OK).body(usuario);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id){
		
		boolean resp = this.usuarioService.excluir(id);
		
		if(resp == false) throw new ObjectBadRequestException("Erro ao excluir usuário");
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<Usuario>> buscarTodos(
			@RequestParam Integer pagina, @RequestParam Integer qtdPorPagina){
		
		Page<Usuario> paginaUsuario = this.usuarioService.buscarTodos(pagina, qtdPorPagina);
		
		return ResponseEntity.status(HttpStatus.OK).body(paginaUsuario);
		
	}

}
