package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.ContactManagedBean;
import mbeans.CustomerManagedBean;
import repository.entity.Contact;
import repository.entity.Customer;

@Named(value = "contactApplication")
@ApplicationScoped
public class ContactApplication {
	@ManagedProperty(value = "#{contactManagedBean}")
	private ContactManagedBean contactManagedBean;
	
	private CustomerApplication customerApp;

	private long customerIndex;
	private Customer customer;
	private ArrayList<Contact> contacts;

	public ContactApplication() throws Exception {
		contacts = new ArrayList<>();

		// instantiate customerManagedBean
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		contactManagedBean = (ContactManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "contactManagedBean");
		customerApp = (CustomerApplication) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "customerApplication");
		
		String parameterValue = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("customerIndex");
		if (parameterValue != null) {
			customerIndex = Long.valueOf(parameterValue);
	        customer = getCustomer();
	        updateContactList(customer);
		}
	}

	public Customer getCustomer() {
        if (customer == null) {
            return customerApp.getCustomers().get((int) customerIndex);
        }
        return customer;
    }
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	public void updateContactList(Customer customer) {
		if (customer != null) {
			this.customer = customer;
			contacts.clear();
			for (Contact contact : contactManagedBean.getContactsByCustomer(customer)) {
				contacts.add(contact);
			}
			setContacts(contacts);
		}
	}
}
