package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.CustomerManagedBean;
import mbeans.UserManagedBean;
import repository.entity.Customer;
import repository.entity.User;

@RequestScoped
@Named("userController")
public class UserController {
	@ManagedProperty(value = "#{userManagedBean}")
	private UserManagedBean userManagedBean;
	
	private CustomerController customerCtrl;
	
	private ArrayList<User> users  = new ArrayList<>();
	private User user = new User();
	
	public UserController() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		userManagedBean = (UserManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userManagedBean");
		
		customerCtrl = (CustomerController) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "customerController");
		
		updateUserList();
		
		String username = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
		if (username != null) {	        
			user = userManagedBean.getUserByUsername(username);
		}
	}
	
	public void updateUserList() {
		users.clear();
		for (User user : userManagedBean.getAllUsers()) {
			users.add(user);
		}
		setUsers(users);
	}
	
	public String getUserGroup(String username) {
		return userManagedBean.getUserGroup(username);
	}
	
	public void assignConnectUser() {
		Customer customer = customerCtrl.getCustomer();
		customer.setConnectUser(user);
		customerCtrl.assignConnectUser(customer);
	}
	
	public void changeGroup() {
		
		userManagedBean.changeUserGroup(user);
	}
	
	public boolean isMyself(String username) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		User user = (User) externalContext.getSessionMap().get("User");
		return user.getUsername().equals(username);
	}
	
	public String viewUser(String username) {
		User user = userManagedBean.getUserByUsername(username);
		customerCtrl.searchCustomerByUser(user);
		return "/admin/userdetails.xhtml";
	}
	
	public String confirmRemoveUser(String username) {
		User user = userManagedBean.getUserByUsername(username);
		customerCtrl.searchCustomerByUser(user);
		return "/admin/confirmremoveuser.xhtml";
	}
	
	
	public String removeUser(String username) {
		try {
			userManagedBean.removeUser(username);
			updateUserList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("User has been deleted succesfully"));
			return "/admin/userlist.xhtml";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("User cannot be deleted"));
			return "/admin/userlist.xhtml";
		} 
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
