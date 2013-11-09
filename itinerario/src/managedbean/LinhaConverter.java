package managedbean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import modelo.Linha;
import util.Ejb;
import util.JsfUtil;
import facade.LinhaFacade;

@FacesConverter(forClass=Linha.class)
public class LinhaConverter implements Converter {

	private LinhaFacade facade = Ejb.lookup(LinhaFacade.class);
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Linha result = null;
		if (!value.trim().equals("")  && value != null) {
			try {
				Long id = Long.parseLong(value.trim());
				result = facade.recuperar(id);
			} catch (Exception e) {
				JsfUtil.addMsgErro("Erro ao converter entidade linha: " + e.getMessage());
			}
		}
		return result;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {		
		if (value.equals("") || value == null) {
			return null;
		}
		else {
			return String.valueOf(((Linha)value).getId());
		}
	}

}
