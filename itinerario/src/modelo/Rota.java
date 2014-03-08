package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

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
	@OrderBy(value="sequencia")
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
	@SuppressWarnings("unchecked")
	public List<PontoRota> getParadas() {
		// TODO Classe dto para atender situação. 
		List<PontoRota> paradas = new ArrayList<PontoRota>();
		for (PontoRota p: pontos) {
			if (p.getParada()) {
				paradas.add(p);
				System.out.println("getParadas() - " + p.getDescricao() + " - " + p.getNumeroParada());
			}
		}
		// Ordena as paradas
		Collections.sort(paradas, new Comparator() { 
			@Override
		    public int compare(Object o1, Object o2) {
				PontoRota p1 = (PontoRota)o1;
				PontoRota p2 = (PontoRota)o2;
				int retorno = 0;
		    	if (p1.getNumeroParada() > p2.getNumeroParada()) {
		    		retorno = 1;
		    	} else if (p1.getNumeroParada() < p2.getNumeroParada()) {
		    		retorno = -1;
		    	}
		    	return retorno;
		    }
		});
		return paradas;
	}
}
