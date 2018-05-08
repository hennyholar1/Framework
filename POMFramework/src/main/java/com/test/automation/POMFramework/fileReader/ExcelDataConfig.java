package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataConfig {
	XSSFWorkbook wb;
//	HSSFWorkbook wbook;		// For .xls file extension
	XSSFSheet sheetIndex; // For .xlsx file extension
	int row;
	int col;
	
	public ExcelDataConfig(String pathToExcelFile){
		try{
			File src = new File (pathToExcelFile);
			FileInputStream fis = new FileInputStream(src);
	//		wbook = new HSSFWorkbook();		// For .xls excel file extension
			wb = new XSSFWorkbook(fis);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
//	selecting a particular cell value in a spreadsheet	
	public String getData(String excelSheet, int rows, int cols) {
		sheetIndex = wb.getSheet(excelSheet);
		row = sheetIndex.getLastRowNum();
		row = (row + 1);
		col = sheetIndex.getRow(0).getPhysicalNumberOfCells();
		col = (col+1);
		String data = sheetIndex.getRow(rows).getCell(cols).getStringCellValue();
		
		return data;
	}
	
}
