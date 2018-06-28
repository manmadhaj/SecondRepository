package com.drucare.reports.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.drucare.reports.beans.InvoiceRequestBean;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

/**
 * This Class is heart of the PDF generation. Whole logic is hear like creating
 * PDF object,Creating PieChart image ,Inserting PieChart Image into PDF object
 * and sending PDF object to HttpServletResponse OutputStream object with proper
 * headers
 * 
 * @author Srinivas Nangana
 * @version 1.0
 */
@Component
public class InvoiceReportUtill {

	public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	public static final String EXTENSION_TYPE_EXCEL = "xlsx";
	public static final String MEDIA_TYPE_PDF = "application/pdf";
	public static final String EXTENSION_TYPE_PDF = "pdf";
	public static final int PDF_FONT_SIZE_FOR_TABLE_DATA = 10;
	public static final int PDF_FONT_SIZE_FOR_TABLE_HEADING = 12;
	public static final int PDF_FONT_SIZE_FOR_TABLE_FILTER = 11;
	public static final int pieChartWidth = 470;
	public static final int pieChartHeight = 340;
	public static final String CLASS_NAME = ReportUtil.class.getName();
	private static final String AUTHOR = "SRINIVAS";

	static String FONT_STYLE_PIECHART_NAME;

	/*
	 * Spring will not allow static field injection so I am using below non
	 * static method for injecting this static field FONT_STYLE_PIECHART_NAME
	 */
	@Value("${FONT_STYLE_PIECHART_NAME}")
	public void setPrivateName(String FONT_STYLE_PIECHART_NAME) {
		InvoiceReportUtill.FONT_STYLE_PIECHART_NAME = FONT_STYLE_PIECHART_NAME;
	}

	private static final Logger logger = LoggerFactory.getLogger(InvoiceReportUtill.class);

	public static HttpServletResponse export(XSSFWorkbook workbook, HttpServletResponse response,
			ByteArrayOutputStream byteArrayOutputStream, String reportName) throws IOException {

		// Export to output stream
		workbook.write(byteArrayOutputStream);
		// Set contentType
		response.setContentType(MEDIA_TYPE_EXCEL);
		response.setContentLengthLong(byteArrayOutputStream.size());
		// Set out response properties
		response.setHeader("Expirse:", "0"); // eliminates browser caching
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate"); // HTTP
																					// 1.1
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Content-Disposition", "attachment;filename=" + reportName + ".xlsx");
		response.setHeader("X-File-Name", reportName);
		response.setHeader("Access-Control-Expose-Headers", "X-File-Name");
		return response;
	}

