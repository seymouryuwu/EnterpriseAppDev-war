package mbeans;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import repository.UserRepository;
import repository.entity.User;
import repository.utils.AuthenticationUtils;

@ManagedBean(name="loginManagedBean")
@SessionScoped
public class LoginManagedBean {
	@EJB
	private UserRepository userRepository;
	
	private String username;
    private String password;
    
    private User user;
    
    private String inputOldPassword;
    private String inputNewPassword;
    private String inputConfirmPassword;
    
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            request.login(username, password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", null));
            return "/login.xhtml";
        }
        
        Principal principal = request.getUserPrincipal();
        this.user = userRepository.findUserByUsername(principal.getName());

        Logger.getLogger(SignupManagedBean.class.getName()).info("Authentication done for user: " + principal.getName());
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("User", user);
        
        if (request.isUserInRole("admin") || request.isUserInRole("normal")) {
            return "/user/index.xhtml?faces-redirect=true";
        } else {
            return "/login.xhtml";
        }
    }
    
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        try {
            this.user = null;
            request.logout();
            // clear the session
            ((HttpSession) context.getExternalContext().getSession(false)).invalidate();
        } catch (ServletException ex) {
            Logger.getLogger(SignupManagedBean.class.getName()).log(Level.SEVERE, "Failed to logout user!", ex);
        }
        return "/login.xhtml?faces-redirect=true";
    }
    
    public String changePassword() {
    	user.setPassword(inputNewPassword);
    	userRepository.changePassword(user);
    	Logger.getLogger(LoginManagedBean.class.getName()).info("User password changed");
    	logout();
        return "/changepassworddone.xhtml";
    }
    
    public void validateChangePassword(ComponentSystemEvent event) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        
        UIInput uiInputOldPassword = (UIInput) components.findComponent("oldpassword");
        String oldPassword = uiInputOldPassword.getLocalValue() == null ? "" : uiInputOldPassword.getLocalValue().toString();
        String oldPasswordId = uiInputOldPassword.getClientId();
        
        UIInput uiInputNewPassword = (UIInput) components.findComponent("newpassword");
        String newPassword = uiInputNewPassword.getLocalValue() == null ? "" : uiInputNewPassword.getLocalValue().toString();
        String newPasswordId = uiInputNewPassword.getClientId();
        
        UIInput uiInputConfirmPassword = (UIInput) components.findComponent("confirmpassword");
        String confirmPassword = uiInputConfirmPassword.getLocalValue() == null ? "" : uiInputConfirmPassword.getLocalValue().toString();
        
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage("Confirm password does not match password");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(newPasswordId, msg);
            facesContext.renderResponse();
        }
        
        if (!(user.getPassword().equals(AuthenticationUtils.encodeSHA256(oldPassword)))) {
            FacesMessage msg = new FacesMessage("Wrong old password" + "oldp: " + oldPassword + " encode: " + AuthenticationUtils.encodeSHA256(oldPassword) + " userp: " + user.getPassword());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(oldPasswordId, msg);
            facesContext.renderResponse();
        }
        
    }
    
    public boolean isAdmin() {
    	FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    	return request.isUserInRole("admin");
    }

    public User getAuthenticatedUser() {
        return user;
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

	public String getInputOldPassword() {
		return inputOldPassword;
	}

	public void setInputOldPassword(String inputOldPassword) {
		this.inputOldPassword = inputOldPassword;
	}

	public String getInputNewPassword() {
		return inputNewPassword;
	}

	public void setInputNewPassword(String inputNewPassword) {
		this.inputNewPassword = inputNewPassword;
	}

	public String getInputConfirmPassword() {
		return inputConfirmPassword;
	}

	public void setInputConfirmPassword(String inputConfirmPassword) {
		this.inputConfirmPassword = inputConfirmPassword;
	}
    
    
}
