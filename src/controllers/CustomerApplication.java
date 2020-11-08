package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.CustomerManagedBean;
import repository.entity.Customer;

@Named(value = "customerApplication")
@ApplicationScoped
public class CustomerApplication {
	@ManagedProperty(value = "#{customerManagedBean}")
	private CustomerManagedBean customerManagedBean;

	private ArrayList<Customer> customers;

	public CustomerApplication() throws Exception {
		customers = new ArrayList<>();

		// instantiate customerManagedBean
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		customerManagedBean = (CustomerManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "customerManagedBean");

		
		updateCustomerList();
	}

	public ArrayList<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	public void updateCustomerList() {

		customers.clear();
		for (Customer customer : customerManagedBean.getAllCustomers()) {
			customers.add(customer);
		}
		setCustomers(customers);

	}
	
}
