package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SkinBean implements Serializable {

		 private static final long serialVersionUID = -6042519587796329117L;

		 private String skin = "blueSky";

		 public String getSkin() {
			 return skin;
		 }
		 public void setSkin(String skin) {
			 System.out.println("setou");
			 this.skin = skin;
		 }

}
