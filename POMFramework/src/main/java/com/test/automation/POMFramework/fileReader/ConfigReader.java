package com.test.automation.POMFramework.fileReader;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;



public class ConfigReader {

    public static final Logger log = Logger.getLogger(ConfigReader.class.getName());
	Properties prop;
	FileInputStream fis = null;
    String configFilePath;
	
    // Class constructor
	public ConfigReader() {
		try {
        log.info ("Loading configuration properties file ...");
		fis = new FileInputStream(configFilePath);
		prop = new Properties();
		prop.load(fis);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
        log.info ("Loading configuration properties file completed!");
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
