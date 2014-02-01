package managedbean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import modelo.Rota;
import util.JsfUtil;
import facade.RotaFacade;

@ManagedBean
@RequestScoped
public class RotaConverter implements Converter {

	@EJB
	private RotaFacade facade;
	

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (!submittedValue.trim().equals("")  && submittedValue != null) {
			try {
				//TODO retirar linha abaixo.
				System.out.println("rotaConverter.getAsObject()");
				return facade.recuperarPorCodigo(submittedValue);
			}
			catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao recuperar rota: " + e.getCause());
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
			return String.valueOf(((Rota)value).getNome());
		}
	}
}
