package POMFrameWork.src.main.java.com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Novenox
 */
@SuppressWarnings("javadoc")
public class SpreadSheetHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpreadSheetHandler.class);

    public FileInputStream file = null;
    public XSSFWorkbook workbook = null;
    public XSSFSheet sheet = null;
    protected String pathToWorkbookLocation = null;
    protected XSSFRow row = null;
    protected XSSFCell cell = null;
    protected Row rows = null;
    protected Cell cells = null;

    public SpreadSheetHandler(final String workBook, final String excelSheet, final boolean isSheetTypeIndex) {
        this.pathToWorkbookLocation = workBook;
        try {
            file = new FileInputStream(new File(workBook));
            workbook = new XSSFWorkbook(file);
            if (isSheetTypeIndex) {
                final int index = Integer.parseInt(excelSheet);
                sheet = workbook.getSheetAt(index);
            } else {
                sheet = workbook.getSheet(excelSheet);
            }
            file.close();
        } catch (final Exception e) {
            LOGGER.error("Error Occured", e);
        }
    }

    /**
     * This will enable user to use one spreadsheet page for all data input created separately on a sheet,
     *  but none of the fields row data should be null apart from the column that contains the word "start"
     */
    public Object[][] getDataBasedOnFirstColumnWithStart(final String testDataName) {
        Object[][] dataSets = null;
        try {
            final int totalRow = sheet.getLastRowNum();
            int totalColumn = 0;
            final Iterator<Row> rowsIterator = sheet.iterator();
            int i = 0;
            int count = 1;

            while (rowsIterator.hasNext() && (count == 1 || count == 2)) {
                rows = rowsIterator.next();
                final Iterator<Cell> cellIterator = rows.cellIterator();
                int j = 0;
                while (cellIterator.hasNext()) {
                    cells = cellIterator.next();

                    if (cells.toString().equalsIgnoreCase(testDataName + "end")) {
                        count = 0;
                        break;
                    }

                    if (cells.toString().equalsIgnoreCase(testDataName + "start")) {
                        totalColumn = (rows.getPhysicalNumberOfCells() - 1);
                        dataSets = new String[totalRow][totalColumn];
                    }

                    if (cells.toString().equalsIgnoreCase(testDataName + "start") || count == 2) {
                        count = 2;
                        switch (cells.getCellTypeEnum()) {
                            case STRING:
                                if (!cells.toString().equalsIgnoreCase(testDataName + "start")) {
                                    dataSets[i - 1][j++] = cells.getStringCellValue();
                                }
                                break;

                            case NUMERIC:
                                dataSets[i - 1][j++] = String.valueOf(cells.getNumericCellValue());
                                break;

                            case BOOLEAN:
                                dataSets[i - 1][j++] = String.valueOf(cells.getBooleanCellValue());
                                break;

                            case FORMULA:
                                dataSets[i - 1][j++] = String.valueOf(cells.getCellFormula());
                                break;

                            case BLANK:
                                break;

                            default:
                                LOGGER.warn("Not a matching enum data type");
                                break;
                        }
                    }
                }
                i++;
            }
            return parseData(dataSets, totalColumn);
        } catch (final Exception e) {
            LOGGER.error("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
        }
        return dataSets;
    }

    /**
     * This method is used to retrieve data from the spreadsheet based on the column name and row number.
     * The row number starts from 1, unlike the computer 0 index for row or column 1.
     */
    public Object getSpreadsheetCellData(final String columnName, final int rowNumber) {
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

            if (cell.getCellTypeEnum() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
                cellValue = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    final Date date = cell.getDateCellValue();
                    cellValue = dateFormat.format(date);
                }
                return cellValue;

            } else if (cell.getCellTypeEnum() == CellType.BLANK) {
                return "";
            }

            else {
                return String.valueOf(cell.getBooleanCellValue());
            }

        } catch (final Exception e) {
            LOGGER.error("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
            return "No Matching value";
        }
    }

    /**
     * This will enable user to do data-driven with independent unique spreadsheet workbook sheet,
     * and data pulling/driven will start from the second row because the first row will contain the column titles
     */
    public Object[][] getSpreadSheetData() {
        Object[][] dataSets = null;
        try {
            final int totalRow = sheet.getLastRowNum();
            final int totalColumn = sheet.getRow(0).getLastCellNum();
            dataSets = new String[totalRow][totalColumn];

            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                for (int j = 0; j < totalColumn; j++) {
                    cell = row.getCell(j);

                    switch (cell.getCellTypeEnum()) {

                        case NUMERIC:
                            dataSets[i - 1][j] = String.valueOf(cell.getNumericCellValue());
                            break;

                        case STRING:
                            dataSets[i - 1][j] = cell.getStringCellValue();
                            break;

                        case BOOLEAN:
                            dataSets[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
                            break;

                        case FORMULA:
                            dataSets[i - 1][j] = String.valueOf(cell.getCellFormula());
                            break;

                        case BLANK:
                            if (dataSets[i - 1][j] == null) {
                                dataSets[i - 1][j] = "";
                            } // To convert a spreadsheet value from null to "" .
                            break;

                        default:
                            LOGGER.info("Not a matching data type");
                            break;
                    }
                }
            }
            return dataSets;
        } catch (final Exception e) {
            LOGGER.error("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
        }
        return dataSets;
    }

    /**
     * This will enable user to do data-driven using unique spreadsheet workbook sheet, and data pulling/driven will start
     * from the second row because the first row will contain the column title and used it to determine where to start from.
     */
    public Object[][] getSpreadSheetData(final String firstRowAndFirstColumnName) {
        Object[][] dataSets = null;
        try {
            final int totalRow = sheet.getLastRowNum();
            final int totalColumn = sheet.getRow(0).getLastCellNum();
            dataSets = new String[totalRow][totalColumn];
            final Iterator<Row> rowIterator = sheet.iterator();
            int i = 0;
            while (rowIterator.hasNext()) {
                rows = rowIterator.next();
                final Iterator<Cell> cellIterator = rows.cellIterator();
                int j = 0;

                while (cellIterator.hasNext()) {
                    cells = cellIterator.next();

                    if (cells.getStringCellValue().equalsIgnoreCase(firstRowAndFirstColumnName)) {
                        i = 0;
                        break;
                    }

                    switch (cells.getCellTypeEnum()) {
                        case STRING:
                            dataSets[i - 1][j++] = cells.getStringCellValue();
                            break;

                        case NUMERIC:
                            dataSets[i - 1][j++] = String.valueOf(cells.getNumericCellValue());
                            break;

                        case BOOLEAN:
                            dataSets[i - 1][j++] = String.valueOf(cells.getBooleanCellValue());
                            break;

                        case FORMULA:
                            dataSets[i - 1][j++] = String.valueOf(cells.getCellFormula());
                            break;

                        case BLANK:
                            if (dataSets[i - 1][j++] == null) {
                                dataSets[i - 1][j++] = "";
                            } // To convert a spreadsheet value from null to "" .
                            break;

                        default:
                            LOGGER.info("Not a matching data type");
                            break;
                    }
                }
                i++;
            }
            return dataSets;
        } catch (final Exception e) {
            LOGGER.error("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
        }
        return dataSets;
    }

    public Object[][] getTestData() {

        final int totalRow = sheet.getLastRowNum();
        final int totalColumn = sheet.getRow(0).getLastCellNum();

        final Object[][] dataSets = new Object[totalRow][1];
        HashMap<Object, Object> tableData = null;
        for (int i = 0; i < totalRow; i++) {
            tableData = new HashMap<>();
            for (int j = 0; j < totalColumn; j++) {
                tableData.put(sheet.getRow(0).getCell(j).toString(), sheet.getRow(i + 1).getCell(j).toString());
            }
            dataSets[i][0] = tableData;
        }
        return dataSets;
    }

    /**
     * This method will remove unwanted null/space data from the user selected/chosen sheet which contains "start" word.
    */
    public Object[][] parseData(final Object[][] incomingData, final int columnSize) {
        final ArrayList<ArrayList<String>> outerDatalist = new ArrayList<ArrayList<String>>();
        ArrayList<String> innerDatalist;

        for (final Object[] element : incomingData) {
            innerDatalist = new ArrayList<>();
            for (final Object element2 : element) {
                if (element2 != null) {
                    innerDatalist.add((String) element2);
                }
            }
            if (!innerDatalist.isEmpty()) {
                // if (innerDatalist.size() > 0) {
                outerDatalist.add(innerDatalist);
            }
        }

        final Object[][] array2d = new Object[outerDatalist.size()][columnSize];
        for (int k = 0; k < outerDatalist.size(); k++) {
            final ArrayList<String> newArray = outerDatalist.get(k);
            for (int l = 0; l < newArray.size(); l++) {
                array2d[k][l] = newArray.get(l);
            }
        }
        return array2d;
    }

    /**
     * This method is meant to update the test result
    */
    public void updateResult(final String testCaseName, final String testStatus) {
        try {
            final int totalRow = sheet.getLastRowNum();
            for (int i = 1; i <= totalRow; i++) {
                row = sheet.getRow(i);
                final String cel = row.getCell(0).getStringCellValue();
                if (cel.contains(testCaseName)) {
                    row.createCell(1).setCellValue(testStatus);
                    file.close();
                    final FileOutputStream writeOutToFile = new FileOutputStream(new File(pathToWorkbookLocation));
                    workbook.write(writeOutToFile);
                    writeOutToFile.close();
                    break;
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Exception when reading .xlxs spreadsheet test data file: " + e.getMessage());
        }
    }
}
