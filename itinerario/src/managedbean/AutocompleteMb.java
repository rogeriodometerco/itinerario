package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Veiculo;
import util.JsfUtil;
import facade.EscolaFacade;

@ManagedBean
@ViewScoped
public class AutocompleteMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private Veiculo veiculoTeste;
	@EJB
	private EscolaFacade facade;
	private String opcao;
	private ArrayList<String> listax;
	

	public Veiculo getVeiculoTeste() {
		return veiculoTeste;
	}

	public void setVeiculoTeste(Veiculo veiculoTeste) {
		this.veiculoTeste = veiculoTeste;
	}

	public void testar() {
		if (veiculoTeste == null) {
			System.out.println("Ve�culo � null");
			JsfUtil.addMsgSucesso("ve�culo � null");
		} else {
			System.out.println("Ve�culo � " + veiculoTeste.getPlaca());
			JsfUtil.addMsgSucesso("ve�culo � " + veiculoTeste.getPlaca());
		}
		if (opcao == null) {
			System.out.println("Op��o � null");
			JsfUtil.addMsgSucesso("Op��o � null");
		} else {
			System.out.println("op��o � " + opcao);
			JsfUtil.addMsgSucesso("op��o � " + opcao);
		}
	}
	
	public List<String> getListax() {
		listax = new ArrayList<String>();
		listax.add("a1");
		listax.add("a2");
		listax.add("a3");
		return listax;
	}
	
	public void setOpcao(String s) {
		opcao = s;
	}
	
	public String getOpcao() {
		return opcao;
	}
}