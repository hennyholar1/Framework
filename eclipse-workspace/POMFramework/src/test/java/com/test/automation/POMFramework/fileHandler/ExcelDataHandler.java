package com.test.automation.POMFramework.fileHandler;


import org.testng.annotations.Test;

import com.test.automation.POMFramework.fileReader.ExcelFileHandler;

public class ExcelDataHandler {

	@Test
	public void excelHandlerTest() {
	ExcelFileHandler excelHandler = new ExcelFileHandler(System.getProperty("user.dir")+"/src/main/java/com/test/automation/POMFramework/dataFile/LogInTestData.xlsx");
	System.out.println("-----------------------------");
	Object[][] data = excelHandler.retrieveDataStored("LogInData");
	for (int row = 0; row < data.length; row++) {
		for (int col=0; col < data[row].length; col++) {
		System.out.println(data[row][col] + " ");
	}
		System.out.println();
	}
	System.out.println("-----------------------------");
	}	
}
