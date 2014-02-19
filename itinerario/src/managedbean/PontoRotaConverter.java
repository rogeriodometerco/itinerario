package managedbean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import modelo.PontoRota;
import util.JsfUtil;
import facade.PontoRotaFacade;

@ManagedBean
@RequestScoped
public class PontoRotaConverter implements Converter {

	@EJB
	private PontoRotaFacade facade;


	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (submittedValue != null && !submittedValue.trim().equals("")) {
			try {
				return facade.recuperar(Long.parseLong(submittedValue));
			}
			catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao recuperar ponto de rota: " + e.getCause());
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
			return String.valueOf(((PontoRota)value).getId());
		}
	}
}