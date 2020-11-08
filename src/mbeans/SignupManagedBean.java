package mbeans;

import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import repository.UserRepository;
import repository.entity.User;

@ManagedBean(name="signupManagedBean")
@SessionScoped
public class SignupManagedBean {
	@EJB
	private UserRepository userRepository;
	
	private String username;
    private String password;
    private String confirmPassword;
    
    public void validatePassword(ComponentSystemEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        // get password
        UIInput uiInputPassword = (UIInput) components.findComponent("password");
        String password = uiInputPassword.getLocalValue() == null ? "" : uiInputPassword.getLocalValue().toString();
        String passwordId = uiInputPassword.getClientId();
        // get confirm password
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmpassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? "" : uiInputConfirmPassword.getLocalValue().toString();
        
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage("Confirm password does not match password");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(passwordId, msg);
            facesContext.renderResponse();
        }
        
        if (userRepository.findUserByUsername(username) != null) {
            FacesMessage msg = new FacesMessage("User with this username already exists");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(passwordId, msg);
            facesContext.renderResponse();
        }
        
    }
    
    public String signup() {
        User user = new User(username, password);
        userRepository.createUser(user);
        Logger.getLogger(SignupManagedBean.class.getName()).info("New user created with username: " + username);
        return "/signupdone.xhtml";
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

    
}
