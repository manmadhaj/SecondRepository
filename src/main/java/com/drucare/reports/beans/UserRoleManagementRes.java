package com.drucare.reports.beans;

public class UserRoleManagementRes {
	private String empNm;
	private String modeNm;
	private String roleNm;
	private String screenNm;
	private String screenType;
	private Integer privilegeId;

	public String getEmpNm() {
		return empNm;
	}

	public void setEmpNm(String empNm) {
		this.empNm = empNm;
	}

	public String getModeNm() {
		return modeNm;
	}

	public void setModeNm(String modeNm) {
		this.modeNm = modeNm;
	}

	public String getRoleNm() {
		return roleNm;
	}

	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}

	public String getScreenNm() {
		return screenNm;
	}

	public void setScreenNm(String screenNm) {
		this.screenNm = screenNm;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}

	public Integer getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}

}
