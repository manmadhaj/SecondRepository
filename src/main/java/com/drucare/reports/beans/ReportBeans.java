package com.drucare.reports.beans;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ReportBeans {

	
	
	@JsonProperty("displayColumnNm")
	private String displayColumnNm;
	
	@JsonProperty("report_id")
	private Integer report_id;
	
	@JsonProperty("reportInputOrder")
	private Integer reportInputOrder;
	
	@JsonProperty("columnNm")
	private String columnNm;
	
	@JsonProperty("columnDataType")
	private String columnDataType;
	
	@JsonProperty("orgId")
	private Integer orgId;
	
	@JsonProperty("createdUserId")
	private Long createdUserId;
	
	@JsonProperty("createdDttm")
	private Timestamp createdDttm;
	
	@JsonProperty("isActive")
	private Boolean isActive;
	
	@JsonProperty("validation")
	private String validation;
	
	@JsonProperty("validationRule")
	private String validationRule;
	
	 @JsonProperty("screenUrl")
		private String screenUrl;
	 

	public ReportBeans(String displayColumnNm, Integer report_id, Integer reportInputOrder, String columnNm,
			String columnDataType, Integer orgId, Long createdUserId, Timestamp createdDttm, Boolean isActive,
			String validation, String validationRule) {
		super();
		this.displayColumnNm = displayColumnNm;
		this.report_id = report_id;
		this.reportInputOrder = reportInputOrder;
		this.columnNm = columnNm;
		this.columnDataType = columnDataType;
		this.orgId = orgId;
		this.createdUserId = createdUserId;
		this.createdDttm = createdDttm;
		this.isActive = isActive;
		this.validation = validation;
		this.validationRule = validationRule;
	}

	public String getDisplayColumnNm() {
		return displayColumnNm;
	}

	public void setDisplayColumnNm(String displayColumnNm) {
		this.displayColumnNm = displayColumnNm;
	}

	public Integer getReport_id() {
		return report_id;
	}

	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}

	public Integer getReportInputOrder() {
		return reportInputOrder;
	}

	public void setReportInputOrder(Integer reportInputOrder) {
		this.reportInputOrder = reportInputOrder;
	}

	public String getColumnNm() {
		return columnNm;
	}

	public void setColumnNm(String columnNm) {
		this.columnNm = columnNm;
	}

	public String getColumnDataType() {
		return columnDataType;
	}

	public void setColumnDataType(String columnDataType) {
		this.columnDataType = columnDataType;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public Timestamp getCreatedDttm() {
		return createdDttm;
	}

	public void setCreatedDttm(Timestamp createdDttm) {
		this.createdDttm = createdDttm;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	public String getValidationRule() {
		return validationRule;
	}

	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
	}

	
	
	public String getScreenUrl() {
		return screenUrl;
	}

	public void setScreenUrl(String screenUrl) {
		this.screenUrl = screenUrl;
	}

	public ReportBeans() {
		
	}

	@Override
	public String toString() {
		return "ReportBeans [displayColumnNm=" + displayColumnNm + ", report_id=" + report_id + ", reportInputOrder="
				+ reportInputOrder + ", columnNm=" + columnNm + ", columnDataType=" + columnDataType + ", orgId="
				+ orgId + ", createdUserId=" + createdUserId + ", createdDttm=" + createdDttm + ", isActive=" + isActive
				+ ", validation=" + validation + ", validationRule=" + validationRule + "]";
	}


	
	
}
