package dto;

public class NomeAssinatura {
	private String nome;
	private String descricao;
	
	public NomeAssinatura(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
}
