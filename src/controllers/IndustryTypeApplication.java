package controllers;

import java.util.ArrayList;

import javax.el.ELContext;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import mbeans.IndustryTypeManagedBean;
import repository.entity.IndustryType;

@Named(value = "industryTypeApplication")
@ApplicationScoped
public class IndustryTypeApplication {
	@ManagedProperty(value = "#{industryTypeManagedBean}")
	private IndustryTypeManagedBean industryTypeManagedBean;
	
	private ArrayList<IndustryType> industryTypeList = new ArrayList<>();
	private ArrayList<String> industryTypeNameList = new ArrayList<>();
	
	public IndustryTypeApplication() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		industryTypeManagedBean = (IndustryTypeManagedBean) FacesContext.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "industryTypeManagedBean");
		updateIndustryTypeList();
	}
	
	public void updateIndustryTypeList() {
		industryTypeList.clear();
		industryTypeNameList.clear();
		for (IndustryType industryType : industryTypeManagedBean.getAllIndustryTypes()) {
			industryTypeList.add(industryType);
			industryTypeNameList.add(industryType.toString());
		}
		setIndustryTypeList(industryTypeList);
		setIndustryTypeNameList(industryTypeNameList);
	}

	public ArrayList<IndustryType> getIndustryTypeList() {
		return industryTypeList;
	}

	public void setIndustryTypeList(ArrayList<IndustryType> industryTypeList) {
		this.industryTypeList = industryTypeList;
	}

	public ArrayList<String> getIndustryTypeNameList() {
		return industryTypeNameList;
	}

	public void setIndustryTypeNameList(ArrayList<String> industryTypeNameList) {
		this.industryTypeNameList = industryTypeNameList;
	}
	
	
}
