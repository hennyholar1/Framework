package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import utilities.ResourceHelper;


public class ConfigurationDataSource {

	private static Logger log = Logger.getLogger(ConfigurationDataSource.class);
	public static Properties OR;
	final static String configPropFilePath = "/src/main/java/com/test/automation/POMFramework/configResources/config.properties";
	static {
		log.info("loading config.properties file...");
		OR = new Properties();
		
		File f1 = new File(ResourceHelper.getResourcePath(configPropFilePath));
		
		try {
		// For loading property file using getResourcePath(File resource) method
			OR.load(ResourceHelper.getResourcePathInputStream(f1));
	
		/**	
		 * // For loading property file using getResourcePath(String resource) method
		 * FileInputStream file = new FileInputStream(f1);
		 *	OR.load(file); 
		 */

		} catch (Exception e) {

			e.printStackTrace();

		}

		log.info("loading of config.properties done");

	}
}
