package teste;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class DialogBean {  
    
    public void chooseCar() {  
        RequestContext.getCurrentInstance().openDialog("selectCar");  
    }  

    public void onCarChosen(SelectEvent event) {  
        String car = (String) event.getObject();  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Car Selected", "Model:" + car);  

        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}  
         