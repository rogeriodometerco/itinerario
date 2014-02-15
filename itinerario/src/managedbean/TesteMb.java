package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import modelo.Escola;
import util.JsfUtil;
import facade.TesteFacade;

@ManagedBean
@ViewScoped
public class TesteMb implements Serializable {
	private static final long serialVersionUID = 1L;
	private Escola escola;
	@EJB
	private TesteFacade facade;
	private List<Escola> escolas;
	
	public void testar() {
		try {
		escolas = new ArrayList<Escola>();
		for (int i = 0; i < 100; i++) {
			escola = new Escola();
			escola.setNome("Escola teste " + ++i);
			escolas.add(escola);
		}
		facade.salvar(escolas);
		} catch (Exception e) {
			JsfUtil.addMsgErro("getLocalizedMessage(): " + e.getLocalizedMessage());
			JsfUtil.addMsgErro("getMessage(): " + e.getMessage());
			JsfUtil.addMsgErro("getCause(): " + e.getCause());
		}
			
	}
}