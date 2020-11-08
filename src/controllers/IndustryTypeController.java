package controllers;

import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.CustomerManagedBean;
import mbeans.IndustryTypeManagedBean;
import repository.entity.IndustryType;
import repository.entity.User;

@RequestScoped
@Named("industryTypeController")
public class IndustryTypeController {
	@ManagedProperty(value = "#{industryTypeManagedBean}")
	private IndustryTypeManagedBean industryTypeManagedBean;
	
	private IndustryTypeApplication industryTypeApplication;
	
	private CustomerController customerCtrl;
	
	private IndustryType industryType;
	private String newIndustryType;
	
	public IndustryTypeController() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		industryTypeManagedBean = (IndustryTypeManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "industryTypeManagedBean");
		industryTypeApplication = (IndustryTypeApplication) FacesContext.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "industryTypeApplication");
		customerCtrl = (CustomerController) FacesContext.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "customerController");
		
		String industryTypeId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("industryTypeId");
		
		if (industryTypeId != null) {
			industryType = industryTypeManagedBean.getIndustryTypeById(Integer.valueOf(industryTypeId));
			System.out.println("testname: "+industryType.getIndustryType());
		}
		
	}
	
	public void addIndustryType(String inputIndustryType) {
		try {
			industryTypeManagedBean.addIndustryType(inputIndustryType);
			industryTypeApplication.updateIndustryTypeList();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Industry type has been added succesfully"));

		} catch (Exception ex) {

		}
	}
	
	public void updateIndustryType(String inputIndustryType) {
		try {
			industryType.setIndustryType(inputIndustryType);
			System.out.println("testid: "+ industryType.getIndustryTypeId());
			industryTypeManagedBean.updateIndustryType(industryType);
			industryTypeApplication.updateIndustryTypeList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Industry type has been updated succesfully"));
			
		} catch (Exception ex) {
			
		}
	}
	
	public String confirmRemoveIndustryType(String inputIndustryType) {
		IndustryType industryType = industryTypeManagedBean.getIndustryTypeByName(inputIndustryType);
		customerCtrl.searchCustomerByIndustry(industryType);
		return "/admin/confirmremoveindustry.xhtml";
	}
	
	public String removeIndustryType(String industryTypeName) {
		try {
			industryTypeManagedBean.removeIndustryType(industryTypeName);
			industryTypeApplication.updateIndustryTypeList();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Industry type has been deleted succesfully"));
			return "/admin/industrylist.xhtml";
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Industry type cannot be deleted"));
			return "/admin/industrylist.xhtml";
		} 
	}

	public IndustryType getIndustryType() {
		return industryType;
	}

	public void setIndustryType(IndustryType industryType) {
		this.industryType = industryType;
	}

	public String getNewIndustryType() {
		return newIndustryType;
	}

	public void setNewIndustryType(String newIndustryType) {
		this.newIndustryType = newIndustryType;
	}
	
}
