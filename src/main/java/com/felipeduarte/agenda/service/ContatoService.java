package com.felipeduarte.agenda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felipeduarte.agenda.model.Contato;
import com.felipeduarte.agenda.model.Usuario;
import com.felipeduarte.agenda.model.dtos.ContatoDTO;
import com.felipeduarte.agenda.model.dtos.ContatoSalvarDTO;
import com.felipeduarte.agenda.repository.ContatoRepository;
import com.felipeduarte.agenda.resource.exceptions.AuthorizationException;
import com.felipeduarte.agenda.security.User;
import com.felipeduarte.agenda.security.UserService;
import com.felipeduarte.agenda.service.exceptions.IllegalParameterException;
import com.felipeduarte.agenda.service.exceptions.ObjectNotFoundFromParameterException;

@Service
public class ContatoService {
	
	private ContatoRepository contatoRepository;
	
	@Autowired
	public ContatoService(ContatoRepository contatoRepository) {
		this.contatoRepository = contatoRepository;
	}
	
	public ContatoDTO salvar(ContatoSalvarDTO contatoDTO) {
		
		//Recupera usuario logado
		var usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
		var optContato = this.contatoRepository.findByTelefoneAndCelularAndEmailAndUsuario(
				contatoDTO.getTelefone(), contatoDTO.getCelular(), contatoDTO.getEmail(), usuario);
		
		if(optContato.isPresent())
			throw new IllegalParameterException("Erro! Contato já cadastrado!");
		
		
		var contato = new Contato(contatoDTO);
		contato.setUsuario(usuario);
		
		contato = this.contatoRepository.save(contato);
		
		return new ContatoDTO(contato);
		
	}
	
	public ContatoDTO alterar(Long id, ContatoSalvarDTO contatoDTO) {
		
		var optContato = this.contatoRepository.findById(id);
		
		if(!optContato.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! contato não encontrado para o id informado!");
	
		//Verifica se o contato pertence ao usuario
		ContatoServiceVerificacao.verificaIdUsuarioIdContato(optContato.get().getUsuario().getId());
		
		var contato = optContato.get();
		contato.setNome(contatoDTO.getNome());
		contato.setEmail(contatoDTO.getEmail());
		contato.setTelefone(contatoDTO.getTelefone());
		contato.setCelular(contatoDTO.getCelular());
		
		contato = this.contatoRepository.save(contato);
		
		return new ContatoDTO(contato);
		
	}
	
	public void excluir(Long id) {
		
		var optContato = this.contatoRepository.findById(id);
		
		if(!optContato.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! contato não encontrado para o id informado!");
	
		//Verifica se o contato pertence ao usuario
		ContatoServiceVerificacao.verificaIdUsuarioIdContato(optContato.get().getUsuario().getId());
			
		this.contatoRepository.delete(optContato.get());
			
	}	
	
	public Page<ContatoDTO> buscarPorNome(String nome, Pageable paginacao){
		
		var usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
		Page<Contato> paginaContatos;
		
		if(!nome.isEmpty()) {
			paginaContatos = this.contatoRepository.findByUsuarioAndNomeContaining(usuario, nome, paginacao);
			
		}else {
			paginaContatos = this.contatoRepository.findByUsuario(usuario, paginacao);
			
		}
			
		return paginaContatos.map(ContatoDTO::new);
		
	}
	
	public ContatoDTO buscarPorId(Long id){
		
		var optContato = this.contatoRepository.findById(id);
		
		if(!optContato.isPresent())
			throw new ObjectNotFoundFromParameterException("Erro! contato não encontrado para o id informado!");
	
		//Verifica se o contato pertence ao usuario
		ContatoServiceVerificacao.verificaIdUsuarioIdContato(optContato.get().getUsuario().getId());
		
		return new ContatoDTO(optContato.get());
		
	}
	
	public Page<ContatoDTO> buscarTodos(Pageable paginacao){
		
		var usuario = ContatoServiceVerificacao.getUsuarioLogado();
		
		var paginaContatos = this.contatoRepository.findByUsuario(usuario, paginacao);
		
		return paginaContatos.map(ContatoDTO::new);
		
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
