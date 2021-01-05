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
import com.felipeduarte.agenda.resource.exceptions.AuthorizationException;
import com.felipeduarte.agenda.security.User;
import com.felipeduarte.agenda.security.UserService;

@Service
public class ContatoService {
	
	@Autowired
	private ContatoRepository contatoRepository;
	
	public Contato salvar(ContatoDTO contatoDTO) {
		
		Usuario usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
		Optional<Contato> c = this.contatoRepository.findByTelefoneAndCelularAndEmail(
				contatoDTO.getTelefone(), contatoDTO.getCelular(), contatoDTO.getEmail());
		
		Contato contato;
		
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
		
		//ContatoServiceVerificacao.verificaIdUsuarioIdContato(c.get().getUsuario().getId());
		
		contato = Contato.converteContatoDTOParaContato(contatoDTO, c.get().getUsuario());
		
		contato = this.contatoRepository.save(contato);
		
		return contato;
	}
	
	public boolean excluir(Long id) {
		
		Optional<Contato> contato = this.contatoRepository.findById(id);
		
		if(contato.isEmpty()) return false; 
		
		ContatoServiceVerificacao.verificaIdUsuarioIdContato(contato.get().getUsuario().getId());
			
		this.contatoRepository.delete(contato.get());
			
		return true;
	}	
	
	public Page<Contato> buscarPorNome(String nome,Integer pagina, Integer qtdPorPagina){
		
		Usuario usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
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
		
		ContatoServiceVerificacao.verificaIdUsuarioIdContato(contato.get().getUsuario().getId());
		
		return contato.get();
		
	}
	
	public Page<Contato> buscarTodos(Integer pagina,Integer qtdPorPagina){
		
		Usuario usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
		PageRequest pg = PageRequest.of(pagina, qtdPorPagina,Direction.ASC,"nome");
		
		Page<Contato> paginaContatos = this.contatoRepository.findByUsuario(usuario, pg);
		
		return paginaContatos;
	}
	
	private static class ContatoServiceVerificacao{
		
		public static Usuario getUsuarioLogado() {
			
			User user = (User) UserService.getUser();
			
			if(user != null) {
				
				Usuario u = new Usuario();
				u.setId(user.getId());
				u.setNome(user.getNome());
				u.setEmail(user.getUsername());
				u.setSenha(user.getPassword());
				
				return u;
				
			}else {
				throw new AuthorizationException("Usuário não logado!");
			}
			
		}
		
		public static void verificaIdUsuarioIdContato(Long idComparado) {
			Usuario usuario = getUsuarioLogado();
			
			if(!usuario.getId().equals(idComparado)) {
				throw new AuthorizationException("Este contato não pertence ao usuario logado!");
			}
			
		}
		
	}
	
}