	/*
	 * This methods takes invoice data object which is contains Data of invoice
	 * reportname is name of the Excel Document
	 */
	public static HttpServletResponse exportToInvoiceReportData(HttpServletResponse response,
			List<Map<String, Object>> invoiceData, String reportName) throws IOException {

		logger.info("Enteted into This Method exportToInvoiceReportData stsrted Excel Creation Records:"
				+ invoiceData.size());

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Invoice Sheet");

		// Create row object
		XSSFRow row;

		// Excel sheet Headings
		Map<String, String> sheetHeadings = new LinkedHashMap<>();
		sheetHeadings.put("1", "S No");
		sheetHeadings.put("2", "Invoice Date");
		sheetHeadings.put("3", "Invoice Number");
		sheetHeadings.put("4", "Hospital Patient Id");
		sheetHeadings.put("5", "Patient Name");
		sheetHeadings.put("6", "Patient Category");
		sheetHeadings.put("7", "Dr MIC NO & Name");
		sheetHeadings.put("8", "Service Name");
		sheetHeadings.put("9", "Bill From");
		sheetHeadings.put("10", "Bill Status");
		sheetHeadings.put("11", "Created by");
		sheetHeadings.put("12", "Cancel Date");
		sheetHeadings.put("13", "Cancel Reason");
		sheetHeadings.put("14", "Cancel By");
		sheetHeadings.put("15", "Payment Mode");
		sheetHeadings.put("16", "Service Amount");
		sheetHeadings.put("17", "Discount Amount");
		sheetHeadings.put("18", "Total Tax");
		sheetHeadings.put("19", "Invoice Amount");

		// For adding Headings on first row

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		int cellid = 0;
		for (Map.Entry<String, String> entry : sheetHeadings.entrySet()) {

			Cell cell = row.createCell(cellid++);
			cell.setCellValue(entry.getValue());
		}

		List<Map<String, Object>> excelDataWithSNo = new ArrayList<>();
		// Adding S.No
		int sno = 1;
		for (Map<String, Object> obj : invoiceData) {
			Map<String, Object> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, Object> entry : obj.entrySet()) {

				if (flag) {
					mapWithSno.put("S No", sno++);
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			excelDataWithSNo.add(mapWithSno);
		}
		// Adds The Data to Excel Sheet
		for (int i = 0; i < excelDataWithSNo.size(); i++) {
			row = spreadsheet.createRow(rowid++);
			String drMicAndName = "";
			int position = 0;
			int cellid1 = 0;
			for (Map.Entry<String, Object> entry1 : excelDataWithSNo.get(i).entrySet()) {
				// Merging Dr Name&Mic Number in to single column
				if (position == 6 || position == 7) { //
					drMicAndName = drMicAndName + entry1.getValue() == null ? "" : entry1.getValue() + "";
					if (position == 7) {
						Cell cell = row.createCell(cellid1++);
						cell.setCellValue(drMicAndName);
					}
				} else {
					Cell cell = row.createCell(cellid1++);
					cell.setCellValue(entry1.getValue() == null ? "" : entry1.getValue() + "");
				}
				position++;
			}
		}
		autoSizeColumns(workbook);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// Export Report
		export(workbook, response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}
	/*
	 * This Method Returns the InputStream of Excel Document
	 */

	public static InputStream exportToInvoiceReportForMailSending(List<Map<String, Object>> invoiceData)
			throws IOException {

		logger.info("Enteted into This Method exportToInvoiceReportForMailSending started Excel Creation Records:"
				+ invoiceData.size());
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Invoice Creation");

		// Create row object
		XSSFRow row;

		// Excel sheet Headings
		Map<String, String> sheetHeadings = new LinkedHashMap<>();
		sheetHeadings.put("1", "S No");
		sheetHeadings.put("2", "Invoice Date");
		sheetHeadings.put("3", "Invoice Number");
		sheetHeadings.put("4", "Hospital Patient Id");
		sheetHeadings.put("5", "Patient Name");
		sheetHeadings.put("6", "Patient Category");
		sheetHeadings.put("7", "Dr MIC NO & Name");
		sheetHeadings.put("8", "Service Name");
		sheetHeadings.put("9", "Bill From");
		sheetHeadings.put("10", "Bill Status");
		sheetHeadings.put("11", "Created by");
		sheetHeadings.put("12", "Cancel Date");
		sheetHeadings.put("13", "Cancel Reason");
		sheetHeadings.put("14", "Cancel By");
		sheetHeadings.put("15", "Payment Mode");
		sheetHeadings.put("16", "Service Amount");
		sheetHeadings.put("17", "Discount Amount");
		sheetHeadings.put("18", "Total Tax");
		sheetHeadings.put("19", "Invoice Amount");

		// For adding Headings on first row

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		int cellid = 0;
		for (Map.Entry<String, String> entry : sheetHeadings.entrySet()) {

			Cell cell = row.createCell(cellid++);
			cell.setCellValue(entry.getValue());
		}

		List<Map<String, Object>> excelDataWithSNo = new ArrayList<>();
		// Adding S.No
		int sno = 1;
		for (Map<String, Object> obj : invoiceData) {
			Map<String, Object> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, Object> entry : obj.entrySet()) {

				if (flag) {
					mapWithSno.put("S No", sno++);
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			excelDataWithSNo.add(mapWithSno);
		}
		// Adds The Data to Excel Sheet
		for (int i = 0; i < excelDataWithSNo.size(); i++) {
			row = spreadsheet.createRow(rowid++);
			String drMicAndName = "";
			int position = 0;
			int cellid1 = 0;
			for (Map.Entry<String, Object> entry1 : excelDataWithSNo.get(i).entrySet()) {
				// Merging Dr Name&Mic Number in to single column
				if (position == 6 || position == 7) {
					drMicAndName = drMicAndName + entry1.getValue() == null ? "" : entry1.getValue() + "";
					if (position == 7) {
						Cell cell = row.createCell(cellid1++);
						cell.setCellValue(drMicAndName);
					}
				} else {
					Cell cell = row.createCell(cellid1++);
					cell.setCellValue(entry1.getValue() == null ? "" : entry1.getValue() + "");
				}
				position++;
			}
		}
		// spreadsheet.autoSizeColumn(10000000);
		autoSizeColumns(workbook);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);

		byte[] buffer = byteArrayOutputStream.toByteArray();
		InputStream inputStreamOfExcel = new ByteArrayInputStream(buffer);
		try {
			byteArrayOutputStream.close();
			workbook.close();
		} catch (Exception e) {
			logger.error("Excel Sheet Creation Failed Due TO:" + e);
		}
		logger.info("Excel Sheet inputStream Completed");
		return inputStreamOfExcel;
	}

	/**
	 * Auto increments the column size based on data
	 */
	public static void autoSizeColumns(XSSFWorkbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				XSSFRow row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	/************************************************
	 * PDF RELATED
	 ***********************************************/
	/////////////////////////////////////////////////////////////////////////////////////
	public static HttpServletResponse settingPdfResponseHeaders(HttpServletResponse response,
			ByteArrayOutputStream byteArrayOutputStream, String reportName) throws IOException {

		// Set contentType
		response.setContentType(MEDIA_TYPE_PDF);
		response.setContentLengthLong(byteArrayOutputStream.size());
		// Set out response properties
		response.setHeader("Expirse:", "0"); // eliminates browser caching
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate"); // HTTP
																					// 1.1
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Content-Disposition", "attachment;filename=" + reportName + ".pdf");
		response.setHeader("X-File-Name", reportName);
		response.setHeader("Access-Control-Expose-Headers", "X-File-Name");
		return response;
	}

	// Inner class for page number generation
	public static class Rotate extends PdfPageEventHelper {

		PdfTemplate total;

		/*
		 * Creates the PdfTemplate that will hold the total number of pages.
		 */
		public void onOpenDocument(PdfWriter writer, Document document) {
			// This is For A4 or PORTRAIT
			// total = writer.getDirectContent().createTemplate(30, 12);//This
			// This is for LANDSCAPE Rotation
			total = writer.getDirectContent().createTemplate(432, 252);
		}

		public void onEndPage(PdfWriter writer, Document document) {
			document.addAuthor(AUTHOR);
			PdfPTable table = new PdfPTable(2);
			try {

				table.setWidths(new int[] { 48, 2 });
				table.setTotalWidth(527);
				table.setLockedWidth(true);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				PdfPCell c1 = new PdfPCell(new Phrase(String.format("Page %d ", writer.getPageNumber()),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 8)));
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				table.addCell(c1);
				c1 = new PdfPCell(Image.getInstance(total));
				c1.setBorder(0);
				table.addCell(c1);
				table.writeSelectedRows(0, -1, 300, 23, writer.getDirectContent()); // PDF
																					// Page
																					// Number
																					// Position
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

	}

	/**
	 * This Method is designed to create PDF with adjusting one last filed left
	 * to right side for First PDF only. Second PDF alignment chose suitable
	 * Second method as per requirement Note:'by default all fields data align
	 * to left side only
	 * 
	 * 
	 * @author Srinivas Nangana
	 * @param response
	 * @param invoiceData
	 * @param reportName
	 * @param invoiceRequestBean
	 * @param columnWidths
	 * @param invoiceHeadings
	 * @param reportHeading
	 * @param departmentTotalslist
	 * @param departmentTotalsTableHeadings
	 * @param secondTableWidths
	 * @param secondTableMethodName
	 * @param combinationOfHeadingsAndValues
	 * @return HttpServletResponse
	 * @throws IOException
	 * @throws DocumentException
	 * @since 1.0
	 */
	public static HttpServletResponse writeInviceDataPdfToResponseGenericOne(HttpServletResponse response,
			List<Map<String, String>> invoiceData, String reportName, InvoiceRequestBean invoiceRequestBean,
			float[] columnWidths, LinkedHashMap<String, String> invoiceHeadings, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// createPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);
		createPdfGenericOne(byteArrayOutputStream, invoiceData, invoiceHeadings, invoiceRequestBean, columnWidths,
				reportHeading, departmentTotalslist, departmentTotalsTableHeadings, secondTableWidths,
				secondTableMethodName, combinationOfHeadingsAndValues);
		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	public static HttpServletResponse writeInviceDataPdfToResponseGenericTwo(HttpServletResponse response,
			List<Map<String, String>> invoiceData, String reportName, InvoiceRequestBean invoiceRequestBean,
			float[] columnWidths, LinkedHashMap<String, String> invoiceHeadings, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// createPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);
		createPdfGenericTwo(byteArrayOutputStream, invoiceData, invoiceHeadings, invoiceRequestBean, columnWidths,
				reportHeading, departmentTotalslist, departmentTotalsTableHeadings, secondTableWidths,
				secondTableMethodName, combinationOfHeadingsAndValues);
		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	public static HttpServletResponse writeInviceDataPdfToResponseGenericThree(HttpServletResponse response,
			List<Map<String, String>> invoiceData, String reportName, InvoiceRequestBean invoiceRequestBean,
			float[] columnWidths, LinkedHashMap<String, String> invoiceHeadings, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		createPdfGenericThree(byteArrayOutputStream, invoiceData, invoiceHeadings, invoiceRequestBean, columnWidths,
				reportHeading, departmentTotalslist, departmentTotalsTableHeadings, secondTableWidths,
				secondTableMethodName, combinationOfHeadingsAndValues);
		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	/**
	 * This Method is designed to create PDF without adjusting any filed for
	 * First PDF only. Second PDF alignment chose suitable Second method as per
	 * requirement Note:'by default all fields data align to left side only
	 * 
	 * 
	 * @author Srinivas Nangana
	 * @param response
	 * @param invoiceData
	 * @param reportName
	 * @param invoiceRequestBean
	 * @param columnWidths
	 * @param invoiceHeadings
	 * @param reportHeading
	 * @param departmentTotalslist
	 * @param departmentTotalsTableHeadings
	 * @param secondTableWidths
	 * @param secondTableMethodName
	 * @param combinationOfHeadingsAndValues
	 * @return HttpServletResponse
	 * @throws IOException
	 * @throws DocumentException
	 * @since 1.0
	 */
	public static HttpServletResponse writeInviceDataPdfToResponseGenericFoure(HttpServletResponse response,
			List<Map<String, String>> invoiceData, String reportName, InvoiceRequestBean invoiceRequestBean,
			float[] columnWidths, LinkedHashMap<String, String> invoiceHeadings, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// createPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);
		createPdfGenericFoure(byteArrayOutputStream, invoiceData, invoiceHeadings, invoiceRequestBean, columnWidths,
				reportHeading, departmentTotalslist, departmentTotalsTableHeadings, secondTableWidths,
				secondTableMethodName, combinationOfHeadingsAndValues);
		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	public static HttpServletResponse writeUserRoleManagementPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createUserRoleManagementPdf(byteArrayOutputStream, invoiceData);

		String reportName = "User RoleManagement";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	public static HttpServletResponse writeAdminRoleManagementPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createAdminRoleManagementPdf(byteArrayOutputStream, invoiceData);

		String reportName = "Module Wise Role Access Report";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	public static HttpServletResponse writeHospitalWardOccupancyPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createHospitalWardOccupancyPdf(byteArrayOutputStream, invoiceData);

		String reportName = "HospitalWardOccupancyReport";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	// writeNearExpiryDrugDetailsPdfToResponse
	public static HttpServletResponse writeNearExpiryDrugDetailsPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createNearExpiryDrugDetailsPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);

		String reportName = "HospitalWardOccupancyReport";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	// fetchExpiredDrugDetails
	public static HttpServletResponse writeExpiryDrugDetailsPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createExpiryDrugDetailsPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);

		String reportName = "ExpiryDrugDetails";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	// fetchUserwiseInvoiceDetails
	public static HttpServletResponse writeUserWiseDetailsPdfToResponse(HttpServletResponse response,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		createUserWiseDetailsPdf(byteArrayOutputStream, invoiceData, invoiceRequestBean);

		String reportName = "UserWiseInvoice";

		settingPdfResponseHeaders(response, byteArrayOutputStream, reportName);
		OutputStream outputStream = response.getOutputStream();
		// write to output stream
		byteArrayOutputStream.writeTo(outputStream);
		// flush the stream
		outputStream.flush();
		response.flushBuffer();
		outputStream.close();
		return response;
	}

	private static void createPdfGenericOne(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, LinkedHashMap<String, String> invoiceHeadings,
			InvoiceRequestBean invoiceRequestBean, float[] columnWidths, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {
		// step 1
		// Document document = new Document();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 30f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		Rotate event = new Rotate();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// For Filter Data
		Map<String, String> filterDataMap = new LinkedHashMap<>(invoiceData.get(0));
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));
		if (invoiceRequestBean.getDoctorId() != null) {
			filterData.append(", Dr Name : " + filterDataMap.get("doctor_nm"));
		}
		if (invoiceRequestBean.getDepartmentId() != null) {
			filterData.append(", Dept Name : " + filterDataMap.get("dept_nm"));
		}
		if (invoiceRequestBean.getServiceId() != null) {
			filterData.append(", Service Name : " + filterDataMap.get("service_nm"));
		}
		if (invoiceRequestBean.getPaymentMode() != null) {
			filterData.append(", Payment Mode : " + filterDataMap.get("payment_mode"));
		}
		if (invoiceRequestBean.getEmpId() != null) {
			filterData.append(", Employee Name : " + filterDataMap.get("user_nm"));
		}
		if (invoiceRequestBean.getDoctorName() != null) {
			filterData.append(", Ref Dr Name : " + filterDataMap.get("ref_doctor_nm"));
		}

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase(reportHeading,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);

		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));
		// Do not Change values
		// float[] columnWidths = { 0.5f, 1, 1.6f, 1.2f, 0.8f, 1.5f, 1.5f, 0.4f,
		// 0.5f, 1 };
		PdfPTable table = new PdfPTable(columnWidths.length); // columns.
		table.setWidths(columnWidths);
		table.setWidthPercentage(100);

		// Adding Table Headings
		for (Map.Entry<String, String> cellData : invoiceHeadings.entrySet()) {

			table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}

		// Adding S.No
		List<Map<String, String>> pdfDataWithSNoList = new ArrayList<>();
		int sno = 1;
		for (Map<String, String> obj : invoiceData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, String> entry : obj.entrySet()) {
				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			pdfDataWithSNoList.add(mapWithSno);
		}

		for (Map<String, String> pdfMap : pdfDataWithSNoList) {
			// Adding Table Data
			int findingLastColumn = 1;
			for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
				if (columnWidths.length != findingLastColumn) {
					table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
				} else {

					PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
					lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(lastCell);

				}
				findingLastColumn++;
			}
		}
		document.add(table);
		if ("pdfSecondTableConstructionOne".equalsIgnoreCase(secondTableMethodName)) {
			pdfSecondTableConstructionOne(document, departmentTotalslist, departmentTotalsTableHeadings,
					secondTableWidths);
			if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
				createPieChart(document, writer, combinationOfHeadingsAndValues);
			}

		} else if ("pdfSecondTableConstructionTwo".equalsIgnoreCase(secondTableMethodName)) {
			pdfSecondTableConstructionTwo(document, departmentTotalslist, departmentTotalsTableHeadings,
					secondTableWidths);
		}
		document.close();
	}

	private static void createPdfGenericTwo(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, LinkedHashMap<String, String> invoiceHeadings,
			InvoiceRequestBean invoiceRequestBean, float[] columnWidths, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {
		// step 1
		// Document document = new Document();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 30f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		Rotate event = new Rotate();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// For Filter Data
		Map<String, String> filterDataMap = new LinkedHashMap<>(invoiceData.get(0));
		StringBuilder filterData = new StringBuilder();
		if (invoiceRequestBean.getFromDate() != null && invoiceRequestBean.getToDate() != null) {

			filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
			filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));

			if (invoiceRequestBean.getDoctorId() != null) {
				filterData.append(", Dr Name : " + filterDataMap.get("doctor_nm"));
			}
			if (invoiceRequestBean.getDepartmentId() != null) {
				filterData.append(", Dept Name : " + filterDataMap.get("dept_nm"));
			}
			if (invoiceRequestBean.getServiceId() != null) {
				filterData.append(", Service Name : " + filterDataMap.get("service_nm"));
			}
			if (invoiceRequestBean.getPaymentMode() != null) {
				filterData.append(", Payment Mode : " + filterDataMap.get("payment_mode"));
			}
			if (invoiceRequestBean.getEmpId() != null) {
				filterData.append(", User Name : " + filterDataMap.get("bill_user"));
			}

			// Adding Report Filters from User Search Criteria
			document.add(new Paragraph(filterData.toString(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
			document.add(new Paragraph("  "));
		}
		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase(reportHeading,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);
		// Do not Change values
		// float[] columnWidths = { 0.5f, 1, 1.6f, 1.2f, 0.8f, 1.5f, 1.5f, 0.4f,
		// 0.5f, 1 };
		PdfPTable table = new PdfPTable(columnWidths.length); // columns.
		table.setWidths(columnWidths);
		table.setWidthPercentage(100);

		// Adding Table Headings
		for (Map.Entry<String, String> cellData : invoiceHeadings.entrySet()) {

			table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}

		// Adding S.No
		List<Map<String, String>> pdfDataWithSNoList = new ArrayList<>();
		int sno = 1;
		for (Map<String, String> obj : invoiceData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, String> entry : obj.entrySet()) {
				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			pdfDataWithSNoList.add(mapWithSno);
		}

		for (Map<String, String> pdfMap : pdfDataWithSNoList) {
			// Adding Table Data
			int findingLastColumn = 1;
			for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
				if (columnWidths.length != findingLastColumn) {
					// second last column alignment to right
					if ((columnWidths.length - 1) == findingLastColumn) {

						PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(lastSecondCell);

					} else {
						table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
					}
				} else {

					PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
					lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(lastCell);

				}
				findingLastColumn++;
			}
		}
		document.add(table);
		if ("pdfSecondTableConstructionOne".equalsIgnoreCase(secondTableMethodName)) {
			pdfSecondTableConstructionOne(document, departmentTotalslist, departmentTotalsTableHeadings,
					secondTableWidths);
			if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
				createPieChart(document, writer, combinationOfHeadingsAndValues);
			}

		} else if ("pdfSecondTableConstructionTwo".equalsIgnoreCase(secondTableMethodName)) {
			pdfSecondTableConstructionTwo(document, departmentTotalslist, departmentTotalsTableHeadings,
					secondTableWidths);
			if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
				createPieChart(document, writer, combinationOfHeadingsAndValues);
			}
		} else if ("pdfSecondTableConstructionFoure".equalsIgnoreCase(secondTableMethodName)) {
			pdfSecondTableConstructionFoure(document, departmentTotalslist, departmentTotalsTableHeadings,
					secondTableWidths);
			if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
				createPieChart(document, writer, combinationOfHeadingsAndValues);
			}

		}
		document.close();
	}

	private static void createPdfGenericThree(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, LinkedHashMap<String, String> invoiceHeadings,
			InvoiceRequestBean invoiceRequestBean, float[] columnWidths, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {
		// step 1
		// Document document = new Document();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 30f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		Rotate event = new Rotate();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// For Filter Data
		Map<String, String> filterDataMap = new LinkedHashMap<>(invoiceData.get(0));
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));
		if (invoiceRequestBean.getDoctorId() != null) {
			filterData.append(", Dr Name : " + filterDataMap.get("doctor_nm"));
		}
		if (invoiceRequestBean.getDepartmentId() != null) {
			filterData.append(", Dept Name : " + filterDataMap.get("dept_nm"));
		}
		if (invoiceRequestBean.getServiceId() != null) {
			filterData.append(", Service Name : " + filterDataMap.get("service_nm"));
		}
		if (invoiceRequestBean.getPaymentMode() != null) {
			filterData.append(", Payment Mode : " + filterDataMap.get("payment_mode"));
		}

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase(reportHeading,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);

		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));
		// Do not Change values
		// float[] columnWidths = { 0.5f, 1, 1.6f, 1.2f, 0.8f, 1.5f, 1.5f, 0.4f,
		// 0.5f, 1 };
		PdfPTable table = new PdfPTable(columnWidths.length); // columns.
		table.setWidths(columnWidths);
		table.setWidthPercentage(100);

		// Adding Table Headings
		for (Map.Entry<String, String> cellData : invoiceHeadings.entrySet()) {

			table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}

		// Adding S.No
		List<Map<String, String>> pdfDataWithSNoList = new ArrayList<>();
		int sno = 1;
		for (Map<String, String> obj : invoiceData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, String> entry : obj.entrySet()) {
				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			pdfDataWithSNoList.add(mapWithSno);
		}

		for (Map<String, String> pdfMap : pdfDataWithSNoList) {
			// Adding Table Data
			int findingLastColumn = 1;
			for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
				if (columnWidths.length != findingLastColumn) {
					// second last column alignment to right
					if ((columnWidths.length - 1) == findingLastColumn) {

						PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(lastSecondCell);
						// third last column alignment to right
					} else if ((columnWidths.length - 2) == findingLastColumn) {

						PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						table.addCell(lastSecondCell);

					} else {
						table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
					}
				} else {

					PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
					lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					table.addCell(lastCell);

				}
				findingLastColumn++;
			}
		}
		document.add(table);
		pdfSecondTableConstructionThree(document, departmentTotalslist, departmentTotalsTableHeadings,
				secondTableWidths);
		if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
			createPieChart(document, writer, combinationOfHeadingsAndValues);
		}

		document.close();
	}

	private static void createPdfGenericFoure(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, LinkedHashMap<String, String> invoiceHeadings,
			InvoiceRequestBean invoiceRequestBean, float[] columnWidths, String reportHeading,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths, String secondTableMethodName,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws IOException, DocumentException {
		// step 1
		// Document document = new Document();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 30f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		Rotate event = new Rotate();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// For Filter Data
		Map<String, String> filterDataMap = new LinkedHashMap<>(invoiceData.get(0));
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));
		if (invoiceRequestBean.getDoctorId() != null) {
			filterData.append(", Dr Name : " + filterDataMap.get("doctor_nm"));
		}
		if (invoiceRequestBean.getDepartmentId() != null) {
			filterData.append(", Dept Name : " + filterDataMap.get("dept_nm"));
		}
		if (invoiceRequestBean.getServiceId() != null) {
			filterData.append(", Service Name : " + filterDataMap.get("service_nm"));
		}
		if (invoiceRequestBean.getPaymentMode() != null) {
			filterData.append(", Payment Mode : " + filterDataMap.get("payment_mode"));
		}
		if (invoiceRequestBean.getAppointmentType() != null) {
			filterData.append(", Appointment Type : " + filterDataMap.get("app_type"));
		}

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase(reportHeading,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);

		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));
		// Do not Change values
		// float[] columnWidths = { 0.5f, 1, 1.6f, 1.2f, 0.8f, 1.5f, 1.5f, 0.4f,
		// 0.5f, 1 };
		PdfPTable table = new PdfPTable(columnWidths.length); // columns.
		table.setWidths(columnWidths);
		table.setWidthPercentage(100);

		// Adding Table Headings
		for (Map.Entry<String, String> cellData : invoiceHeadings.entrySet()) {

			table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}

		// Adding S.No
		List<Map<String, String>> pdfDataWithSNoList = new ArrayList<>();
		int sno = 1;
		for (Map<String, String> obj : invoiceData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, String> entry : obj.entrySet()) {
				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue());
			}
			pdfDataWithSNoList.add(mapWithSno);
		}

		for (Map<String, String> pdfMap : pdfDataWithSNoList) {
			for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {

				PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
				table.addCell(lastCell);

			}
		}
		document.add(table);

		pdfSecondTableConstructionFoure(document, departmentTotalslist, departmentTotalsTableHeadings,
				secondTableWidths);
		if (combinationOfHeadingsAndValues != null && combinationOfHeadingsAndValues.size() > 1) {
			createPieChart(document, writer, combinationOfHeadingsAndValues);
		}
		document.close();
	}

	private static void pdfSecondTableConstructionOne(Document document, List<Map<String, String>> departmentTotalslist,
			LinkedHashMap<String, String> departmentTotalsTableHeadings, float[] secondTableWidths)
			throws DocumentException {

		if (departmentTotalsTableHeadings != null && departmentTotalsTableHeadings.size() > 0
				&& departmentTotalslist != null && departmentTotalslist.size() > 0) {
			document.add(new Paragraph("          "));
			document.add(new Paragraph("Sub Totals:",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));

			// Adding department Totals Table Heading
			PdfPTable departmentTotalsTable = new PdfPTable(secondTableWidths.length); // columns.
			departmentTotalsTable.setWidths(secondTableWidths);
			departmentTotalsTable.setWidthPercentage(50);

			for (Map.Entry<String, String> cellData : departmentTotalsTableHeadings.entrySet()) {
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			}

			double grandTotal = 0.00;
			int totalInvoiceCount = 0;
			double totalInvoiceAmount = 0;
			boolean isNumberFormateException = false;

			for (Map<String, String> pdfMap : departmentTotalslist) {
				// Adding Table Data
				int findingLastColumn = 1;
				for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
					if (secondTableWidths.length != findingLastColumn) {
						// second last column alignment to right
						if ((secondTableWidths.length - 1) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);

							try {
								totalInvoiceCount = +totalInvoiceCount + (cellData.getValue().length() > 0
										? Integer.parseInt(cellData.getValue()) : 0);
							} catch (NumberFormatException nfe) {
								totalInvoiceAmount = +totalInvoiceAmount + (cellData.getValue().length() > 0
										? Double.parseDouble(cellData.getValue()) : 0);
								isNumberFormateException = true;
							}
						} else {
							departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						}
					} else {
						// last column alignment to right
						PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						departmentTotalsTable.addCell(lastCell);
						// Last Column Need to add for finding Grand Total
						grandTotal = +grandTotal
								+ (cellData.getValue().length() > 0 ? Double.parseDouble(cellData.getValue()) : 0.00);
					}
					findingLastColumn++;
				}
			}

			for (int i = 3; i < secondTableWidths.length; i++) {
				// Adding Grand Total Row empty columns
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph("")));
			}
			PdfPCell lastThirdCell = (new PdfPCell(new Paragraph("Grand Total : ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastThirdCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastThirdCell);

			if (isNumberFormateException) {
				// totalInvoiceAmount
				PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(String.format("%.2f", totalInvoiceAmount),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
				lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				departmentTotalsTable.addCell(lastSecondCell);

			} else {
				PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(totalInvoiceCount + "",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
				lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				departmentTotalsTable.addCell(lastSecondCell);
			}

			// String.format("%.2f", grandTotal) prints grandTotal 0.00 format
			PdfPCell lastCell = (new PdfPCell(new Paragraph(String.format("%.2f", grandTotal),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastCell);
			// Setting Table alignment Left to Center
			departmentTotalsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.add(departmentTotalsTable);
		} // End if

	}

	private static void pdfSecondTableConstructionTwo(Document document, List<Map<String, String>> departmentTotalslist,
			LinkedHashMap<String, String> departmentTotalsTableHeadings, float[] secondTableWidths)
			throws DocumentException {

		if (departmentTotalsTableHeadings != null && departmentTotalsTableHeadings.size() > 0
				&& departmentTotalslist != null && departmentTotalslist.size() > 0) {
			document.add(new Paragraph("          "));
			document.add(new Paragraph("          "));
			document.add(new Paragraph("Sub Totals:",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
			document.add(new Paragraph("          "));

			// Adding department Totals Table Heading
			PdfPTable departmentTotalsTable = new PdfPTable(secondTableWidths.length); // columns.
			departmentTotalsTable.setWidths(secondTableWidths);
			// Note:Update This Logic
			if (secondTableWidths.length < 6) {
				departmentTotalsTable.setWidthPercentage(50);
			} else {
				departmentTotalsTable.setWidthPercentage(65);
			}

			for (Map.Entry<String, String> cellData : departmentTotalsTableHeadings.entrySet()) {
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			}

			double amountOne = 0;
			double amountTwo = 0;
			double amountThree = 0;
			for (Map<String, String> pdfMap : departmentTotalslist) {
				// Adding Table Data
				int findingLastColumn = 1;
				for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
					if (secondTableWidths.length != findingLastColumn) {
						// second last column alignment to right
						if ((secondTableWidths.length - 1) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);
							amountTwo = +amountTwo + (cellData.getValue().length() > 0
									? Double.parseDouble(cellData.getValue()) : 0.00);

						}
						// third last column alignment to right
						else if ((secondTableWidths.length - 2) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);
							amountThree = +amountThree
									+ (cellData.getValue().length() > 0 ? Double.parseDouble(cellData.getValue()) : 0);

						} else {
							departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						}
					} else {
						// last column alignment to right
						PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						departmentTotalsTable.addCell(lastCell);
						// Last Column Need to add
						amountOne = +amountOne
								+ (cellData.getValue().length() > 0 ? Double.parseDouble(cellData.getValue()) : 0.00);
					}
					findingLastColumn++;
				}
			}

			for (int i = 4; i < secondTableWidths.length; i++) {
				// Adding Grand Total Row empty columns
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph("")));
			}

			PdfPCell lastFourthCell = (new PdfPCell(new Paragraph("Grand Total : ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastFourthCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastFourthCell);

			PdfPCell lastThirdCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountThree),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastThirdCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastThirdCell);

			PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountTwo),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastSecondCell);

			// String.format("%.2f", grandTotal) prints grandTotal 0.00 format
			PdfPCell lastCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountOne),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastCell);
			// Align Second table in center
			departmentTotalsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.add(departmentTotalsTable);
		} // End if
	}

	private static void pdfSecondTableConstructionThree(Document document,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths) throws DocumentException {

		if (departmentTotalsTableHeadings != null && departmentTotalsTableHeadings.size() > 0
				&& departmentTotalslist != null && departmentTotalslist.size() > 0) {
			document.add(new Paragraph("          "));
			document.add(new Paragraph("          "));
			document.add(new Paragraph("Sub Totals:",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
			document.add(new Paragraph("          "));

			// Adding department Totals Table Heading
			PdfPTable departmentTotalsTable = new PdfPTable(secondTableWidths.length); // columns.
			departmentTotalsTable.setWidths(secondTableWidths);
			// Note:Update This Logic
			if (secondTableWidths.length < 6) {
				departmentTotalsTable.setWidthPercentage(50);
			} else {
				departmentTotalsTable.setWidthPercentage(65);
			}

			for (Map.Entry<String, String> cellData : departmentTotalsTableHeadings.entrySet()) {
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			}

			double amountOne = 0;
			double amountTwo = 0;
			double amountThree = 0;
			int amountFourth = 0;
			for (Map<String, String> pdfMap : departmentTotalslist) {
				// Adding Table Data
				int findingLastColumn = 1;
				for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
					if (secondTableWidths.length != findingLastColumn) {
						// second last column alignment to right
						if ((secondTableWidths.length - 1) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);
							amountTwo = +amountTwo + (cellData.getValue().length() > 0
									? Double.parseDouble(cellData.getValue()) : 0.00);

						}
						// third last column alignment to right
						else if ((secondTableWidths.length - 2) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);
							amountThree = +amountThree
									+ (cellData.getValue().length() > 0 ? Double.parseDouble(cellData.getValue()) : 0);

						} // fourth last column alignment to right
						else if ((secondTableWidths.length - 3) == findingLastColumn) {

							PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
							lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							departmentTotalsTable.addCell(lastCell);
							amountFourth = +amountFourth
									+ (cellData.getValue().length() > 0 ? Integer.parseInt(cellData.getValue()) : 0);

						} else {
							departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
									FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						}
					} else {
						// last column alignment to right
						PdfPCell lastCell = (new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
						lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						departmentTotalsTable.addCell(lastCell);
						// Last Column Need to add
						amountOne = +amountOne
								+ (cellData.getValue().length() > 0 ? Double.parseDouble(cellData.getValue()) : 0.00);
					}
					findingLastColumn++;
				}
			}

			for (int i = 5; i < secondTableWidths.length; i++) {
				// Adding Grand Total Row empty columns
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph("")));
			}

			PdfPCell lastFithCell = (new PdfPCell(new Paragraph("Grand Total : ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastFithCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastFithCell);

			PdfPCell lastFourthCell = (new PdfPCell(new Paragraph(amountFourth + "",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastFourthCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastFourthCell);

			PdfPCell lastThirdCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountThree),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastThirdCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastThirdCell);

			PdfPCell lastSecondCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountTwo),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastSecondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastSecondCell);

			// String.format("%.2f", grandTotal) prints grandTotal 0.00 format
			PdfPCell lastCell = (new PdfPCell(new Paragraph(String.format("%.2f", amountOne),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));
			lastCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			departmentTotalsTable.addCell(lastCell);
			// Setting Table alignment Left to Center
			departmentTotalsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.add(departmentTotalsTable);
		} // End if
	}

	private static void pdfSecondTableConstructionFoure(Document document,
			List<Map<String, String>> departmentTotalslist, LinkedHashMap<String, String> departmentTotalsTableHeadings,
			float[] secondTableWidths) throws DocumentException {

		if (departmentTotalsTableHeadings != null && departmentTotalsTableHeadings.size() > 0
				&& departmentTotalslist != null && departmentTotalslist.size() > 0) {
			document.add(new Paragraph("          "));
			document.add(new Paragraph("Sub Totals:",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));

			// Adding department Totals Table Heading
			PdfPTable departmentTotalsTable = new PdfPTable(secondTableWidths.length); // columns.
			departmentTotalsTable.setWidths(secondTableWidths);
			departmentTotalsTable.setWidthPercentage(50);

			for (Map.Entry<String, String> cellData : departmentTotalsTableHeadings.entrySet()) {
				departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			}

			for (Map<String, String> pdfMap : departmentTotalslist) {
				for (Map.Entry<String, String> cellData : pdfMap.entrySet()) {
					departmentTotalsTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
				}
			}

			// Setting Table alignment Left to Center
			departmentTotalsTable.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.add(departmentTotalsTable);
		} // End if

	}

	public static class PageNoForA4SizePdf extends PdfPageEventHelper {

		PdfTemplate total;

		/*
		 * Creates the PdfTemplate that will hold the total number of pages.
		 */
		public void onOpenDocument(PdfWriter writer, Document document) {
			// This is For A4 or PORTRAIT
			// total = writer.getDirectContent().createTemplate(30, 12);//This
			// This is for LANDSCAPE Rotation
			total = writer.getDirectContent().createTemplate(432, 252);
		}

		public void onEndPage(PdfWriter writer, Document document) {
			document.addAuthor(AUTHOR);
			PdfPTable table = new PdfPTable(2);
			try {

				table.setWidths(new int[] { 48, 2 });
				table.setTotalWidth(527);
				table.setLockedWidth(true);
				table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				PdfPCell c1 = new PdfPCell(new Phrase(String.format("Page %d ", writer.getPageNumber()),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 8)));
				c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				c1.setBorder(0);
				table.addCell(c1);
				c1 = new PdfPCell(Image.getInstance(total));
				c1.setBorder(0);
				table.addCell(c1);
				table.writeSelectedRows(0, -1, 36, 25, writer.getDirectContent()); // PDF
																					// Page
																					// Number
																					// Position
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

	}

	private static void createUserRoleManagementPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		PageNoForA4SizePdf event = new PageNoForA4SizePdf();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase("User Wise Role Management",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);

		Map<String, String> invoiceHeadings = new LinkedHashMap<>();
		invoiceHeadings.put("1", "Mode Name");
		invoiceHeadings.put("2", "Role Name");
		invoiceHeadings.put("3", "Screen Name");
		invoiceHeadings.put("4", "Screen Type");
		invoiceHeadings.put("5", "Privilege Id");

		Map<String, String> flag = new LinkedHashMap<>();
		Map<String, String> modeNameFlag = new LinkedHashMap<>();
		Map<String, String> roleNameFlag = new LinkedHashMap<>();
		boolean flagTwo = false;
		PdfPTable table1 = null;
		boolean modeNameFlagBoolean = false;
		boolean roleNameFlagBoolean = false;
		int listLastEle = invoiceData.size();
		int count = 1;
		for (Map<String, String> pdfMap : invoiceData) {
			/*
			 * First time emp Name is not there so By using flagTwo I am
			 * skipping first time adding table second time onwards based on new
			 * emp_nm
			 */
			if (!flag.containsKey(pdfMap.get("emp_nm")) && flagTwo) {
				document.add(table1);
			}

			/* Checking emp_nm already exist */
			if (flag.put(pdfMap.get("emp_nm"), "") == null) {
				document.add(new Paragraph(" "));

				document.add(new Paragraph("Employee User Name : " + pdfMap.get("emp_nm"),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
				document.add(new Paragraph(" "));
				table1 = new PdfPTable(5); // columns.
				table1.setWidths(new float[] { 1.2f, 1.2f, 1.4f, 0.6f, 0.6f });
				table1.setWidthPercentage(105);
				modeNameFlag = new LinkedHashMap<>();
				roleNameFlag = new LinkedHashMap<>();

				if (!flagTwo) {
					// Adding Table Headings only once
					for (Map.Entry<String, String> cellData : invoiceHeadings.entrySet()) {

						table1.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
								FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

					}
				}
				modeNameFlagBoolean = modeNameFlag.containsKey(pdfMap.get("mod_nm"));
				roleNameFlagBoolean = roleNameFlag.containsKey(pdfMap.get("role_nm"));
				createUserRoleManagementPdfUtill(pdfMap, table1, modeNameFlagBoolean, roleNameFlagBoolean);

			} else {
				modeNameFlagBoolean = modeNameFlag.containsKey(pdfMap.get("mod_nm"));
				roleNameFlagBoolean = roleNameFlag.containsKey(pdfMap.get("role_nm"));
				createUserRoleManagementPdfUtill(pdfMap, table1, modeNameFlagBoolean, roleNameFlagBoolean);
			}
			flagTwo = true;
			modeNameFlag.put(pdfMap.get("mod_nm"), "");
			roleNameFlag.put(pdfMap.get("role_nm"), "");
			/* For Adding Last Employee Details */
			if (listLastEle == count) {
				document.add(table1);
			}
			count++;
		}

		document.close();
	}

	private static void createAdminRoleManagementPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		PageNoForA4SizePdf event = new PageNoForA4SizePdf();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase p = new Phrase("Module Wise Role Access Report",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		p.add(new Chunk(glue));
		p.add("Generated On : " + simpDate.format(new Date()));
		document.add(p);
		document.add(new Paragraph("Module Name : " + invoiceData.get(0).get("mod_nm"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, (PDF_FONT_SIZE_FOR_TABLE_HEADING - 1))));
		document.add(new Paragraph(" "));

		int listLastEle = invoiceData.size();
		int count = 1;
		boolean flagTwo = false;
		LinkedHashMap<String, String> modeFlag = new LinkedHashMap<>();
		List<String> empNames = new ArrayList<>();
		PdfPTable modeTable = null;
		PdfPTable empTable = null;
		PdfPTable addTwoTables = null;
		for (Map<String, String> pdfMap : invoiceData) {
			if (!modeFlag.containsKey(pdfMap.get("role_nm")) && flagTwo) {
				addingEmployeeTebleUtill(empNames, empTable);
				empNames = new ArrayList<>();
				addTwoTables.addCell(modeTable);
				addTwoTables.addCell(empTable);
				document.add(addTwoTables);
			}

			if (modeFlag.put(pdfMap.get("role_nm"), "") == null) {
				document.add(new Paragraph("Role Name : " + pdfMap.get("role_nm"),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, (PDF_FONT_SIZE_FOR_TABLE_HEADING - 1))));

				document.add(new Paragraph(" "));

				modeTable = new PdfPTable(2); // columns.
				modeTable.setWidths(new float[] { 1.4f, 0.6f });
				modeTable.addCell(new PdfPCell(new Paragraph("Screen Name",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER))));

				modeTable.addCell(new PdfPCell(new Paragraph("Privilege",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING))));

				if ("SCREEN".equalsIgnoreCase(pdfMap.get("screen_emp_ind"))) {
					createAdminRoleManagementPdfUtill(document, pdfMap, modeTable);
				}

				empTable = new PdfPTable(1);
				empTable.setWidths(new float[] { 1 });
				empTable.addCell(new PdfPCell(new Paragraph("Employee Name",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING))));

				if ("EMPLOYEE".equalsIgnoreCase(pdfMap.get("screen_emp_ind"))) {
					empNames.add(pdfMap.get("se_nm"));
				}

				addTwoTables = new PdfPTable(2);
				addTwoTables.setWidths(new float[] { 0.6f, 0.3f });
				addTwoTables.getDefaultCell().setBorder(0);
			} else {
				if ("SCREEN".equalsIgnoreCase(pdfMap.get("screen_emp_ind"))) {
					createAdminRoleManagementPdfUtill(document, pdfMap, modeTable);
				}
				if ("EMPLOYEE".equalsIgnoreCase(pdfMap.get("screen_emp_ind"))) {
					empNames.add(pdfMap.get("se_nm"));
				}
			}
			flagTwo = true;
			/* For Adding Last Employee Details */
			if (listLastEle == count) {

				addingEmployeeTebleUtill(empNames, empTable);

				addTwoTables.addCell(modeTable);
				addTwoTables.addCell(empTable);
				document.add(addTwoTables);

			}
			count++;
		}

		document.close();
	}

	private static void createAdminRoleManagementPdfUtill(Document document, Map<String, String> pdfMap,
			PdfPTable table) throws DocumentException {

		table.addCell(new PdfPCell(new Paragraph(pdfMap.get("se_nm"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		String privilageTypeBasedOnId = "";
		if ("1".equalsIgnoreCase(pdfMap.get("pid"))) {
			privilageTypeBasedOnId = "Read";
		} else if ("2".equalsIgnoreCase(pdfMap.get("pid"))) {
			privilageTypeBasedOnId = "Write";
		} else if ("3".equalsIgnoreCase(pdfMap.get("pid"))) {
			privilageTypeBasedOnId = "All";
		}
		table.addCell(new PdfPCell(new Paragraph(privilageTypeBasedOnId,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

	}

	private static void addingEmployeeTebleUtill(List<String> employeeNamesOnly, PdfPTable table)
			throws DocumentException {

		for (String empName : employeeNamesOnly) {
			table.addCell(new PdfPCell(new Paragraph(empName,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}
	}

	private static void createUserRoleManagementPdfUtill(Map<String, String> pdfMap, PdfPTable table1,
			boolean modeNameFlagBoolean, boolean roleNameFlagBoolean) throws DocumentException {

		PdfPCell modeName = null;

		if (!modeNameFlagBoolean) {
			modeName = (new PdfPCell(new Paragraph(pdfMap.get("mod_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
		} else {
			modeName = (new PdfPCell(new Paragraph(" ")));
		}
		PdfPCell roleName = null;
		if (!roleNameFlagBoolean) {
			roleName = (new PdfPCell(new Paragraph(pdfMap.get("role_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		} else {
			roleName = new PdfPCell(new Paragraph(" "));

		}

		PdfPCell screenName = (new PdfPCell(new Paragraph(pdfMap.get("screen_nm"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		String screenTypeData = "";
		if ("S".equalsIgnoreCase(pdfMap.get("screen_type"))) {
			screenTypeData = "Screen";
		} else if ("R".equalsIgnoreCase(pdfMap.get("screen_type"))) {
			screenTypeData = "Report";
		}
		PdfPCell screenType = new PdfPCell(new Paragraph(screenTypeData,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));

		String privilageTypeBasedOnId = "";
		if ("1".equalsIgnoreCase(pdfMap.get("privilege_id"))) {
			privilageTypeBasedOnId = "Read";
		} else if ("2".equalsIgnoreCase(pdfMap.get("privilege_id"))) {
			privilageTypeBasedOnId = "Write";
		} else if ("3".equalsIgnoreCase(pdfMap.get("privilege_id"))) {
			privilageTypeBasedOnId = "All";
		}
		PdfPCell privilageId = new PdfPCell(new Paragraph(privilageTypeBasedOnId,
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));

		table1.addCell(modeName);
		table1.addCell(roleName);
		table1.addCell(screenName);
		table1.addCell(screenType);
		table1.addCell(privilageId);

	}

	private static void createHospitalWardOccupancyPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData) throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		PageNoForA4SizePdf event = new PageNoForA4SizePdf();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase heading = new Phrase("Hospital Ward Occupancy Report",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING + 1));
		heading.add(new Chunk(glue));
		heading.add("Generated On : " + simpDate.format(new Date()));
		document.add(heading);
		document.add(new Paragraph("  "));
		Phrase subHeading = new Phrase("Total Hospital Beds : " + invoiceData.get(0).get("hospital_total_beds"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		subHeading.add("         Total Occupied Beds : " + invoiceData.get(0).get("hospital_total_occupied_beds"));
		subHeading.add("         Total Available Beds : " + invoiceData.get(0).get("hospita_total_available_beds"));
		document.add(subHeading);
		document.add(new Paragraph("   "));
		LineSeparator lineSeperatior = new LineSeparator();
		document.add(lineSeperatior);
		PdfPTable wardTable = null;
		int listLastEle = invoiceData.size();
		int count = 1;
		boolean flagTwo = false;
		Map<String, String> wardTableHeadings = new LinkedHashMap<>();
		wardTableHeadings.put("1", "Room Number");
		wardTableHeadings.put("2", "Room Type");
		wardTableHeadings.put("3", "Bed Number");
		wardTableHeadings.put("4", "Patient Admit Date");
		wardTableHeadings.put("5", "Patient Name/Age/Gender");
		wardTableHeadings.put("6", "Doctor Name");

		Map<String, String> wardNameFlag = new LinkedHashMap<>();
		for (Map<String, String> data : invoiceData) {

			if (!wardNameFlag.containsKey(data.get("ward_nm")) && flagTwo) {
				document.add(wardTable);
			}
			if (wardNameFlag.put(data.get("ward_nm"), "") == null) {
				document.add(new Paragraph("Ward Name: " + data.get("ward_nm"),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA + 2)));
				Phrase wardHeading = new Phrase("Total Beds : " + data.get("ward_total_beds"),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA + 1));

				wardHeading.add("   Total Occupied Beds : " + data.get("ward_occupied_beds"));
				wardHeading.add("   Total Available Beds : " + data.get("ward_available_beds"));
				document.add(wardHeading);
				wardTable = new PdfPTable(6); // columns.
				wardTable.setWidths(new float[] { 0.9f, 0.7f, 0.8f, 0.9f, 1.6f, 1.1f });
				wardTable.setWidthPercentage(100);
				// Adding Table Headings only once
				for (Map.Entry<String, String> cellData : wardTableHeadings.entrySet()) {

					wardTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

				}
				createHospitalWardOccupancyPdfUtill(wardTable, data);
			} else {
				createHospitalWardOccupancyPdfUtill(wardTable, data);
			}

			flagTwo = true;
			/* For Adding Last Employee Details */
			if (listLastEle == count) {

				document.add(wardTable);

			}
			count++;
		}

		document.close();
	}

	private static void createHospitalWardOccupancyPdfUtill(PdfPTable wardTable, Map<String, String> data) {

		wardTable.addCell(new PdfPCell(new Paragraph(data.get("room_no"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		wardTable.addCell(new PdfPCell(new Paragraph(data.get("room_type"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		wardTable.addCell(new PdfPCell(new Paragraph(data.get("bed_no"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		wardTable.addCell(new PdfPCell(new Paragraph(data.get("admit_dt"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
		StringBuilder patientNameAgeGendercombination = new StringBuilder();
		patientNameAgeGendercombination
				.append((data.get("patient_nm").length() == 0 ? "" : data.get("patient_nm") + "/"));
		patientNameAgeGendercombination.append((data.get("age").length() == 0 ? "" : data.get("age") + "/"));
		patientNameAgeGendercombination.append((data.get("gender").length() == 0 ? "" : data.get("gender")));

		wardTable.addCell(new PdfPCell(new Paragraph(patientNameAgeGendercombination.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		wardTable.addCell(new PdfPCell(new Paragraph(data.get("doctor_nm"),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
	}

	// writeNearExpiryDrugDetailsPdfToResponse
	private static void createNearExpiryDrugDetailsPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		PageNoForA4SizePdf event = new PageNoForA4SizePdf();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase heading = new Phrase("Near Expiry Drug Details Report",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		heading.add(new Chunk(glue));
		heading.add("Generated On : " + simpDate.format(new Date()));
		document.add(heading);
		document.add(new Paragraph("  "));

		// For Filter Data
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));

		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));

		Map<String, String> wardTableHeadings = new LinkedHashMap<>();
		wardTableHeadings.put("1", "Drug Brand");
		wardTableHeadings.put("2", "Drug Type");
		wardTableHeadings.put("3", "Drug Name");
		wardTableHeadings.put("4", "Stock Qty");
		wardTableHeadings.put("5", "Batch Number");
		wardTableHeadings.put("6", "Expiry Date");
		wardTableHeadings.put("7", "Total Cost Price");

		PdfPTable wardTable = new PdfPTable(7); // columns.
		wardTable.setWidths(new float[] { 1.2f, 1.2f, 1.5f, 0.5f, 1, 0.8f, 0.8f });
		wardTable.setWidthPercentage(100);

		// Adding Table Headings only once
		for (Map.Entry<String, String> cellData : wardTableHeadings.entrySet()) {

			wardTable.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}
		long sumOfTotalCostPrice = 0;
		// Adding PDf Body Data
		for (Map<String, String> data : invoiceData) {
			wardTable.addCell(new PdfPCell(new Paragraph(data.get("drug_brand_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			wardTable.addCell(new PdfPCell(new Paragraph(data.get("drug_type_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			wardTable.addCell(new PdfPCell(new Paragraph(data.get("drug_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			wardTable.addCell(new PdfPCell(new Paragraph(data.get("stock_qty"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			wardTable.addCell(new PdfPCell(new Paragraph(data.get("batch_no"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			wardTable.addCell(new PdfPCell(new Paragraph(data.get("expire_dt"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
			String totalCostPrice = data.get("total_cost_price").length() == 0 ? "0" : data.get("total_cost_price");
			sumOfTotalCostPrice += Long.parseLong(totalCostPrice);
			PdfPCell totalCostPriceCell = new PdfPCell(new Paragraph(totalCostPrice,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
			totalCostPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			wardTable.addCell(totalCostPriceCell);
		}
		// Adding Grand Total
		wardTable.addCell(new PdfPCell(new Paragraph("  ")));
		wardTable.addCell(new PdfPCell(new Paragraph("  ")));
		wardTable.addCell(new PdfPCell(new Paragraph("  ")));
		wardTable.addCell(new PdfPCell(new Paragraph("  ")));
		wardTable.addCell(new PdfPCell(new Paragraph("  ")));
		wardTable.addCell(new PdfPCell(new Paragraph("Grand Total",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		PdfPCell grandTotal = new PdfPCell(new Paragraph(sumOfTotalCostPrice + "",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
		grandTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		wardTable.addCell(grandTotal);
		document.add(wardTable);
		document.close();
	}

	// fetchExpiredDrugDetails
	private static void createExpiryDrugDetailsPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {
		// step 1
		Document document = new Document();
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		PageNoForA4SizePdf event = new PageNoForA4SizePdf();
		writer.setPageEvent(event);
		// step 3
		document.open();

		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase heading = new Phrase("Expired Drug Details Report",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		heading.add(new Chunk(glue));
		heading.add("Generated On : " + simpDate.format(new Date()));
		document.add(heading);
		document.add(new Paragraph("  "));

		// For Filter Data
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));

		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));

		Map<String, String> wardTableHeadings = new LinkedHashMap<>();
		wardTableHeadings.put("1", "Drug Brand");
		wardTableHeadings.put("2", "Drug Type");
		wardTableHeadings.put("3", "Drug Name");
		wardTableHeadings.put("4", "Expired Stock Qty");
		wardTableHeadings.put("5", "Batch Number");
		wardTableHeadings.put("6", "Expiry Date");
		wardTableHeadings.put("7", "Total Cost Price");

		PdfPTable table = new PdfPTable(7); // columns.
		table.setWidths(new float[] { 1.2f, 1.2f, 1.5f, 0.5f, 1, 0.8f, 0.8f });
		table.setWidthPercentage(100);

		// Adding Table Headings only once
		for (Map.Entry<String, String> cellData : wardTableHeadings.entrySet()) {

			table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		}
		long sumOfTotalCostPrice = 0;
		// Adding PDf Body Data
		for (Map<String, String> data : invoiceData) {
			table.addCell(new PdfPCell(new Paragraph(data.get("drug_brand_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			table.addCell(new PdfPCell(new Paragraph(data.get("drug_type_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			table.addCell(new PdfPCell(new Paragraph(data.get("drug_nm"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			table.addCell(new PdfPCell(new Paragraph(data.get("stock_qty"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			table.addCell(new PdfPCell(new Paragraph(data.get("batch_no"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

			table.addCell(new PdfPCell(new Paragraph(data.get("expire_dt"),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));
			String totalCostPrice = data.get("total_cost_price").length() == 0 ? "0" : data.get("total_cost_price");
			sumOfTotalCostPrice += Long.parseLong(totalCostPrice);
			PdfPCell totalCostPriceCell = new PdfPCell(new Paragraph(totalCostPrice,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
			totalCostPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(totalCostPriceCell);
		}
		// Adding Grand Total
		table.addCell(new PdfPCell(new Paragraph("  ")));
		table.addCell(new PdfPCell(new Paragraph("  ")));
		table.addCell(new PdfPCell(new Paragraph("  ")));
		table.addCell(new PdfPCell(new Paragraph("  ")));
		table.addCell(new PdfPCell(new Paragraph("  ")));
		table.addCell(new PdfPCell(new Paragraph("Grand Total",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

		PdfPCell grandTotal = new PdfPCell(new Paragraph(sumOfTotalCostPrice + "",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
		grandTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(grandTotal);
		document.add(table);
		document.close();
	}

	// writeUserWiseDetailsPdfToResponse
	private static void createUserWiseDetailsPdf(ByteArrayOutputStream byteArrayOutputStream,
			List<Map<String, String>> invoiceData, InvoiceRequestBean invoiceRequestBean)
			throws IOException, DocumentException {
		// Document document = new Document();
		Document document = new Document(PageSize.A4.rotate(), 10f, 10f, 25f, 30f);
		// step 2
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		Rotate event = new Rotate();
		writer.setPageEvent(event);
		// step 3
		document.open();
		// Generic Date Converter dd-MM-yyyy
		SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");

		// Adding Report Heading and Today Date in single Line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Phrase heading = new Phrase("User wise Invoice Details",
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_HEADING));
		heading.add(new Chunk(glue));
		heading.add("Generated On : " + simpDate.format(new Date()));
		document.add(heading);
		document.add(new Paragraph("  "));

		// For Filter Data
		StringBuilder filterData = new StringBuilder();
		filterData.append("Filters >>> FromDate : " + simpDate.format(invoiceRequestBean.getFromDate()));
		filterData.append(", ToDate : " + simpDate.format(invoiceRequestBean.getToDate()));
		if (invoiceRequestBean.getEmpId() != null) {
			filterData.append(", User Name : " + invoiceData.get(0).get("employee_nm"));
		}
		// Adding Report Filters from User Search Criteria
		document.add(new Paragraph(filterData.toString(),
				FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_FILTER)));
		document.add(new Paragraph("  "));

		Map<String, String> wardTableHeadings = new LinkedHashMap<>();
		// wardTableHeadings.put("1", "Employee Name");
		wardTableHeadings.put("2", "Transaction Date");
		wardTableHeadings.put("3", "Invoice Type");
		wardTableHeadings.put("4", "Gross Amt");
		wardTableHeadings.put("5", "Discount");
		wardTableHeadings.put("6", "Advance Adj");
		wardTableHeadings.put("7", "Net Amt");

		wardTableHeadings.put("8", "Receipt Amt");
		wardTableHeadings.put("9", "Cash Amt");
		wardTableHeadings.put("10", "Card Amt");
		wardTableHeadings.put("11", "Cheque Amt");
		wardTableHeadings.put("12", "DD Amt");
		wardTableHeadings.put("13", "Other Amt");
		wardTableHeadings.put("14", "Total Due Amt");

		PdfPTable table = null; // columns.
		double grossAmount = 0.0;
		double discount = 0.0;
		double advanceAdj = 0.0;
		double netAmount = 0.0;
		double receiptAmount = 0.0;
		double cashAmount = 0.0;
		double cardAmount = 0.0;
		double chequeAmount = 0.0;
		double ddAmount = 0.0;
		double otherAmount = 0.0;
		double totalDueAmt = 0.0;

		boolean flagTwo = false;
		int listLastEle = invoiceData.size();
		int count = 1;
		// Adding PDf Body Data
		Map<String, String> empIdFlagMap = new LinkedHashMap<>();
		for (Map<String, String> data : invoiceData) {

			if (!empIdFlagMap.containsKey(data.get("created_usr_id")) && flagTwo) {
				table.addCell(alignCellDataToRightSideUtill(" "));
				table.addCell(alignCellDataToRightSideUtill("Grand Total:"));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", grossAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", discount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", advanceAdj)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", netAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", receiptAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", cashAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", cardAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", chequeAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", ddAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", otherAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", totalDueAmt)));

				grossAmount = 0.0;
				discount = 0.0;
				advanceAdj = 0.0;
				netAmount = 0.0;
				receiptAmount = 0.0;
				cashAmount = 0.0;
				cardAmount = 0.0;
				chequeAmount = 0.0;
				ddAmount = 0.0;
				otherAmount = 0.0;
				totalDueAmt = 0.0;
				document.add(table);
			}
			if (empIdFlagMap.put(data.get("created_usr_id"), "") == null) {
				document.add(new Phrase("  "));
				document.add(new Paragraph("Employe Name: " + data.get("employee_nm"),
						FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA + 2)));
				document.add(new Phrase("  "));
				table = new PdfPTable(13); // columns.
				table.setWidths(new float[] { 1.2f, 1.4f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1, 0.6f, 0.6f, 0.6f, 1 });
				table.setWidthPercentage(100);
				// Adding Table Headings only once
				for (Map.Entry<String, String> cellData : wardTableHeadings.entrySet()) {

					table.addCell(new PdfPCell(new Paragraph(cellData.getValue(),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA))));

				}
				createUserWiseDetailsPdf(table, data);
				grossAmount = grossAmount + readDataFromMap(data, "gross_amt");
				discount = discount + readDataFromMap(data, "discount");
				advanceAdj = advanceAdj + readDataFromMap(data, "advance_adj");
				netAmount = netAmount + readDataFromMap(data, "net_amt");
				receiptAmount = receiptAmount + readDataFromMap(data, "receipt_amt");
				cashAmount = cashAmount + readDataFromMap(data, "cash_amt");
				cardAmount = cardAmount + readDataFromMap(data, "card_amt");
				chequeAmount = chequeAmount + readDataFromMap(data, "cheque_amt");
				ddAmount = ddAmount + readDataFromMap(data, "dd_amt");
				otherAmount = otherAmount + readDataFromMap(data, "other_amt");
				totalDueAmt = totalDueAmt + readDataFromMap(data, "total_due_amt");
			} else {
				grossAmount = grossAmount + readDataFromMap(data, "gross_amt");
				discount = discount + readDataFromMap(data, "discount");
				advanceAdj = advanceAdj + readDataFromMap(data, "advance_adj");
				netAmount = netAmount + readDataFromMap(data, "net_amt");
				receiptAmount = receiptAmount + readDataFromMap(data, "receipt_amt");
				cashAmount = cashAmount + readDataFromMap(data, "cash_amt");
				cardAmount = cardAmount + readDataFromMap(data, "card_amt");
				chequeAmount = chequeAmount + readDataFromMap(data, "cheque_amt");
				ddAmount = ddAmount + readDataFromMap(data, "dd_amt");
				otherAmount = otherAmount + readDataFromMap(data, "other_amt");
				totalDueAmt = totalDueAmt + readDataFromMap(data, "total_due_amt");
				createUserWiseDetailsPdf(table, data);
			}

			flagTwo = true;
			/* For Adding Last Employee Details */
			if (listLastEle == count) {
				table.addCell(alignCellDataToRightSideUtill(" "));
				table.addCell(alignCellDataToRightSideUtill("Grand Total:"));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", grossAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", discount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", advanceAdj)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", netAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", receiptAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", cashAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", cardAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", chequeAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", ddAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", otherAmount)));
				table.addCell(alignCellDataToRightSideUtill(String.format("%.2f", totalDueAmt)));
				document.add(table);

			}
			count++;
		}

		document.close();
	}

	private static void createUserWiseDetailsPdf(PdfPTable table, Map<String, String> data) {

		table.addCell(setFontSizeAndFontStyleUtill(data.get("transaction_dt")));
		String invoiveType = data.get("invoice_type");
		if ("GEN".equalsIgnoreCase(data.get("invoice_type"))) {
			invoiveType = "OP Bills";
		} else if ("IPD".equalsIgnoreCase(data.get("invoice_type"))) {
			invoiveType = "IPD Bills";
		}
		table.addCell(setFontSizeAndFontStyleUtill(invoiveType));
		table.addCell(alignCellDataToRightSideUtill(data.get("gross_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("discount")));
		table.addCell(alignCellDataToRightSideUtill(data.get("advance_adj")));
		table.addCell(alignCellDataToRightSideUtill(data.get("net_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("receipt_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("cash_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("card_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("cheque_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("dd_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("other_amt")));
		table.addCell(alignCellDataToRightSideUtill(data.get("total_due_amt")));
	}

	private static double readDataFromMap(Map<String, String> map, String key) {
		return stringToDouble(map.get(key));
	}

	/**
	 * It Takes String argument and returns the PdfCell object aligned Right
	 * Side
	 * 
	 * @author Srinivas Nangana
	 * @param cellValue
	 * @return PdfPCell
	 */
	private static PdfPCell alignCellDataToRightSideUtill(String cellValue) {
		PdfPCell pdfCell = new PdfPCell(
				new Paragraph(cellValue, FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
		pdfCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		return pdfCell;
	}

	/**
	 * It Takes the String argument and return the PdfPCell with TIMES_ROMAN
	 * font and 10 font size
	 * 
	 * @author Srinivas Nangana
	 * @param cellValue
	 * @return PdfPCell
	 */
	private static PdfPCell setFontSizeAndFontStyleUtill(String cellValue) {
		return new PdfPCell(
				new Paragraph(cellValue, FontFactory.getFont(FontFactory.TIMES_ROMAN, PDF_FONT_SIZE_FOR_TABLE_DATA)));
	}

	/**
	 * Converts the String to Double
	 * 
	 * @author Srinivas Nangana
	 * @param data
	 * @return double
	 */
	private static double stringToDouble(String data) {
		if (data == null || data.length() == 0) {
			return 0.0;
		} else {
			return Double.parseDouble(data);
		}
	}

	/********************************************************************************************
	 * Pie Chart Related Logic
	 *******************************************************************************************/

	/**
	 * Creates pieChart with out heading
	 * 
	 * @param document
	 * @param writer
	 * @param combinationOfHeadingsAndValues
	 * @throws DocumentException
	 */

	private static void createPieChart(Document document, PdfWriter writer,
			LinkedHashMap<String, Double> combinationOfHeadingsAndValues) throws DocumentException {
		System.out.println(combinationOfHeadingsAndValues);
		/* Create Pie Chart */
		DefaultPieDataset coloredPieChart = new DefaultPieDataset();
		for (Map.Entry<String, Double> data : combinationOfHeadingsAndValues.entrySet()) {
			coloredPieChart.setValue(data.getKey(), data.getValue());
		}

		JFreeChart myColoredChart = ChartFactory.createPieChart("", coloredPieChart, true, true, false);
		/* Set main chart body color to white */
		myColoredChart.getPlot().setBackgroundPaint(Color.white);
		/* Color Pie Chart */
		PiePlot colorConfigurator = (PiePlot) myColoredChart.getPlot();
		/* For Changing Font styles of Chart labels */

		colorConfigurator.setLabelFont(new Font(FONT_STYLE_PIECHART_NAME, Font.BOLD, 8));

		/*
		 * GraphicsEnvironment graphicsEnvironment =
		 * GraphicsEnvironment.getLocalGraphicsEnvironment(); String fontNames[]
		 * = graphicsEnvironment.getAvailableFontFamilyNames();
		 * 
		 * for(String name:fontNames){ System.out.println(name); }
		 */
		colorConfigurator.setLabelBackgroundPaint(Color.WHITE);
		colorConfigurator.setLabelOutlinePaint(new Color(255, 255, 255));
		colorConfigurator.setLabelShadowPaint(new Color(255, 255, 255));
		// colorConfigurator.setBaseSectionOutlinePaint(new Color(0, 255, 0));

		List<Color> colorRGBDataList = getRGBValuesForChats(combinationOfHeadingsAndValues.size());
		int colurCount = 0;
		for (Map.Entry<String, Double> data : combinationOfHeadingsAndValues.entrySet()) {

			colorConfigurator.setSectionPaint(data.getKey(), colorRGBDataList.get(colurCount++));
		}

		/*
		 * We have to insert this colored Pie Chart into the PDF file using
		 * iText(lowagie) now
		 */

		PdfContentByte pdfContentByteAddChartContent = writer.getDirectContent();
		PdfTemplate templateChartHolder = pdfContentByteAddChartContent.createTemplate(pieChartWidth, pieChartHeight);
		Graphics2D chartGraphics = templateChartHolder.createGraphics(pieChartWidth, pieChartHeight,
				new DefaultFontMapper());
		Rectangle2D chartRegion = new Rectangle2D.Double(0, 0, pieChartWidth, pieChartHeight);
		myColoredChart.draw(chartGraphics, chartRegion);
		chartGraphics.dispose();

		Image chartImage = Image.getInstance(templateChartHolder);
		chartImage.setAlignment(Element.ALIGN_CENTER);
		document.add(chartImage);

	}

	/**
	 * This Method returns field howManyColors those many colors
	 * 
	 * @param howManyColors
	 * @return List<Color>
	 */
	private static List<Color> getRGBValuesForChats(int howManyColors) {

		List<Color> colorPool = new ArrayList<>();
		colorPool.add(new Color(233, 62, 62));
		colorPool.add(new Color(237, 174, 48));
		colorPool.add(new Color(0, 183, 74));
		colorPool.add(new Color(47, 169, 213));
		colorPool.add(new Color(189, 0, 0));
		colorPool.add(new Color(236, 125, 0));
		colorPool.add(new Color(0, 116, 47));
		colorPool.add(new Color(0, 142, 190));

		colorPool.add(new Color(210, 245, 60));// Lime
		colorPool.add(new Color(170, 110, 40));// Brown
		colorPool.add(new Color(128, 0, 0));// Maroon
		colorPool.add(new Color(128, 128, 0));// Olive
		colorPool.add(new Color(255, 225, 25));// Blue

		colorPool.add(new Color(221, 133, 199));
		colorPool.add(new Color(135, 91, 186));
		colorPool.add(new Color(208, 219, 151));
		colorPool.add(new Color(182, 23, 75));
		colorPool.add(new Color(85, 12, 24));
		colorPool.add(new Color(238, 66, 102));
		colorPool.add(new Color(212, 210, 165));

		colorPool.add(new Color(46, 14, 2));
		colorPool.add(new Color(254, 97, 0));
		colorPool.add(new Color(31, 1, 185));
		colorPool.add(new Color(129, 191, 140));
		colorPool.add(new Color(255, 248, 153));
		colorPool.add(new Color(241, 255, 250));
		colorPool.add(new Color(239, 222, 205));
		colorPool.add(new Color(105, 89, 88));
		colorPool.add(new Color(186, 224, 116));

		for (int i = colorPool.size(); i <= howManyColors; i++) {
			colorPool.add(new Color(getColorRandomNumber(), getColorRandomNumber(), getColorRandomNumber()));
		}

		return colorPool;

	}

	/**
	 * Returns Random numbers between 0 t0 256
	 * 
	 * @return int
	 */
	private static int getColorRandomNumber() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(255) + 1;
		return randomInt;
	}
}
