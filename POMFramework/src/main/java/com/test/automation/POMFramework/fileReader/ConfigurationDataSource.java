package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;


public class ConfigurationDataSource {

	private static Logger log = Logger.getLogger(ConfigurationDataSource.class);
	public static Properties OR;
	
	static {
		log.info("loading config.properties file...");
		String configFilePath = "/src/main/java/com/test/automation/POMFramework/configResources/config.properties";
		OR = new Properties();
		
		File f1 = new File(ResourceHelper.getResourcePath(configFilePath));
		try {
			FileInputStream file = new FileInputStream(f1);
			OR.load(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.info("loading of config.properties file done");
	}
}
