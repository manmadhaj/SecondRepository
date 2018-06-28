package com.drucare.reports.controller;

import java.math.BigDecimal;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class MonthlyDataBean {

	private String indicatorName;
	private BigDecimal totalSummaryAmt;
	private BigDecimal totalSummaryAmt1;
	private BigDecimal totalSummaryAmt2;
	private String month;
	private BigDecimal amount;
	private JRBeanCollectionDataSource beanCollectionDataSource;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public JRBeanCollectionDataSource getBeanCollectionDataSource() {
		return beanCollectionDataSource;
	}

	public void setBeanCollectionDataSource(JRBeanCollectionDataSource beanCollectionDataSource) {
		this.beanCollectionDataSource = beanCollectionDataSource;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public BigDecimal getTotalSummaryAmt() {
		return totalSummaryAmt;
	}

	public void setTotalSummaryAmt(BigDecimal totalSummaryAmt) {
		this.totalSummaryAmt = totalSummaryAmt;
	}

	public BigDecimal getTotalSummaryAmt1() {
		return totalSummaryAmt1;
	}

	public void setTotalSummaryAmt1(BigDecimal totalSummaryAmt1) {
		this.totalSummaryAmt1 = totalSummaryAmt1;
	}

	public BigDecimal getTotalSummaryAmt2() {
		return totalSummaryAmt2;
	}

	public void setTotalSummaryAmt2(BigDecimal totalSummaryAmt2) {
		this.totalSummaryAmt2 = totalSummaryAmt2;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

}
