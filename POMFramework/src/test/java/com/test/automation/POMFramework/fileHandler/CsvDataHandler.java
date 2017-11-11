package com.test.automation.POMFramework.fileHandler;

import java.util.ArrayList;
import java.util.Iterator;

import com.test.automation.POMFramework.fileReader.CsvDataReader;

// To retrieve data from csv file
public class CsvDataHandler {
	
	public void testCsvDataReading() {
	CsvDataReader csvDataReader = new CsvDataReader(System.getProperty("user.dir") + "/path to the .csv file directory goes here/...csv");
	ArrayList<String> data = csvDataReader.retrieveScvDataStored();
	{
		System.out.println ("-------------------------------");
		
		Iterator <String> itereator = data.iterator();
	while(itereator.hasNext()) {
		System.out.println(itereator.next());
	}
		System.out.println("--------------------------------");
		}
	}
}
