package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class ConfigReader {

	Properties prop;
	
	public ConfigReader() {
		try {
		File src = new File (System.getProperty("user.dir") + "/src/main/java/com/test/automation/POMFramework/configResources/config.properties");
		FileInputStream fis = new FileInputStream(src);
		prop = new Properties();
		prop.load(fis);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//	To retrieve data from the properties file one-by-one
	public String getKeyData(String key) {
		return this.prop.getProperty(key);
		}	
	
	private Set<Object> getAllKeys(){
		Set<Object> keys = this.prop.keySet();
		return keys;
	}	
	
	// To read/fetch full property file data
	public HashMap<String, String> retrivePropertiesDataStored() {
		HashMap<String, String> dataHolder = new HashMap<String, String>();
		Set<Object> keys = getAllKeys();
		for (Object k : keys) {
			String key = (String) k ;
			dataHolder.put(key, getKeyData(key));
		}
		return dataHolder;
	}
}