package controllers;

import java.util.List;

import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.ContactManagedBean;
import repository.entity.Contact;
import repository.entity.Customer;

@RequestScoped
@Named("searchController")
public class SearchController {
	private CustomerController customerCtrl;
	private ContactController contactCtrl;
	
	private String customerSearchResult = "";
	private String contactSearchResult = "";
	
	public SearchController() throws Exception {
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		
		customerCtrl = (CustomerController) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "customerController");
		contactCtrl = (ContactController) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "contactController");
		
	}
	
	/*
	public String searchByCustomerIdAndContactId(Customer searchCustomer, Contact searchContact) {
		Customer customer = customerCtrl.searchCustomerById(searchCustomer.getCustomerId());
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ContactManagedBean contactManagedBean = (ContactManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "contactManagedBean");
		List<Contact> searchContactList = contactManagedBean.getContactsByCustomer(customer);
		
		
		return "/user/details.xhtml";
	}
	*/
	
	public void searchByCustomerId(String searchCustomerId) {
		searchCustomerId = (searchCustomerId == null) ? "" : searchCustomerId;
		if (!searchCustomerId.equals("")) {
			if (!customerCtrl.searchCustomerById(Long.valueOf(searchCustomerId))) {
				customerSearchResult = "Customer with Id: " + searchCustomerId + " not found";
			}
		}
	}
	
	public String searchByContactId(String searchContactId) {
		searchContactId = (searchContactId == null) ? "" : searchContactId;
		if (!searchContactId.equals("")) {
			if (contactCtrl.searchContactById(Long.valueOf(searchContactId))) {
				return "/user/details.xhtml";
			} else {
				contactSearchResult = "Contact with Id: " + searchContactId + " not found";
				return "/user/index.xhtml";
			}
			
		} else {
			return "/user/index.xhtml";
		}
	}
	
	public String searchByContactIdInDetails(String searchContactId) {
		searchContactId = (searchContactId == null) ? "" : searchContactId;
		if (!searchContactId.equals("")) {
			if (contactCtrl.searchContactByIdInDetails(Long.valueOf(searchContactId))) {
				return "/user/details.xhtml";
			} else {
				contactSearchResult = "Contact with Id: " + searchContactId + " for Customer Id: " + customerCtrl.getCustomer().getCustomerId() + " not found";
				
				contactCtrl.getContacts().clear();
				return "/user/details.xhtml";
			}
			
		} else {
			
			return "/user/details.xhtml";
		}
	}

	public String getCustomerSearchResult() {
		return customerSearchResult;
	}

	public void setCustomerSearchResult(String customerSearchResult) {
		this.customerSearchResult = customerSearchResult;
	}

	public String getContactSearchResult() {
		return contactSearchResult;
	}

	public void setContactSearchResult(String contactSearchResult) {
		this.contactSearchResult = contactSearchResult;
	}
	
	
}
