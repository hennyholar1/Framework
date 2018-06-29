package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadDataFromExcelSheet {
		
	public String path;
	public XSSFSheet sheet;
	public FileInputStream fis;
	public XSSFWorkbook workbook;
	public XSSFRow row;
	public XSSFCell cell;
	
	public ReadDataFromExcelSheet(String path) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ReadDataFromExcelSheet() {

		try {
			fis = new FileInputStream(this.path);
			workbook = new XSSFWorkbook(fis);
			}

		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This will enable user to do data-driven with independent unique spreadsheet workbook sheet
	 * 
	 * @param sheetName
	 * @param workbookName
	 * @return
	 */
//	@SuppressWarnings({ })
	public String[][] getDataFromSpreadSheet(String sheetName, String workbookName) {

		String dataSets[][] = null;

			try {
				// get sheet from excel workbook
				XSSFSheet sheet = workbook.getSheet(sheetName);

				// count number of active tows
				int totalRow = sheet.getLastRowNum() + 1;

				// count number of active columns in row
				int totalColumn = sheet.getRow(0).getLastCellNum();

				// Create array of rows and column
				dataSets = new String[totalRow - 1][totalColumn];

				// Run for loop and store data in 2D array
				
					// This for loop will run on rows
				for (int i = 1; i < totalRow; i++) {

					XSSFRow rows = sheet.getRow(i);	

					// This for loop will run on columns of that row
					for (int j = 0; j < totalColumn; j++) {

						// get Cell method will get cell
						XSSFCell cell = rows.getCell(j);

						// If cell of type String , then this if condition will work
						if (cell.getCellType() == Cell.CELL_TYPE_STRING)

							dataSets[i - 1][j] = cell.getStringCellValue();

						// If cell of type Number , then this if condition will work
						else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

							String cellText = String.valueOf(cell.getNumericCellValue());

							dataSets[i - 1][j] = cellText;	

						} else

							// If cell of type boolean , then this if condition will work
							dataSets[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
					}
				}

				return dataSets;

			} catch (Exception e) {

				System.out.println("Exception in reading xlxs file" + e.getMessage());
				e.printStackTrace();
			}

			return dataSets;
		}

		
	/**
	 * This will enable user to do data-driven with independent unique spreadsheet workbook sheet
	 * 
	 * @param excellocation
	 * @param sheetName
	 * @param rowTitle
	 * @return
	 */
	public String[][] getSpreadsheetData(String excelLocation, String sheetName, String FirstColumnText) {

		try {
			String dataSets[][] = null;

			FileInputStream file = new FileInputStream(new File(excelLocation));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			// count number of active rows
			int totalRow = sheet.getLastRowNum();

			// count number of active columns in row
			int totalColumn = sheet.getRow(0).getLastCellNum();

			// Create array of rows and column
			dataSets = new String[totalRow][totalColumn];

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			int i = 0;

			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				int j = 0;

				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getStringCellValue().contains(FirstColumnText)) {

						break;
					}

					// Check the cell type and format accordingly
					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_NUMERIC:

						dataSets[i - 1][j++] = cell.getStringCellValue();

						break;

					case Cell.CELL_TYPE_STRING:

						dataSets[i - 1][j++] = cell.getStringCellValue();

						break;

					case Cell.CELL_TYPE_BOOLEAN:

						dataSets[i - 1][j++] = cell.getStringCellValue();

						break;

					case Cell.CELL_TYPE_FORMULA:

						dataSets[i - 1][j++] = cell.getStringCellValue();

						System.out.println(cell.getStringCellValue());

						break;
					}
				}

				System.out.println("");

				i++;
			}

			file.close();

			return dataSets;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	
	/**
	 * This will enable user to use one spreadsheet page for all data input created separately on a sheet
	 * 
	 * @param excellocation
	 * @param sheetName
	 * @param testName
	 * @return
	 */
	public Object[][] getExcelDataBasedOnStartingPoint(String excellocation, String sheetName, String testName) {

		try {

			String dataSets[][] = null;

			FileInputStream file = new FileInputStream(new File(excellocation));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			// count number of active rows
			int totalRow = sheet.getLastRowNum();

			int totalColumn = 0;

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();

			int i = 0;

			int count = 1;

			while (rowIterator.hasNext() && count == 1 || count == 2) {
				
				Row row = rowIterator.next();

				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				int j = 0;

				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					if (cell.getStringCellValue().contains(testName + "end")) {

						count = 0;

						break;
					}

					if (cell.getStringCellValue().contains(testName + "start")) {

						// count number of active columns in row
						totalColumn = row.getPhysicalNumberOfCells() - 1;

						// Create array of rows and column
						dataSets = new String[totalRow][totalColumn];

					}

					if (cell.getStringCellValue().contains(testName + "start") || count == 2) {

						count = 2;

						// Check the cell type and format accordingly
						switch (cell.getCellType()) {

						case Cell.CELL_TYPE_NUMERIC:

							dataSets[i - 1][j++] = cell.getStringCellValue();

							break;

						case Cell.CELL_TYPE_STRING:

							if (!cell.getStringCellValue().contains(testName + "start")) {

								dataSets[i - 1][j++] = cell.getStringCellValue();
							}

							break;

						case Cell.CELL_TYPE_BOOLEAN:

							dataSets[i - 1][j++] = cell.getStringCellValue();

							break;

						case Cell.CELL_TYPE_FORMULA:

							dataSets[i - 1][j++] = cell.getStringCellValue();

							break;

						}
					}
				}

				System.out.println("");
				
				i++;
			}

			file.close();

			return parseData(dataSets, totalColumn);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	/**
	 * This method is used to retrieve data from the spreadsheet cell 
	 * 
	 * @param sheetName
	 * @param colName
	 * @param rowNum
	 * @return
	 */
	public String getSpreadsheetCellData(String sheetName, String colName, int rowNum) {
		
		try {
			int col_Num = 0;

			int index = workbook.getSheetIndex(sheetName);

			sheet = workbook.getSheetAt(index);

			XSSFRow row = sheet.getRow(0);

			for (int i = 0; i < row.getLastCellNum(); i++) {

				if (row.getCell(i).getStringCellValue().equals(colName)) {

					col_Num = i;

					break;
				}
			}

			row = sheet.getRow(rowNum - 1);

			XSSFCell cell = row.getCell(col_Num);

			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

				return cell.getStringCellValue();

			} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {

				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	
	/**
	 * This method is used to update the test result 
	 * 
	 * @param excellocation
	 * @param sheetName
	 * @param testCaseName
	 * @param testStatus
	 * @return
	 */
	public void updateResult(String excellocation, String sheetName, String testCaseName, String testStatus)
			throws IOException {

		try {
			FileInputStream file = new FileInputStream(new File(excellocation));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			// count number of active tows
			int totalRow = sheet.getLastRowNum() + 1;

			// count number of active columns in row
			for (int i = 1; i < totalRow; i++) {

				XSSFRow r = sheet.getRow(i);

				String ce = r.getCell(1).getStringCellValue();

				if (ce.contains(testCaseName)) {

					r.createCell(2).setCellValue(testStatus);

					file.close();

					System.out.println("result updated");

					FileOutputStream outFile = new FileOutputStream(new File(excellocation));

					workbook.write(outFile);

					outFile.close();

					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * This method is used to remove unwanted null/space data from array 
	 * 
	 * @param data
	 * @param colSize
	 * @return
	 */

	public Object[][] parseData(Object[][] data, int colSize) {

		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		// This array list will store one Array index data, every array index has three sets of data
		ArrayList<String> list1;

		// running for loop on array size
		for (int i = 0; i < data.length; i++) {

			list1 = new ArrayList<String>();

			// this for loop will run on array index, since each array index has
			// three sets of data
			for (int j = 0; j < data[i].length; j++) {

				// this if will check null
				if (data[i][j] != null) {

					list1.add((String) data[i][j]);
				}
			}

			// once all one array index data is entered in arrayList , then putting this object in parent arrayList
			if (list1.size() > 0) {

				list.add(list1);
			}
		}

		// convert array List Data into 2D Array
		Object[][] arr2d = new Object[list.size()][colSize];

		// run loop on array list data
		for (int i = 0; i < list.size(); i++) {

			// every array list index has arryList inside
			ArrayList<String> t = list.get(i);

			// run loop on inner array List
			for (int j = 0; j < t.size(); j++) {

				arr2d[i][j] = t.get(j);
			}
		}

		return arr2d;
	}
	
	

	public Object[][] getExcelData(String excelLocation, String sheetName) {

		try {
			Object dataSets[][] = null;
			FileInputStream file = new FileInputStream(new File(excelLocation));
			// Create Workbook instance
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get sheet Name from Workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);

			// count number of active rows in excel sheet
			int totalRow = sheet.getLastRowNum();
            System.out.println(totalRow);
			// count active columns in row
			int totalColumn = sheet.getRow(0).getLastCellNum();

			dataSets = new Object[totalRow][totalColumn-1];

			// Iterate Through each Rows one by one.
			Iterator<Row> rowIterator = sheet.iterator();
			int i = 0;
			while (rowIterator.hasNext()) {
				i++;
				// for Every row , we need to iterator over columns
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				int j = 0;
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					if (cell.getStringCellValue().contains("Start")) {
						i = 0;
						break;
					}
					switch (cell.getCellTypeEnum()) {
					case STRING:
						dataSets[i-1][j++] = cell.getStringCellValue();
						break;
					case NUMERIC:
						dataSets[i-1][j++] = cell.getNumericCellValue();
						break;
					case BOOLEAN:
						dataSets[i-1][j++] = cell.getBooleanCellValue();
					case FORMULA:
						dataSets[i-1][j++] = cell.getCellFormula();
						break;

					default:
						System.out.println("no matching enum date type found");
						break;
					}
				}
			}
			return dataSets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateResult(String excelLocation, String sheetName, String testCaseName, String testStatus){
		try{
			FileInputStream file = new FileInputStream(new File(excelLocation));
			// Create Workbook instance
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get sheet Name from Workbook
			XSSFSheet sheet = workbook.getSheet(sheetName);
			// count number of active rows in excel sheet
			int totalRow = sheet.getLastRowNum()+1;
			for(int i =1; i<totalRow; i++){
				XSSFRow r = sheet.getRow(i);
				String ce = r.getCell(0).getStringCellValue();
				if(ce.contains(testCaseName)){
					r.createCell(1).setCellValue(testStatus);
					file.close();
					log.info("result updated..");
					FileOutputStream out = new FileOutputStream(new File(excelLocation));
					workbook.write(out);
					out.close();
					break;
				}
			}
		}
		catch(Exception e){
			
		}
	}
	
	public static void main(String[] args) {
	 ExcelHelper	 excelHelper = new ExcelHelper();
	 String excelLocation = ResourceHelper.getResourcePath("src/main/resources/configfile/testData.xlsx");
	 Object[][] data = excelHelper.getExcelData(excelLocation, "loginData");
	 //System.out.println(data);
//	 excelHelper.updateResult(excelLocation, "TestScripts", "Login", "PASS");
//	 excelHelper.updateResult(excelLocation, "TestScripts", "Registration", "FAIL");
//	 excelHelper.updateResult(excelLocation, "TestScripts", "Add to Cart", "PASS");
	 
	}
}

}
