package POMFrameWork.src.main.java.com.test.automation.POMFramework.fileReader;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * @author Novenox
 */
public class SpreadSheetHandler {

	private static final Logger logger = LogFunction.getLogger(SpreadSheetHandler.class);
	public FileInputStream file = null;
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	protected String pathToWorkbookLocation = null;
	protected XSSFRow row = null;
	protected XSSFCell cell = null;
	protected Row rows = null;
	protected Cell cells = null;
	

	/**
	 * This is a constructor that user will use to create an instance of this class and,
	 * enter the path to the excel/spreadsheet workbook with the spreadsheet (sheet) name.
	 * 
	 * @param workBookWithItsLocationPath
	 * @param excelSheetName
	 */
	public SpreadSheetHandler (String workBookWithItsLocationPath, String excelSheetName) {
		this.pathToWorkbookLocation = workBookWithItsLocationPath;
		try {
			file = new FileInputStream(new File(workBookWithItsLocationPath));
			workbook = new XSSFWorkbook(file);
			sheet = workbook.getSheet(excelSheetName);
			file.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This will enable user to do data-driven with independent unique spreadsheet workbook sheet, 
	 * and data pulling/driven will start from the second row because the first row will contain the column titles
	 * 
	 * @return
	 */
	public Object[][] getSpreadSheetData () {
		Object[][] dataSets = null;
		try {
			int totalRow = sheet.getLastRowNum();
			int totalColumn = sheet.getRow(0).getLastCellNum();
			dataSets = new String[totalRow][totalColumn];

			for (int i = 1; i <= totalRow; i++) {
				row = sheet.getRow(i);
				for (int j = 0; j < totalColumn; j++) {
					cell = row.getCell(j);

					switch (cell.getCellTypeEnum()) {

					case NUMERIC:
						dataSets[i-1][j] = String.valueOf(cell.getNumericCellValue());
						break;

					case STRING:
						dataSets[i-1][j] = cell.getStringCellValue();
						break;

					case BOOLEAN:
						dataSets[i-1][j] = String.valueOf(cell.getBooleanCellValue());
						break;

					case FORMULA:
						dataSets[i-1][j] = String.valueOf(cell.getCellFormula());
						break;

					case BLANK:
						break;

					default:
						logger.info("Not a matching data type");
						break;
					}
				}
			}
			return dataSets;
		} catch (Exception e) {
			logger.info("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
			e.printStackTrace();
		}
		return dataSets;
	}
	
	/**
	 * This will enable user to do data-driven using unique spreadsheet workbook sheet, and data pulling/driven
	 * will start from the second row because the first row will contain the column title with the "start" word 
	 * that will ensure the write data is being used for data driven test.
	 * 
	 * @return
	 */
	public Object[][] getSpreadSheetData (String firstRowAndFirstColumnName) {
		Object[][] dataSets = null;
		try {
			int totalRow = sheet.getLastRowNum();
			int totalColumn = sheet.getRow(0).getLastCellNum();
			dataSets = new String[totalRow][totalColumn];
			Iterator<Row> rowIterator = sheet.iterator();
			int i = 0;
			while (rowIterator.hasNext()) {
				rows = rowIterator.next();
				Iterator<Cell> cellIterator = rows.cellIterator();
				int j = 0;

				while (cellIterator.hasNext()) {
					cells = cellIterator.next();

					if (cells.getStringCellValue().equalsIgnoreCase(firstRowAndFirstColumnName)) {
						i = 0;
						break;
					}

					switch (cells.getCellTypeEnum()) {
					case STRING:
						dataSets[i-1][j++] = cells.getStringCellValue();
						break;

					case NUMERIC:
						dataSets[i-1][j++] = String.valueOf(cells.getNumericCellValue());
						break;

					case BOOLEAN:
						dataSets[i-1][j++] = String.valueOf(cells.getBooleanCellValue());
						break;

					case FORMULA:
						dataSets[i-1][j++] = String.valueOf(cells.getCellFormula());
						break;
						
					case BLANK:
						break;
						
					default:
						logger.info("Not a matching data type");
						break;
					}
				}
				i++;
			}
			return dataSets;
		} catch (Exception e) {
			logger.info("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
			e.printStackTrace();
		}
		return dataSets;
	}
	
	/**
	 * This will enable user to use one spreadsheet page for all data input
	 * created separately on a sheet
	 * 
	 * @param firstRowFirstColumn
	 * @return
	 */
	public Object[][] getDataBasedOnFirstColumnWithStart (String testDataName) {
		Object[][] dataSets = null;	
		try {	
		  int totalRow = sheet.getLastRowNum(); 
		  int totalColumn = 0;
		  Iterator<Row> rowsIterator = sheet.iterator(); 
		  int i = 0; 
		  int count = 1;
		  
		  while (rowsIterator.hasNext() && (count == 1 || count == 2)) {  
			  rows = rowsIterator.next(); 
			  Iterator<Cell> cellIterator = rows.cellIterator();  
			  int  j = 0;
			  while (cellIterator.hasNext()) { 
				  cells = cellIterator.next();
			  
			  if (cells.toString().equalsIgnoreCase(testDataName + "end")) { 
				  count  = 0; 	
				  break; }
			  
			  if (cells.toString().equalsIgnoreCase(testDataName + "start")) {
			  totalColumn = (rows.getPhysicalNumberOfCells() - 1); 
			  dataSets = new String[totalRow][totalColumn]; }
			  
			  if (cells.toString().equalsIgnoreCase(testDataName + "start") || count == 2) { 
				  count = 2;
				  switch (cells.getCellTypeEnum()) {
					  case STRING: 
						  if (!cells.toString().equalsIgnoreCase(testDataName + "start"))
						  { dataSets[i-1][j++] = cells.getStringCellValue();  }
						  break;
						  
					  case NUMERIC: 
						  dataSets[i-1][j++] = String.valueOf(cells.getNumericCellValue());
						  break;
					  
					  case BOOLEAN: 
						  dataSets[i-1][j++] = String.valueOf(cells.getBooleanCellValue());
						  break;
					  
					  case FORMULA: 
						  dataSets[i-1][j++] = String.valueOf(cells.getCellFormula()); 
						  break;
						  
					  case BLANK:
							break;
					  
					  default: 
						  logger.info("Not a matching enum data type"); 
						  break;
					  		}  
				  		} 
				  	} 
			  	i++; 
		  		}
		  	return parseData(dataSets, totalColumn); 
		  } catch (Exception e)  { 
			  logger.info("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
			  e.printStackTrace(); 
			  }
	  return dataSets; 
	  }
	 
	 
	/**
	 * This method will remove unwanted null/space data from the user selected/chosen sheet which contains "start" word.
	 * 
	 * @param incomingData
	 * @param columnSize
	 * @return
	 */
	public Object[][] parseData(Object[][] incomingData, int columnSize) {
		ArrayList<ArrayList<String>> outerDatalist = new ArrayList<ArrayList<String>>();
		ArrayList<String> innerDatalist;

		for (int i = 0; i < incomingData.length; i++) {
			innerDatalist = new ArrayList<>();
			for (int j = 0; j < incomingData[i].length; j++) {
				if (incomingData[i][j] != null) {
					innerDatalist.add((String) incomingData[i][j]);
				}
			}
			 if (!innerDatalist.isEmpty()) {
			//	 if (innerDatalist.size() > 0) {
				 outerDatalist.add(innerDatalist);
			}
		}

		Object[][] array2d = new Object[outerDatalist.size()][columnSize];
		for (int k = 0; k < outerDatalist.size(); k++) {
			ArrayList<String> newArray = outerDatalist.get(k);
			for (int l = 0; l < newArray.size(); l++) {
				array2d[k][l] = newArray.get(l);
			}
		}
		return array2d;
	}

	/**
	 * This method is used to retrieve data from the spreadsheet based on the column name and row number. 
	 * The row number starts from 1, unlike the computer 0 index for row or column 1.
	 * 
	 * @param columnName
	 * @param rowNumber
	 * @return
	 */
	public Object getSpreadsheetCellData(String columnName, int rowNumber) {
		try {
			String cellValue = null;
			row = sheet.getRow(0);
			int colNum = 0;
			
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
					colNum = i;
					break;
				}
			}
			
			row = sheet.getRow(rowNumber - 1);
			cell = row.getCell(colNum);
			
			if (cell.getCellTypeEnum() == CellType.STRING) 
				return cell.getStringCellValue();

			else if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
				cellValue = String.valueOf(cell.getNumericCellValue());
				if(HSSFDateUtil.isCellDateFormatted(cell)) {
					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					Date date = cell.getDateCellValue();
					cellValue = dateFormat.format(date);
				}
				return cellValue;
					
			} else if (cell.getCellTypeEnum() == CellType.BLANK) {
				return "";
			}
			
			else {
				return String.valueOf(cell.getBooleanCellValue());
			}

		} catch (Exception e) {
			logger.info("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
			return "No Matching value";
		}
	}

	/**
	 * This method is meant to update the test result
	 * 
	 * @param testCaseName
	 * @param testStatus
	 */
	public void updateResult(String testCaseName, String testStatus) {
		try {
			int totalRow = sheet.getLastRowNum();
			for (int i = 1; i <= totalRow; i++) {
				row = sheet.getRow(i);
				String cel = row.getCell(0).getStringCellValue();
				if (cel.contains(testCaseName)) {
					row.createCell(1).setCellValue(testStatus);
					file.close();
					FileOutputStream writeOutToFile = new FileOutputStream(new File(pathToWorkbookLocation));
					workbook.write(writeOutToFile);
					writeOutToFile.close();
					break;
				}
			}
		} catch (Exception e) {
			logger.info("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
		}
	}
}
