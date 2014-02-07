package managedbean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import modelo.ProgramacaoRota;
import util.JsfUtil;
import facade.ProgramacaoRotaFacade;

@ManagedBean
@RequestScoped
public class ProgramacaoRotaConverter implements Converter {

	@EJB
	private ProgramacaoRotaFacade facade;


	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (!submittedValue.trim().equals("")  && submittedValue != null) {
			try {
				return facade.recuperar(Long.parseLong(submittedValue));
			}
			catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao recuperar programação da rota: " + e.getCause());
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
			return String.valueOf(((ProgramacaoRota)value).getId());
		}
	}
}