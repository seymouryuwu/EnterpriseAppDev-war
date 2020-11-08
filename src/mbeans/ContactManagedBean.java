package mbeans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dto.ContactDTO;
import dto.CustomerDTO;
import repository.ContactRepository;
import repository.CustomerRepository;
import repository.entity.Address;
import repository.entity.Contact;
import repository.entity.Customer;
import repository.entity.IndustryType;
import repository.staticValue.Title;

@ManagedBean(name = "contactManagedBean")
@SessionScoped
public class ContactManagedBean {
	@EJB
	private ContactRepository contactRepository;
	
	public List<Contact> getContactsByCustomer(Customer customer) {
		try {
            List<Contact> contacts = contactRepository.findContactsByCustomer(customer);
            return contacts;
        } catch (Exception ex) {
            Logger.getLogger(CustomerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
	
	public Contact getContactById(long contactId) {
		try {
			Contact contact = contactRepository.findContactByContactId(contactId);
			return contact;
		} catch (Exception ex) {
            Logger.getLogger(CustomerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
	
	public void removeContact(long contactId) {
		try {
            contactRepository.removeContact(contactId);
        } catch (Exception ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void addContact(ContactDTO contactDTO, Customer customer) {
		Contact contact =  convertContactToEntity(contactDTO);
		contact.setCustomer(customer);
		try {
			contactRepository.addContact(contact);
        } catch (Exception ex) {
            Logger.getLogger(ContactManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public Contact convertContactToEntity(ContactDTO contactDTO) {
		Contact contact = new Contact();
		
		contact.setContactName(contactDTO.getContactName());
		
		Title title = checkTitle(contactDTO.getContactTitle());
		contact.setTitle(title);
		
		contact.setContactPhoneNumber(contactDTO.getContactPhoneNumber());
		contact.setContactEmail(contactDTO.getContactEmail());
		
		return contact;
	}
	
	public Title checkTitle(String inputTitle) {
		Title title = Title.UNKNOWN;
		
		switch (inputTitle) {
		case "Mr" :
			title = Title.MR;
			break;
		case "Miss" :
			title = Title.MISS;
			break;
		case "Mrs" :
			title = Title.MRS;
			break;
		case "Ms" :
			title = Title.MS;
			break;
		}
		
		return title;
	}
	
	public ContactDTO convertContactToDTO(Contact contact) {
		ContactDTO contactDTO =  new ContactDTO();
		contactDTO.setContactId(contact.getContactId());
		contactDTO.setContactName(contact.getContactName());
		contactDTO.setContactTitle(contact.getTitle().toString());
		contactDTO.setContactPhoneNumber(contact.getContactPhoneNumber());
		contactDTO.setContactEmail(contact.getContactEmail());
		
		return contactDTO;
	}
	
	public void updateContact(ContactDTO contactDTO, Customer customer) {
		Contact contact = convertContactToEntity(contactDTO);
		contact.setContactId(contactDTO.getContactId());
		contact.setCustomer(customer);
		try {
			contactRepository.updateContact(contact);
        } catch (Exception ex) {
            Logger.getLogger(ContactManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
