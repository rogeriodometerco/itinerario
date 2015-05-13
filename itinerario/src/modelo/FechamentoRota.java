package modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class FechamentoRota {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Rota rota;
	@ManyToOne
	private Motorista motorista;
	private Date dataInicial;
	private Date dataFinal;
	private Boolean concluido;

	@OneToMany(fetch=FetchType.EAGER, 
			cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH},
			mappedBy="fechamentoRota")
	private List<AnaliseViagem> analisesViagem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<AnaliseViagem> getAnalisesViagem() {
		return analisesViagem;
	}

	public void setAnalisesViagem(List<AnaliseViagem> analisesViagem) {
		this.analisesViagem = analisesViagem;
	}

	public Boolean getConcluido() {
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}

	public Integer getQtdeViagensPrevistas() {
		return analisesViagem.size();
	}

	public Integer getQtdeViagensRealizadas() {
		Integer qtde = 0;
		for (AnaliseViagem a: analisesViagem) {
			if (a.getKmRealizado() > 0) {
				qtde++;
			}
		}
		return qtde;
	}

	public Double getKmPrevisto() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmPrevisto();
		}
		return km;
	}

	public Double getKmRealizado() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmRealizado();
		}
		return km;
	}

	public Double getKmNoTrajeto() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmNoTrajeto();
		}
		return km;
	}

	public Double getKmForaTrajeto() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmForaTrajeto();
		}
		return km;
	}

	public Double getKmPago() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmPago();
		}
		return km;
	}

	public Double getValorPago() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getValorPago();
		}
		return km;
	}

	public Integer getParadasPrevistas() {
		Integer paradas = 0;
		for (AnaliseViagem a: analisesViagem) {
			paradas += a.getParadasPrevistas();
		}
		return paradas;
	}

	public Integer getParadasCumpridas() {
		Integer paradas = 0;
		for (AnaliseViagem a: analisesViagem) {
			paradas += a.getParadasCumpridas();
		}
		return paradas;
	}

}