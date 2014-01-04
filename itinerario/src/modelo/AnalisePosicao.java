package modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class AnalisePosicao {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private AnaliseViagem analiseViagem;
	@ManyToOne
	private PosicaoVeiculo posicaoVeiculo;
	private Boolean noTrajeto;
	
	public AnalisePosicao() {
		
	}
	
	public AnalisePosicao(AnaliseViagem analiseViagem, PosicaoVeiculo posicao, boolean noTrajeto) {
		this.analiseViagem = analiseViagem;
		this.posicaoVeiculo = posicao;
		this.noTrajeto = noTrajeto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnaliseViagem getAnaliseViagem() {
		return analiseViagem;
	}

	public void setAnaliseViagem(AnaliseViagem analiseViagem) {
		this.analiseViagem = analiseViagem;
	}

	public PosicaoVeiculo getPosicaoVeiculo() {
		return posicaoVeiculo;
	}

	public void setPosicaoVeiculo(PosicaoVeiculo posicaoVeiculo) {
		this.posicaoVeiculo = posicaoVeiculo;
	}

	public Boolean getNoTrajeto() {
		return noTrajeto;
	}

	public void setNoTrajeto(Boolean noTrajeto) {
		this.noTrajeto = noTrajeto;
	}
	
}
