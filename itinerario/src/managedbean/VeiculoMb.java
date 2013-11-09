package managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Linha;
import modelo.Veiculo;
import util.JsfUtil;
import facade.VeiculoFacade;

@ManagedBean
@ViewScoped
public class VeiculoMb {
	private VeiculoFacade facade;
	private List<Veiculo> lista;
	private Veiculo entidade;
	
	public Veiculo getEntidade() {
		if (entidade == null) {
			entidade = new Veiculo();
		}
		return entidade;
	}
	
	public void setEntidade(Veiculo entidade) {
		this.entidade = entidade;
	}
	
	public List<Veiculo> getLista() {
		if (lista == null) {
			recuperarLista();
		}
		return lista;
	}
	
	private void recuperarLista() {
		try {
			lista = facade.listar();
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String salvar() {
		try {
			facade.salvar(entidade);
			entidade = null;
			lista = null;
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
