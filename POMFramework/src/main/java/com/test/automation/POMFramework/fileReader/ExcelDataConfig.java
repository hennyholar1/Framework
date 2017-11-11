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
	
	public String getData(String excelSheetIndex, int rows, int cols) {
		sheetIndex = wb.getSheet(excelSheetIndex);
		row = sheetIndex.getLastRowNum();
		row = (row + 1);
		col = sheetIndex.getRow(0).getPhysicalNumberOfCells();
		col = (col+1);
		String data = sheetIndex.getRow(rows).getCell(cols).getStringCellValue();
		
		return data;
	}
	
	/*
	public int getExcelSheetIndexForRow(int excelSheetIndex){
		 row = wb.getSheetAt(excelSheetIndex).getLastRowNum();
		 row = (row + 1);
		return row;
	}
	
	
// Try and understand Bhanu excel reader Data Driven style for row & Column	
	public int getExcelSheetIndexForColumn(){
//		Row row = sheet.getRow(rowNum).getLastCellNum();
//		 col = sheetIndex.getRow(0).getLastCellNum();
//		int col = sheetIndex.getRow(0).getPhysicalNumberOfCells(); another way of getting last column in a 
		return (col+1);
	}
	*/
}
