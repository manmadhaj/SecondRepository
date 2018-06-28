package com.drucare.reports.beans;

public class ModuleBeans {
	
	
	public Integer moduleId;
	public String moduleName;
	public ModuleBeans(Integer moduleId, String moduleName) {
		super();
		this.moduleId = moduleId;
		this.moduleName = moduleName;
	}
	
	public ModuleBeans() {
		
	}

	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	

}
