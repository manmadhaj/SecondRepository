package com.drucare.reports.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportUtil {

	public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	public static final String MEDIA_TYPE_PDF = "application/pdf";
	public static final String EXTENSION_TYPE_EXCEL = "xls";
	public static final String EXTENSION_TYPE_PDF = "pdf";
	public static final String CLASS_NAME = ReportUtil.class.getName();
	private static final Logger logger = LoggerFactory.getLogger(ReportUtil.class);
	public static HttpServletResponse export(String type,
			JasperPrint jasperPrint, HttpServletResponse response,
			ByteArrayOutputStream byteArrayOutputStream, String reportName) {

		String methodName = "export";
		logger.info("Starting report export::" + CLASS_NAME + ","
				+ methodName);
		if (type.equalsIgnoreCase(EXTENSION_TYPE_EXCEL)) {
			// Export to output stream
			exportXls(jasperPrint, byteArrayOutputStream);
			// Set contentType
			response.setContentType(MEDIA_TYPE_EXCEL);
			response.setContentLengthLong(byteArrayOutputStream.size());
			// Set out response properties
			response.setHeader("Expirse:", "0"); // eliminates browser caching
			response.setHeader("Cache-Control",
					"no-cache,no-store,must-revalidate"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ reportName + ".xls");
			return response;
		}
		if (type.equalsIgnoreCase(EXTENSION_TYPE_PDF)) {
			// Export to output stream
			exportPdf(jasperPrint, byteArrayOutputStream);
			// Set contentType
			response.setContentType(MEDIA_TYPE_PDF);
			response.setContentLengthLong(byteArrayOutputStream.size());
			// Set out response properties
			response.setHeader("Expirse:", "0"); // eliminates browser caching
			response.setHeader("Cache-Control",
					"no-cache,no-store,must-revalidate"); // HTTP 1.1
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Content-Disposition", "inline;filename="
					+ reportName + ".pdf");
			return response;
		}
		logger.info("Ending report export::" + CLASS_NAME + ","
				+ methodName);
		throw new RuntimeException("No type set for type" + type);
	}

	public static void exportXls(JasperPrint jasperPrint,
			ByteArrayOutputStream baos) {
		String methodName = "exportXls";
		logger.info("Starting report exportXls::" + CLASS_NAME + ","
				+ methodName);
		if (baos != null)
			logger.info("Checing ByteArrayOutputStream size ::"
					+ baos.size() + CLASS_NAME + "," + methodName);
		// Create a JRXlsExporter instance
		JRXlsExporter jrXlsExporter = new JRXlsExporter();
		// Here we assign the parameters js and baos to the exporter
		/*
		 * jrXlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,
		 * jasperPrint);
		 * jrXlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		 */
		jrXlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		jrXlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
				baos));
		SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setDetectCellType(true);
		configuration.setCollapseRowSpan(false);
		jrXlsExporter.setConfiguration(configuration);
		try {
			jrXlsExporter.exportReport();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.info("Ending report exportXls::" + CLASS_NAME + ","
				+ methodName);
	}

	public static void exportPdf(JasperPrint jasperPrint,
			ByteArrayOutputStream baos) {
		String methodName = "exportPdf";
		logger.info("Starting report exportPdf::" + CLASS_NAME + ","
				+ methodName);
		// Create a JRPdfExporter instance
		JRPdfExporter jrPdfExporter = new JRPdfExporter();
		/*
		 * jrPdfExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT,
		 * jasperPrint);
		 * jrPdfExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,
		 * baos);
		 */
		jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
				baos));
		try {
			jrPdfExporter.exportReport();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.info("Ending report exportPdf::" + CLASS_NAME + ","
				+ methodName);
	}
	
	public static HttpServletResponse  exportToReportData(HttpServletRequest request,HttpServletResponse response,Connection connection,InputStream inputStream,Map parameters,String type,String reportName){
		String methodName = "exportToReportData";
		System.out.println("Starting report exportToReportData::" + CLASS_NAME + ","
				+ methodName);
		OutputStream outputStream = null;
		try{
		//InputStream inputStream = new FileInputStream(jrxmlPath);
		//Convert template to JasperDesign
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		//compile design to JasperReport
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		//Create the JasperPrint object
		// Make sure to pass the JasperReport,report parameters, data source
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,connection);
		//Create an output byte stream where data will be written
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//Export Report
		export(type, jasperPrint, response, byteArrayOutputStream, reportName);
		outputStream = response.getOutputStream();
		//write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		//flush the stream
		outputStream.flush();
		response.flushBuffer();
		}catch(Exception ex){
			logger.error("Exception occured in exportToReportData.."+ex);
		}finally{
			try{
			if(outputStream != null){
				outputStream.close();
			}
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		logger.info("Ending report exportToReportData::" + CLASS_NAME + ","
				+ methodName);
		return response;
	}
	
	
	
	public static HttpServletResponse  exportToReportBeanData(HttpServletRequest request,HttpServletResponse response,JRBeanCollectionDataSource jcon,InputStream inputStream,Map parameters,String type,String reportName){
		String methodName = "exportToReportData";
		System.out.println("Starting report exportToReportData::" + CLASS_NAME + ","
				+ methodName);
		OutputStream outputStream = null;
		try{
		//InputStream inputStream = new FileInputStream(jrxmlPath);
		//Convert template to JasperDesign
		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		//compile design to JasperReport
		JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
		//Create the JasperPrint object
		// Make sure to pass the JasperReport,report parameters, data source
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,jcon);
		//Create an output byte stream where data will be written
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//Export Report
		export(type, jasperPrint, response, byteArrayOutputStream, reportName);
		outputStream = response.getOutputStream();
		//write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		//flush the stream
		outputStream.flush();
		response.flushBuffer();
		}catch(Exception ex){
			logger.error("Exception occured in exportToReportData.."+ex);
		}finally{
			try{
			if(outputStream != null){
				outputStream.close();
			}
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		logger.info("Ending report exportToReportData::" + CLASS_NAME + ","
				+ methodName);
		return response;
	}
}
