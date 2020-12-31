package com.felipeduarte.agenda.model.enums;

public enum TipoUsuario {
	
	ADMIN(0, "ROLE_ADMIN"),
	USUARIO(1, "ROLE_USER");
	
	private int codigo;
	private String descricao;
	
	private TipoUsuario(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static TipoUsuario toEnum(Integer codigo) {
		if(codigo == null) {
			return null;
		}
		
		for(TipoUsuario x: TipoUsuario.values()) {
			if(codigo.equals(x.getCodigo())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id Inválido " + codigo);
	}
	
}
