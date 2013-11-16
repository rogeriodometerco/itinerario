package managedbean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.PosicaoVeiculo;
import modelo.Veiculo;
import util.JsfUtil;
import facade.PosicaoVeiculoFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class PosicaoVeiculoMb {
	private Veiculo veiculo;
	private List<PosicaoVeiculo> posicoes;
	private Date data;
	
	@EJB
	private VeiculoFacade veiculoFacade;
	
	@EJB
	private PosicaoVeiculoFacade posicaoFacade;
	
	@PostConstruct
	private void inicializar() {
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 6);
		data = new Date(c.getTimeInMillis());
		getVeiculo().setIdentificacao("ZZZ9999");
		this.inicializarVeiculo();
		listar();
	}


	public Veiculo getVeiculo() {
		if (veiculo == null) {
			veiculo = new Veiculo();
		}
		return veiculo;
	}

	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public List<PosicaoVeiculo> getPosicoes() {
		return posicoes;
	}
	
	public void inicializarVeiculo() {
		try {
			this.veiculo = veiculoFacade.recuperarPorIdentificacao(veiculo.getIdentificacao());
			if (veiculo == null) {
				JsfUtil.addMsgErro("Veiculo não cadastrado.");
			}
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao consultar veículo: " + e.getMessage());
		}
	}
	
	public void listar() {
		try {
			this.posicoes = posicaoFacade.recuperarPosicoes(veiculo, data);
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar posições do veículo: " + e.getMessage());
		}
	}
}
