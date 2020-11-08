package mbeans;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import dto.CustomerDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

import repository.ContactRepository;
import repository.CustomerRepository;
import repository.entity.Address;
import repository.entity.Contact;
import repository.entity.Customer;
import repository.entity.IndustryType;
import repository.entity.User;


@ManagedBean(name = "customerManagedBean")
@SessionScoped
public class CustomerManagedBean {
	@EJB
	private CustomerRepository customerRepository;
	
	@EJB
	private ContactRepository contactRepository;
	
	public List<Customer> getAllCustomers() {
		try {
            List<Customer> customers = customerRepository.findAllCustomers();
            return customers;
        } catch (Exception ex) {
            Logger.getLogger(CustomerRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
	
	public List<Customer> getCustomersByUser(User user) {
		try {
			List<Customer> customers = customerRepository.findCustomersByConnectUser(user);
			return customers;
		} catch (Exception ex) {
			Logger.getLogger(CustomerRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public Customer getCustomerById(long customerId) {
		try {
			Customer customer = customerRepository.findCustomerByCustomerId(customerId);
			return customer;
		} catch (Exception ex) {
			Logger.getLogger(CustomerRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	
	public void addCustomer(CustomerDTO customerDTO) {
		Customer customer =  convertCustomerToEntity(customerDTO);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		User user = (User) externalContext.getSessionMap().get("User");
		customer.setConnectUser(user);
		
		try {
            customerRepository.addCustomer(customer);
        } catch (Exception ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public Customer convertCustomerToEntity(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		
		customer.setCustomerName(customerDTO.getCustomerName());
		
		IndustryType industryType = checkIndustryType(customerDTO.getIndustryType());
		customer.setIndustryType(industryType );
		
		Address address = new Address();
		address.setStreetNumber(customerDTO.getStreetNumber());
		address.setStreetAddress(customerDTO.getStreetAddress());
		address.setSuburb(customerDTO.getSuburb());
		address.setState(customerDTO.getState().toUpperCase());
		address.setPostcode(customerDTO.getPostcode());
		
		customer.setAddress(address);
		
		return customer;
	}
	
	public IndustryType checkIndustryType(String industry) {
		IndustryType industryType = IndustryType.UNKNOWN;
		
		switch (industry) {
		case "Bank" :
			industryType = IndustryType.BANK;
			break;
		case "Building" :
			industryType = IndustryType.BUILDING;
			break;
		case "Data Communication" :
			industryType = IndustryType.DATACOMMUNICATION;
			break;
		case "Education" :
			industryType = IndustryType.EDUCATION;
			break;
		case "Farm" :
			industryType = IndustryType.FARM;
			break;
		case "Health" :
			industryType = IndustryType.HEALTH;
			break;
		case "Mining" :
			industryType = IndustryType.MINING;
			break;
		case "Publishing" :
			industryType = IndustryType.PUBLISHING;
			break;
		}
		return industryType;
	}
	
	public void removeCustomer(long customerId) {
		try {
			Customer customer = customerRepository.findCustomerByCustomerId(customerId);
			List<Contact> contacts = contactRepository.findContactsByCustomer(customer);
			for (Contact contact : contacts) {
				contactRepository.removeContact(contact.getContactId());
			}
            customerRepository.removeCustomer(customerId);
        } catch (Exception ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public CustomerDTO convertCustomerToDTO(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setCustomerId(customer.getCustomerId());
		customerDTO.setCustomerName(customer.getCustomerName());
		customerDTO.setIndustryType(customer.getIndustryType().toString());
		customerDTO.setStreetNumber(customer.getAddress().getStreetNumber());
		customerDTO.setStreetAddress(customer.getAddress().getStreetAddress());
		customerDTO.setSuburb(customer.getAddress().getSuburb());
		customerDTO.setState(customer.getAddress().getState());
		customerDTO.setPostcode(customer.getAddress().getPostcode());
		
		return customerDTO;
	}
	
	public void updateCustomer(CustomerDTO customerDTO) {
		try {
			Customer customer = convertCustomerToEntity(customerDTO);
			customer.setCustomerId(customerDTO.getCustomerId());
						
			customer.setConnectUser(customerDTO.getConnectUser());
			
			customerRepository.updateCustomer(customer);
		} catch (Exception ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void assignConnectUser(Customer customer) {
		try {
			customerRepository.updateCustomer(customer);
		} catch (Exception ex) {
			Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}
