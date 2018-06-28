package com.drucare.reports.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drucare.core.util.AppServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.drucare.reports.controller.MonthlyDataBean;
import com.drucare.reports.controller.MonthlyDataRowmapper;

@Repository
public class CommonReportDaoImpl implements CommonReportDao{

	private static final Logger logger = LoggerFactory.getLogger(CommonReportDaoImpl.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MonthlyDataBean> fetchMonthlyData(String month1, String month2,
			String month3, Integer fromMonth1, Integer toMonth1,
			Integer fromYear1, Integer toYear1, String selectedCategeory,
			Integer orgId1) throws AppServiceException {
		List<MonthlyDataBean> monthlyDataList = new ArrayList<MonthlyDataBean>();
		Set processedData = new HashSet<MonthlyDataBean>();
		try{
		String query ="select indicator_nm,month,total_summary from quality.quality_indicators_monthly_summary_trans "
				+ "where total_summary is not null and org_id="+orgId1+" and summary_dt between '"+fromYear1+"-"+fromMonth1+"-"+01+"' and '"+toYear1+"-"+toMonth1+"-"+28+"'";
		if("NABH".equalsIgnoreCase(selectedCategeory)){
			query += " and indicator_id in(select indicator_no from quality.indicators_info_ref where is_nabh_indicator >0)";
		}else if("DEPT".equalsIgnoreCase(selectedCategeory)){
			query += " and indicator_id not in(select indicator_no from quality.indicators_info_ref where is_nabh_indicator > 0)";
		}		
		List<MonthlyDataBean> listMontlydata = jdbcTemplate.query(query,

				new MonthlyDataRowmapper() {
					@Override
					public MonthlyDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {

						MonthlyDataBean dataBean = (MonthlyDataBean) super.mapRow(rs, rowNum);
						dataBean.setIndicatorName(rs.getString(INDICATOR_NM));
						dataBean.setMonth(rs.getString(MONTH));
						dataBean.setAmount(rs.getBigDecimal(TOTAL_SUMMARY));

						return dataBean;
					}
				});
		logger.info("listMontlydata:::"+listMontlydata.size());
		if(listMontlydata.size()>0){
			for (MonthlyDataBean monthlyDataBean : listMontlydata) {
				MonthlyDataBean mDataBean = new MonthlyDataBean();			
				if(!processedData.contains(monthlyDataBean.getIndicatorName())){
					processedData.add(monthlyDataBean.getIndicatorName());						
				String indicatorName = monthlyDataBean.getIndicatorName();			
				mDataBean.setIndicatorName(indicatorName);
				mDataBean.setTotalSummaryAmt(getAmountByMonth(listMontlydata,indicatorName,month1));
				mDataBean.setTotalSummaryAmt1(getAmountByMonth(listMontlydata,indicatorName,month2));
				mDataBean.setTotalSummaryAmt2(getAmountByMonth(listMontlydata,indicatorName,month3));
				}		
				monthlyDataList.add(mDataBean);
			}
			logger.info("monthlyDataList:::"+monthlyDataList.size());
		}
		}catch (DataAccessException e) {
			logger.info("Exception while fetching monthly data report--------" + e.getMessage());
			throw new AppServiceException(e);

		} catch (Exception e) {
			logger.info("Exception while fetching monthly data report--------" + e.getMessage());
			throw new AppServiceException(e);

		}
		return monthlyDataList;
	}

	
	public BigDecimal getAmountByMonth(List<MonthlyDataBean> monthlyDataList,String indicatorName,String month){
		BigDecimal finalAmount = null;
		for(MonthlyDataBean mBean : monthlyDataList){
			if(indicatorName.equals(mBean.getIndicatorName()) && month.equals(new DateFormatSymbols().getMonths()[(Integer.parseInt(mBean.getMonth())-1)])){
				finalAmount = mBean.getAmount();
				break;
			}
		}
		
		return finalAmount;
	}
}
