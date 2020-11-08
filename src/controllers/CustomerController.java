package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import dto.CustomerDTO;
import mbeans.CustomerManagedBean;
import repository.entity.Customer;
import repository.entity.IndustryType;
import repository.entity.User;

@RequestScoped
@Named("customerController")
public class CustomerController {
	@ManagedProperty(value = "#{customerManagedBean}")
	private CustomerManagedBean customerManagedBean;

	private ArrayList<Customer> customers = new ArrayList<>();

	private Customer customer = new Customer();
	private CustomerDTO customerDTO = new CustomerDTO();

	private String searchCustomerId = null;



	public CustomerController() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();

	
		customerManagedBean = (CustomerManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "customerManagedBean");

	

		updateCustomerList();

		String customerId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("customerId");

		if (customerId != null) {
			customer = customerManagedBean.getCustomerById(Long.valueOf(customerId));
			customerDTO = customerManagedBean.convertCustomerToDTO(customer);
		}

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

	public boolean searchCustomerById(long customerId) {
		customer = customerManagedBean.getCustomerById(customerId);
		if (customer != null) {
			customers.clear();
			customers.add(customer);
			setCustomers(customers);
			return true;
		}
		return false;
	}
	
	public void searchCustomerByUser(User user) {
		customers.clear();
		for (Customer customer : customerManagedBean.getCustomersByUser(user)) {
			customers.add(customer);
		}
		setCustomers(customers);
	}
	
	public void searchCustomerByIndustry(IndustryType industryType) {
		customers.clear();
		for (Customer customer : customerManagedBean.getCustomersByIndustry(industryType)) {
			customers.add(customer);
		}
		setCustomers(customers);
	}

	public boolean isAllowedToEdit(Customer customer) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		if (request.isUserInRole("admin")) {
			return true;
		} else if (request.isUserInRole("normal")) {
			User user = (User) externalContext.getSessionMap().get("User");
			for (Customer c : customerManagedBean.getCustomersByUser(user)) {
				if (c.getCustomerId() == customer.getCustomerId()) return true;
			}
		}
		return false;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}

	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}

	public void addCustomer(CustomerDTO customerDTO) {
		try {
			customerManagedBean.addCustomer(customerDTO);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Customer has been added succesfully"));

		} catch (Exception ex) {

		}
	}

	public void removeCustomer(long customerId) {
		try {
			customerManagedBean.removeCustomer(customerId);
			updateCustomerList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Customer has been deleted succesfully"));

		} catch (Exception ex) {

		}
	}

	

	public void updateCustomer(CustomerDTO customerDTO) {
		try {
			customerDTO.setCustomerId(customer.getCustomerId());
			customerDTO.setConnectUser(customer.getConnectUser());
			customerManagedBean.updateCustomer(customerDTO);
			updateCustomerList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Customer has been edited succesfully"));
		} catch (Exception ex) {

		}
	}
	
	public void assignConnectUser(Customer customer) {
		try {
			customerManagedBean.assignConnectUser(customer);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Connect user has been assigned succesfully"));
		} catch (Exception ex) {

		}
	}

	public String getSearchCustomerId() {
		return searchCustomerId;
	}

	public void setSearchCustomerId(String searchCustomerId) {
		this.searchCustomerId = searchCustomerId;
	}

}
