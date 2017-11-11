package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileHandler {

	public XSSFSheet sheet;
	public FileInputStream fis = null;
	public XSSFWorkbook workbook;
	public XSSFRow row;
	public XSSFCell Cell;
	String excelFilePath;
	public Object[][] excelDataSet = null;
	
	File file;
	
	public ExcelFileHandler(String pathToFile){
		this.excelFilePath = pathToFile;
		try{
			file = new File (excelFilePath);
			fis = new FileInputStream(file);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public Object[][] retrieveDataStored (String excelSheetName){
		try {
//			workbook = new HSSFWorkbook();		// For .xls excel file extension
				this.workbook = new XSSFWorkbook(fis);
				sheet = workbook.getSheet(excelSheetName);
				int ci, cj;
				int rowCount = sheet.getLastRowNum();
				int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
				excelDataSet = new Object[rowCount][totalCols-1];
				ci = 0;
				for (int i = 0; i <= rowCount; i++, ci++) {
					cj = 0;
					for (int j = 0; j <= totalCols; j++, j++) {
						excelDataSet[ci][cj] = getCellData(i,j);
					}
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return excelDataSet;
	}
	
	@SuppressWarnings("deprecation")
	private String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			Cell = sheet.getRow(RowNum).getCell(ColNum);
			int dataType = Cell.getCellType();
			if (dataType == XSSFCell.CELL_TYPE_NUMERIC) {
				int i = (int) Cell.getNumericCellValue();
				return Integer.toString(i);
			} else {
				String CellData = Cell.getStringCellValue();
				return CellData;
			}
		}
			catch (Exception e) {
				throw (e);
			}
		}
	}

