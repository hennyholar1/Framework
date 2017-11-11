package com.test.automation.POMFramework.fileReader;

import java.util.HashMap;

public class ConfigGenerator {

	public static final HashMap<String, String> configDataHolder = new HashMap<String, String>();
	static ConfigReader propertiesHandler = new ConfigReader();
	
	private static ConfigGenerator single_instance = null;
	
	private ConfigGenerator() {
		configDataRetriever();
	}
	
	public static ConfigGenerator getInstance() {
		if (single_instance == null)
			single_instance = new ConfigGenerator();
		return single_instance;
	}
	
	private static void configDataRetriever() {
		configDataHolder.putAll(propertiesHandler.retrivePropertiesDataStored());
	}
}
