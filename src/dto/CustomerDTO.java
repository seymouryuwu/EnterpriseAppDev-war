package dto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import repository.entity.Address;
import repository.entity.Contact;
import repository.entity.IndustryType;
import repository.entity.User;

//@RequestScoped
//@Named(value = "customer")
public class CustomerDTO {
	private long customerId;
	private String customerName;
	private String streetNumber;
    private String streetAddress;
    private String suburb;
    private String postcode;
    private String state;
	private User connectUser;
	private String industryType;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public User getConnectUser() {
		return connectUser;
	}

	public void setConnectUser(User connectUser) {
		this.connectUser = connectUser;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	
	
	public void setCustomerDTO(CustomerDTO customerDTO) {
		customerId = customerDTO.getCustomerId();
		customerName = customerDTO.getCustomerName();
		streetNumber = customerDTO.getStreetNumber();
	    streetAddress = customerDTO.getStreetAddress();
	    suburb = customerDTO.getSuburb();
	    postcode = customerDTO.getPostcode();
	    state = customerDTO.getState();
		//connectUser = customerDTO;
		industryType = customerDTO.getIndustryType();
	}
	

}
