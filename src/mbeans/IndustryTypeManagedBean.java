package mbeans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import repository.CustomerRepository;
import repository.IndustryTypeRepository;
import repository.UserRepository;
import repository.entity.Customer;
import repository.entity.IndustryType;
import repository.entity.User;

@ManagedBean(name = "industryTypeManagedBean")
@SessionScoped
public class IndustryTypeManagedBean {
	@EJB
	private IndustryTypeRepository industryTypeRepository;
	
	@EJB
	private CustomerRepository customerRepository;
	
	public List<IndustryType> getAllIndustryTypes() {
		try {
            List<IndustryType> industryTypeList = industryTypeRepository.findAllInsdustryTypes();
            return industryTypeList;
        } catch (Exception ex) {
            Logger.getLogger(IndustryTypeRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
	}
	
	public void addIndustryType(String industryTypeName) {
		IndustryType industryType = new IndustryType(industryTypeName);
		try {
			industryTypeRepository.addIndustryType(industryType);
        } catch (Exception ex) {
            Logger.getLogger(IndustryTypeRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void updateIndustryType(IndustryType industryType) {
		try {
			industryTypeRepository.updateIndustryType(industryType);;
		} catch (Exception ex) {
            Logger.getLogger(IndustryTypeManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public IndustryType getIndustryTypeByName(String industryTypeByName) {
		try {
			IndustryType industryType = industryTypeRepository.findIndustryTypeByName(industryTypeByName);
			return industryType;
		} catch (Exception ex) {
			Logger.getLogger(IndustryTypeRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	public IndustryType getIndustryTypeById(int industryTypeId) {
		try {
			IndustryType industryType = industryTypeRepository.findIndustryTypeById(industryTypeId);
			return industryType;
		} catch (Exception ex) {
			Logger.getLogger(IndustryTypeRepository.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
		
	}
	
	public void removeIndustryType(String industryTypeName) {
		try {
			IndustryType industryType = industryTypeRepository.findIndustryTypeByName(industryTypeName);
			List<Customer> customers = customerRepository.findCustomerByIndustryType(industryType);
			for (Customer customer : customers) {
				customer.setIndustryType(null);
				customerRepository.updateCustomer(customer);
			}
			
			industryTypeRepository.removeIndustryType(industryTypeName);
			
        } catch (Exception ex) {
            Logger.getLogger(IndustryTypeRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
