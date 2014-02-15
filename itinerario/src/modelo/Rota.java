package modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Rota {
	@Id
	@GeneratedValue
	private Long id;
	private String codigo; //TODO Anotar de forma a garantir unicidade na tabela.	
	private String nome;
	private String origem;
	private String destino;
	private Double quilometragem;
	private String observacao;
	private Boolean ativa;
	@OneToMany(cascade=CascadeType.ALL, mappedBy="rota", orphanRemoval=true)
	private List<PontoRota> pontos;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Double getQuilometragem() {
		return quilometragem;
	}
	public void setQuilometragem(Double quilometragem) {
		this.quilometragem = quilometragem;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Boolean getAtiva() {
		return ativa;
	}
	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
	public List<PontoRota> getPontos() {
		return pontos;
	}
	public void setPontos(List<PontoRota> pontos) {
		this.pontos = pontos;
	}
	
}
