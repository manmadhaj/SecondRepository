package com.drucare.reports.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelCreatinUtill {
	public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
	private static final Logger logger = LoggerFactory.getLogger(ExcelCreatinUtill.class);

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

	public static HttpServletResponse prepareExcelReport(HttpServletResponse response,
			List<Map<String, Object>> firstSheetData, Map<String, String> firstSheetHeadings, String reportName,
			Integer sheetCount, List<Map<String, Object>> secondSheetData, Map<String, String> secondSheetHeadings)
			throws IOException {

		logger.info("Enteted into This Method exportToInvoiceReportData stsrted Excel Creation Records:"
				+ firstSheetData.size());

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Invoice Data");

		// Create row object
		XSSFRow row;

		// For adding Headings on first row

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		int cellid = 0;
		for (Map.Entry<String, String> entry : firstSheetHeadings.entrySet()) {

			Cell cell = row.createCell(cellid++);
			cell.setCellValue(entry.getValue());
		}

		List<Map<String, String>> excelDataWithSNo = new ArrayList<>();
		// Adding S.No
		int sno = 1;
		for (Map<String, Object> obj : firstSheetData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, Object> entry : obj.entrySet()) {

				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue() + "");
			}
			excelDataWithSNo.add(mapWithSno);
		}
		// Adds The Data to Excel Sheet
		for (int i = 0; i < excelDataWithSNo.size(); i++) {
			row = spreadsheet.createRow(rowid++);

			int cellid1 = 0;
			for (Map.Entry<String, String> entry1 : excelDataWithSNo.get(i).entrySet()) {

				Cell cell = row.createCell(cellid1++);
				cell.setCellValue(entry1.getValue());

			}
		}

		if (sheetCount != null && sheetCount > 0) {
			creatingSecondWorkSheetForExistingExcel(workbook.createSheet("Invoice Summary"), secondSheetData,
					secondSheetHeadings);
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

	public static HttpServletResponse prepareExcelReportTwo(HttpServletResponse response,
			List<Map<String, Object>> firstSheetData, Map<String, String> firstSheetHeadings, String reportName,
			Integer sheetCount, List<Map<String, Object>> secondSheetData, Map<String, String> secondSheetHeadings)
			throws IOException {

		logger.info("Enteted into This Method exportToInvoiceReportData stsrted Excel Creation Records:"
				+ firstSheetData.size());

		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Invoice Data");

		// Create row object
		XSSFRow row;

		// For adding Headings on first row

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		int cellid = 0;
		for (Map.Entry<String, String> entry : firstSheetHeadings.entrySet()) {

			Cell cell = row.createCell(cellid++);
			cell.setCellValue(entry.getValue());
		}

		List<Map<String, String>> excelDataWithSNo = new ArrayList<>();
		// Adding S.No
		int sno = 1;
		for (Map<String, Object> obj : firstSheetData) {
			Map<String, String> mapWithSno = new LinkedHashMap<>();
			boolean flag = true;
			for (Map.Entry<String, Object> entry : obj.entrySet()) {

				if (flag) {
					mapWithSno.put("S No", (sno++) + "");
					flag = false;
				}
				mapWithSno.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue() + "");
			}
			excelDataWithSNo.add(mapWithSno);
		}
		// Adds The Data to Excel Sheet
		Double advanceAmt = 0.0;
		Double approximateAmt = 0.0;
		boolean flagOne = false;
		boolean flagTwo = false;
		System.out.println(excelDataWithSNo);
		for (int i = 0; i < excelDataWithSNo.size(); i++) {
			row = spreadsheet.createRow(rowid++);

			int cellid1 = 0;
			for (Map.Entry<String, String> entry1 : excelDataWithSNo.get(i).entrySet()) {

				Cell cell = row.createCell(cellid1++);
				cell.setCellValue(entry1.getValue());
				System.out.println(entry1.getKey() + ":" + entry1.getValue());
				if ("advance_amt".equals(entry1.getKey().trim().toLowerCase())) {
					advanceAmt = entry1.getValue().length() > 0 ? Double.parseDouble(entry1.getValue()) : 0.0;
					flagOne = true;
				}
				if ("approximate_amt".equals(entry1.getKey().trim().toLowerCase())) {
					approximateAmt = entry1.getValue().length() > 0 ? Double.parseDouble(entry1.getValue()) : 0.0;
					flagTwo = true;
				}
				if (flagOne && flagTwo) {
					Cell cell1 = row.createCell(cellid1++);
					cell1.setCellValue((approximateAmt - advanceAmt) + "");
					flagTwo = false;
					flagOne = false;
				}
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

	private static void creatingSecondWorkSheetForExistingExcel(XSSFSheet spreadsheet,
			List<Map<String, Object>> secondSheetData, Map<String, String> secondSheetHeadings) {
		// Create row object
		XSSFRow row;

		int rowid = 0;
		row = spreadsheet.createRow(rowid++);

		int cellid = 0;
		for (Map.Entry<String, String> entry : secondSheetHeadings.entrySet()) {

			Cell cell = row.createCell(cellid++);
			cell.setCellValue(entry.getValue());
		}

		// Adds The Data to Excel Sheet
		for (int i = 0; i < secondSheetData.size(); i++) {
			row = spreadsheet.createRow(rowid++);

			int cellid1 = 0;
			for (Map.Entry<String, Object> entry1 : secondSheetData.get(i).entrySet()) {

				Cell cell = row.createCell(cellid1++);
				cell.setCellValue(entry1.getValue() == null ? " " : entry1.getValue() + "");

			}
		}
	}

	/**
	 * Auto increments the column size based on data
	 */
	private static void autoSizeColumns(XSSFWorkbook workbook) {
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
}
