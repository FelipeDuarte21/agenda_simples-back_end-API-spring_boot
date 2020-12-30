package com.felipeduarte.agenda.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Contato;
import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.ContatoDTO;
import com.felipeduarte.agenda.repository.ContatoRepository;

@Service
public class ContatoService {
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public Contato salvar(ContatoDTO contatoDTO) {
		
		Usuario usuario = this.usuarioService.buscarPorId(contatoDTO.getUsuario());
		
		Contato contato;
		
		if(usuario == null) {
			contato = new Contato();
			contato.setUsuario(null);
			return contato;
		}
		
		Optional<Contato> c = this.contatoRepository.findByTelefoneAndCelularAndEmail(
				contatoDTO.getTelefone(), contatoDTO.getCelular(), contatoDTO.getEmail());
		
		if(c.isPresent()) {
			contato = new Contato();
			contato.setNome(null);
			return contato;
		}
		
		contato = Contato.converteContatoDTOParaContato(contatoDTO, usuario);
		
		contato = this.contatoRepository.save(contato);
		
		return contato;
	}
	
	public Contato alterar(ContatoDTO contatoDTO) {
		
		Contato contato;
		
		if(contatoDTO.getId() == null) {
			contato = new Contato();
			contato.setId(null);
			return contato;
		}
	
		Optional<Contato> c = this.contatoRepository.findById(contatoDTO.getId());
		
		if(c.isEmpty()) {
			contato = new Contato();
			contato.setId(0L);
			contato.setNome(null);
			return contato;
		}
		
		contato = Contato.converteContatoDTOParaContato(contatoDTO, c.get().getUsuario());
		
		contato = this.contatoRepository.save(contato);
		
		return contato;
	}
	
	public boolean excluir(Long id) {
		
		Optional<Contato> contato = this.contatoRepository.findById(id);
		
		if(contato.isEmpty()) return false; 
			
		this.contatoRepository.delete(contato.get());
			
		return true;
	}
	
	public Page<Contato> buscarPorNome(Long idUsuario, String nome,Integer pagina, Integer qtdPorPagina){
		
		Usuario usuario = this.usuarioService.buscarPorId(idUsuario);
		
		if(usuario == null) return null;
		
		PageRequest pg = PageRequest.of(pagina, qtdPorPagina,Direction.ASC,"nome");
		
		Page<Contato> paginaContatos;
		
		if(!nome.isEmpty()) {
			paginaContatos = this.contatoRepository.findByUsuarioAndNomeContaining(usuario, nome, pg);
		}else {
			paginaContatos = this.contatoRepository.findByUsuario(usuario, pg);
		}
			
		return paginaContatos;
	}
	
	public Contato buscarPorId(Long id){
		
		Optional<Contato> contato = this.contatoRepository.findById(id);
		
		if(contato.isEmpty()) return null;
		
		return contato.get();
		
	}
	
	public Page<Contato> buscarTodos(Long idUsuario, Integer pagina,Integer qtdPorPagina){
		
		Usuario usuario = this.usuarioService.buscarPorId(idUsuario);
	
		if(usuario == null) return null;
		
		PageRequest pg = PageRequest.of(pagina, qtdPorPagina,Direction.ASC,"nome");
		
		Page<Contato> paginaContatos = this.contatoRepository.findByUsuario(usuario, pg);
		
		return paginaContatos;
	}
	
}
