package com.test.automation.POMFramework.fileReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;

public class CsvDataReader {
 static ArrayList<String> dataStoreCsv = new ArrayList<>();
 String scvFilePath = null;
 CSVReader reader = null;
 int size = 0;
 
 public CsvDataReader (String pathToFile) {
	 this.scvFilePath = pathToFile;
	 try {
		 this.reader = new CSVReader(new FileReader(this.scvFilePath));
	 }
	 catch (FileNotFoundException e) {
		 System.out.println(e.getMessage());	 
	 }
 }
 
 public ArrayList<String> retrieveScvDataStored(){
	 String[] line;
	 try{
		 while((line = reader.readNext()) != null) {
			 pusDataToList(line); }
		 }
		 catch	 (IOException e) 
			 {
				 e.printStackTrace();
			 }
	 return dataStoreCsv;
	 	}
 
 private void pusDataToList(String[] line) {
	 int size = line.length;
	 for (int i = 0; i < size; i++) {
		 dataStoreCsv.add(line[i]);
	 }
 }
 	}	 

