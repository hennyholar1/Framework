package com.test.automation.POMFramework.fileHandler;

import java.util.Map;

import org.testng.annotations.Test;

import com.test.automation.POMFramework.fileReader.ConfigGenerator;

public class TestConfigGenerator {

	@Test
	public void testConfigGenerator() {
		ConfigGenerator.getInstance();
		System.out.println("---------------------------");
		
		for(Map.Entry<String, String> entry : ConfigGenerator.configDataHolder.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("------------------------------------");
		
	}
}
