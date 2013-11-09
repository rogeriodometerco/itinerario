package managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import util.JsfUtil;

import facade.LinhaFacade;

import modelo.Linha;

@ManagedBean
@ViewScoped
public class ItinerarioMb {
	private LinhaFacade facade;
	private List<Linha> lista;
	private Linha linha;
	
	public Linha getItinerario() {
		if (linha == null) {
			linha = new Linha();
		}
		return linha;
	}
	
	public void setItinerario(Linha i) {
		this.linha = i;
	}
	
	public List<Linha> getLista() {
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
	
	public void salvar() {
		try {
			facade.salvar(linha);
			linha = null;
			lista = null;
		} catch (Exception e) {
			JsfUtil.addMsgErro(e.getMessage());
			e.printStackTrace();
		}
	}
}
