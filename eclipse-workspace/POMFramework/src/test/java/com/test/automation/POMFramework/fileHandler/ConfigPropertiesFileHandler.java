package com.test.automation.POMFramework.fileHandler;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.test.automation.POMFramework.fileReader.ConfigReader;

public class ConfigPropertiesFileHandler {
@Test
	public void testPropertiesDataStored () {
		ConfigReader propertiesDataHandler = new ConfigReader(); 
		HashMap<String, String> data = propertiesDataHandler.retrivePropertiesDataStored();
		
		System.out.println ("-------------------------------");
		for(Map.Entry<String, String> entry : data.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("------------------------------------");
		
		}
	}
