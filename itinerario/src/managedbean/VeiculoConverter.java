package managedbean;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import util.JsfUtil;

import modelo.Veiculo;
import facade.VeiculoFacade;

@ManagedBean
@RequestScoped
public class VeiculoConverter implements Converter {

	@EJB
	private VeiculoFacade facade;
	

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		if (!submittedValue.trim().equals("")  && submittedValue != null) {
			try {
				return facade.recuperarPorIdentificacao(submittedValue);
			}
			catch (Exception e) {
				//TODO Como tratar exceção
				JsfUtil.addMsgErro("Erro ao recuperar veículo: " + e.getCause());
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
			return String.valueOf(((Veiculo)value).getIdentificacao());
		}
	}
}
