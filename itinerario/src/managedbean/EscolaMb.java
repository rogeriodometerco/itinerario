package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;

import modelo.Escola;
import facade.EscolaFacade;

@ManagedBean
@ViewScoped
public class EscolaMb extends GenericCrudMb<Escola> 
		implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@EJB
	private EscolaFacade escolaFacade;
	private String nomePesquisa;
	
	@Override
	protected EscolaFacade getServico() {
		return escolaFacade;
	}

	@Override
	protected List<Escola> recuperarLista() throws Exception {
		if (nomePesquisa == null || nomePesquisa.trim().isEmpty()) {
			return getServico().listar();
		} else {
			return getServico().listarPorNomeContendo(nomePesquisa);
		}
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nome) {
		this.nomePesquisa = nome;
	}
}
