package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import dto.ContactDTO;
import mbeans.ContactManagedBean;
import mbeans.CustomerManagedBean;
import repository.entity.Contact;
import repository.entity.Customer;

@RequestScoped
@Named("contactController")
public class ContactController {
	@ManagedProperty(value = "#{contactManagedBean}")
	ContactManagedBean contactManagedBean;
	
	private CustomerController customerCtrl;
	
	private ArrayList<Contact> contacts = new ArrayList<>();
	//private Customer customer;
	
	//private ContactApplication contactApp;
	
	//private long contactIndex;
	private Contact contact = new Contact();
	private ContactDTO contactDTO = new ContactDTO();
	private String searchContactId= null;

	public ContactController() throws Exception {
		
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		contactManagedBean = (ContactManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "contactManagedBean");
		
		customerCtrl = (CustomerController) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "customerController");
		
		//contactApp = (ContactApplication) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "contactApplication");
		//customer = contactApp.getCustomer();
		
		updateContactList(customerCtrl.getCustomer());
		
		String contactId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("contactId");
		
		if (contactId != null) {
			contact = contactManagedBean.getContactById(Long.valueOf(contactId));
			contactDTO = contactManagedBean.convertContactToDTO(contact);
		}
	}
	
	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

	public void updateContactList(Customer customer) {
		if (customer != null) {
			//this.customer = customer;
			contacts.clear();
			for (Contact contact : contactManagedBean.getContactsByCustomer(customer)) {
				contacts.add(contact);
			}
			setContacts(contacts);
		}
	}
	
	public boolean searchContactById(long contactId) {
		contact = contactManagedBean.getContactById(contactId);
		if (contact != null) {
			customerCtrl.setCustomer(contact.getCustomer());
			
			contacts.clear();
			contacts.add(contact);
			setContacts(contacts);
			return true;
		}
		
		return false;
	}
	
	public boolean searchContactByIdInDetails(long contactId) {
		contact = contactManagedBean.getContactById(contactId);
		if (contact != null) {
			if (contact.getCustomer().getCustomerId() == customerCtrl.getCustomer().getCustomerId()) {
								
				contacts.clear();
				contacts.add(contact);
				setContacts(contacts);
				return true;
			}
		}
		
		return false;
	}
	
	/*
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	*/

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public ContactDTO getContactDTO() {
		return contactDTO;
	}

	public void setContactDTO(ContactDTO contactDTO) {
		this.contactDTO = contactDTO;
	}

	public void removeContact(long contactId) {
		try {
			contactManagedBean.removeContact(contactId);
			updateContactList(customerCtrl.getCustomer());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contact has been deleted succesfully"));
			
		} catch (Exception ex) {
			
		}
	}
	
	public void addContact(ContactDTO contactDTO) {
		try {
			contactManagedBean.addContact(contactDTO, customerCtrl.getCustomer());
			updateContactList(customerCtrl.getCustomer());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contact has been added succesfully"));
			
		} catch (Exception ex) {
			
		}
	}
	
	/*
	public String loadAddContactPage(Customer customer) {
		this.customer = customer;
		return "addcontact.xhtml";
	}
	*/

	public void updateContact(ContactDTO contactDTO) {
		try {
			contactDTO.setContactId(contact.getContactId());
			contactManagedBean.updateContact(contactDTO, customerCtrl.getCustomer());
			updateContactList(customerCtrl.getCustomer());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Contact has been edited succesfully"));
			
		} catch (Exception ex) {
			
		}
	}

	public String getSearchContactId() {
		return searchContactId;
	}

	public void setSearchContactId(String searchContactId) {
		this.searchContactId = searchContactId;
	}
	
}
