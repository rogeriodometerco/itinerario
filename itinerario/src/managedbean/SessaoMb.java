package managedbean;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import util.JsfUtil;

@ManagedBean
@SessionScoped
public class SessaoMb implements Serializable{

	private static final long serialVersionUID = 1L;

	public String getLogin() {
		return JsfUtil.getLogin();
	}
	
	public void logout() throws IOException {
		try {
			JsfUtil.invalidarSessao();
		} catch (Exception e) {
			//TODO Pensar como tratar
		}
	}
	
}
