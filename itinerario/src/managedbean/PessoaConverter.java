package managedbean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import modelo.Pessoa;
import util.JsfUtil;
import facade.PessoaFacade;

@ManagedBean
@RequestScoped
public class PessoaConverter implements Converter {

	@EJB
	private PessoaFacade facade;


	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (!submittedValue.trim().equals("")  && submittedValue != null) {
			try {
				return facade.recuperar(Long.parseLong(submittedValue));
			}
			catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao recuperar pessoa: " + e.getCause());
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null || value.equals("")) {
			return null;
		}
		else {
			return String.valueOf(((Pessoa)value).getId());
		}
	}
}