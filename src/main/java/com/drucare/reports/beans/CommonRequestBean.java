package com.drucare.reports.beans;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CommonRequestBean {

	@ApiModelProperty(value = "organisation id", required = true)
	@NotNull
	private Integer orgId;

	@ApiModelProperty(value = "current login user id", required = true)
	@NotNull
	private Long createdUsrId;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Long getCreatedUsrId() {
		return createdUsrId;
	}

	public void setCreatedUsrId(Long createdUsrId) {
		this.createdUsrId = createdUsrId;
	}

	@Override
	public String toString() {
		return "CommonRequestBean [orgId=" + orgId + ", createdUsrId=" + createdUsrId + "]";
	}

}
