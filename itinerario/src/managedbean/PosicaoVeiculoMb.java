package managedbean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.MapModel;

import modelo.PosicaoVeiculo;
import modelo.Veiculo;
import util.JsfUtil;
import util.RotaMapModel;
import facade.PosicaoVeiculoFacade;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class PosicaoVeiculoMb {
	private Veiculo veiculo;
	private List<PosicaoVeiculo> posicoes;
	private Date data;
	
	private Date horaInicial;
	private Date horaFinal;
	private RotaMapModel rotaMapModel;
	
	
	@EJB
	private VeiculoFacade veiculoFacade;
	
	@EJB
	private PosicaoVeiculoFacade posicaoFacade;
	
	@PostConstruct
	private void inicializar() {
		rotaMapModel = new RotaMapModel();
		rotaMapModel.setDraggable(false);
		Calendar c = Calendar.getInstance();
		c.set(2013, 10, 6);
		data = new Date(c.getTimeInMillis());
		try {
			veiculo = veiculoFacade.recuperarPorPlaca("ROG0000");
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.set(1970, 0, 1, 16, 0, 0);
		horaInicial = c.getTime();
		c.set(1970, 0, 1, 18, 0, 0);
		horaFinal = c.getTime();
		listar();
	}

	public Veiculo getVeiculo() {
		if (veiculo == null) {
			veiculo = new Veiculo();
		}
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Date getHoraInicial() {
		return horaInicial;
	}


	public void setHoraInicial(Date horaInicial) {
		this.horaInicial = horaInicial;
	}


	public Date getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Date horaFinal) {
		this.horaFinal = horaFinal;
	}

	public MapModel getMapModel() {
		return rotaMapModel.getMapModel();
	}

	public String getCentroMapa() {
		return rotaMapModel.getCentro();
	}

	public Integer getZoomMapa() {
		return rotaMapModel.getZoom();
	}

	public void onMapStateChange(StateChangeEvent event) {
		rotaMapModel.onMapStateChange(event);
	}

	public List<PosicaoVeiculo> getPosicoes() {
		return posicoes;
	}
	
	public void listar() {
		if (veiculo == null) {
			JsfUtil.addMsgErro("Informe o veículo para a pesquisa.");
			return;
		}
		if (data == null) {
			JsfUtil.addMsgErro("Informe a data para a pesquisa.");
			return;
		}
		if (horaInicial == null) {
			JsfUtil.addMsgErro("Informe a hora inicial para a pesquisa.");
			return;
		}
		if (horaFinal == null) {
			JsfUtil.addMsgErro("Informe a hora final para a pesquisa.");
			return;
		}
		if (horaFinal.before(horaInicial)) {
			JsfUtil.addMsgErro("Hora final não pode ser menor que a hora inicial para a pesquisa.");
			return;
		}
			
		if (horaFinal.getTime() - horaInicial.getTime() > (1000 * 60 * 60 * 2)) {
			JsfUtil.addMsgErro("Intervalo de horário para a pesquisa não pode ser superior a 2 horas.");
			return;
		}
		// Calendar para manipular o parâmetro <data>.
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(data);
		
		// Data e hora inicial.
		Calendar dataHoraInicial = Calendar.getInstance();
		dataHoraInicial.setTime(horaInicial);
		dataHoraInicial.set(dataCalendar.get(Calendar.YEAR), dataCalendar.get(Calendar.MONTH), dataCalendar.get(Calendar.DATE));
		// Data e hora final.
		Calendar dataHoraFinal = Calendar.getInstance();
		dataHoraFinal.setTime(horaFinal);
		dataHoraFinal.set(dataCalendar.get(Calendar.YEAR), dataCalendar.get(Calendar.MONTH), dataCalendar.get(Calendar.DATE));
		try {
			this.posicoes = posicaoFacade.recuperarPosicoes(veiculo, 
					dataHoraInicial.getTime(), dataHoraFinal.getTime());
			rotaMapModel.inicializarPropriedadesDeMapa();
			rotaMapModel.setPosicoesVeiculo(posicoes);
			rotaMapModel.centralizarPelasPosicoesVeiculo();
		} catch (Exception e) {
			JsfUtil.addMsgErro("Erro ao recuperar posições do veículo: " + e.getMessage());
		}
	}
}
