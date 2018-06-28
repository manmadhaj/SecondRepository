package com.drucare.reports.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MonthlyDataRowmapper implements RowMapper{

	public static final String INDICATOR_NM ="INDICATOR_NM";
	public static final String MONTH="MONTH";
	public static final String TOTAL_SUMMARY="TOTAL_SUMMARY";
	
	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		MonthlyDataBean monthlyDataBean = new MonthlyDataBean();
		monthlyDataBean.setIndicatorName(rs.getString(INDICATOR_NM));
		monthlyDataBean.setMonth(rs.getString(MONTH));
		monthlyDataBean.setAmount(rs.getBigDecimal(TOTAL_SUMMARY));
		return monthlyDataBean;
	}

}
