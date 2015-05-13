package dto;

import java.util.Date;
import java.util.List;

import modelo.AnaliseViagem;
import modelo.FechamentoRota;
import modelo.Motorista;
import modelo.Rota;

public class PreFechamentoRota {
	private Rota rota;
	private Motorista motorista;
	private Date dataInicial;
	private Date dataFinal;
	private Boolean concluido;
	private List<AnaliseViagem> analisesViagem;

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
		return (double)Math.round(km * 100) / 100;
	}
	public Double getKmRealizado() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmRealizado();
		}
		return (double)Math.round(km * 100) / 100;
	}

	public Double getKmNoTrajeto() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmNoTrajeto();
		}
		return (double)Math.round(km * 100) / 100;
	}
	
	public Double getKmForaTrajeto() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmForaTrajeto();
		}
		return (double)Math.round(km * 100) / 100;
	}

	public Double getKmPago() {
		Double km = 0d;
		for (AnaliseViagem a: analisesViagem) {
			km += a.getKmPago();
		}
		return (double)Math.round(km * 100) / 100;
	}

	public Double getValorPago() {
		Double valor = 0d;
		for (AnaliseViagem a: analisesViagem) {
			valor += a.getValorPago();
		}
		return (double)Math.round(valor * 100) / 100;
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

	public FechamentoRota getFechamentoRota() {
		FechamentoRota f = new FechamentoRota();
		f.setRota(getRota());
		f.setMotorista(getMotorista());
		f.setDataInicial(getDataInicial());
		f.setDataFinal(getDataFinal());
		f.setAnalisesViagem(getAnalisesViagem());
		for (AnaliseViagem a: analisesViagem) {
			a.setFechamentoRota(f);
		}
		return f;
	}
}