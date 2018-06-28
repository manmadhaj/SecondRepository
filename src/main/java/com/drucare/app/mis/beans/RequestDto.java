package com.drucare.app.mis.beans;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class RequestDto {

	@ApiModelProperty(value = "organisation id", required = true)
	@NotNull
	private Integer orgId;

	@ApiModelProperty(value = "current login user id", required = true)
	@NotNull
	private Long authenticatedUserId;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	

	public Long getAuthenticatedUserId() {
		return authenticatedUserId;
	}

	public void setAuthenticatedUserId(Long authenticatedUserId) {
		this.authenticatedUserId = authenticatedUserId;
	}

	@Override
	public String toString() {
		return "CommonRequestBean [orgId=" + orgId + ", authenticatedUserId=" + authenticatedUserId + "]";
	}

	
	
	
	
	

}
