package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;



public class ConfigReader {

    public static final Logger log = Logger.getLogger(ConfigReader.class.getName());
	Properties prop;
    
    String configFilePath = "/src/main/java/com/test/automation/POMFramework/configResources/config.properties";
	
    // Class constructor
	public ConfigReader() {
		try {
            // making use of testbase getResource method
        log.info ("Loading configuration properties file ...");
		File src = new File (com.test.automation.POMFramework.testBase.TestBase.getResourcePath(configFilePath));
		FileInputStream fis = new FileInputStream(src);
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