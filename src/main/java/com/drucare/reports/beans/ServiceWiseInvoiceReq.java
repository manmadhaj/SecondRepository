package com.drucare.reports.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ServiceWiseInvoiceReq extends FromAndToDateBean{

	@ApiModelProperty("this value is optional")
	private Long serviceId;

	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	

}
