package mbeans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import repository.CustomerRepository;
import repository.UserRepository;
import repository.entity.Contact;
import repository.entity.Customer;
import repository.entity.Group;
import repository.entity.User;

@ManagedBean(name = "userManagedBean")
@SessionScoped
public class UserManagedBean {
	@EJB
	private UserRepository userRepository;
	
	@EJB
	private CustomerRepository customerRepository;
	
	public List<User> getAllUsers() {
		try {
            List<User> users = userRepository.findAllUsers();
            return users;
        } catch (Exception ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
	
	public User getUserByUsername(String username) {
		try {
			User user = userRepository.findUserByUsername(username);
			return user;
		} catch (Exception ex) {
			Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public String getUserGroup(String username) {
		try {
			String userGroup = userRepository.findUserGroup(username);
			return userGroup;
		} catch (Exception ex) {
			Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public void changeUserGroup(User user) {
		try {
			userRepository.changeUserGroup(user);
		} catch (Exception ex) {
			Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void removeUser(String username) {
		try {
			User user = userRepository.findUserByUsername(username);
			List<Customer> customers = customerRepository.findCustomersByConnectUser(user);
			for (Customer customer : customers) {
				customer.setConnectUser(null);
				customerRepository.updateCustomer(customer);
			}
			
			userRepository.removeUserGroup(user);
			
            userRepository.removeUser(username);
        } catch (Exception ex) {
            Logger.getLogger(CustomerManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
 }
