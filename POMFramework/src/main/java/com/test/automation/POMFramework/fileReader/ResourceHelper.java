package com.test.automation.POMFramework.fileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceHelper {
	
	public static String getResourcePath(String resource){
	
		return System.getProperty("user.dir") +resource;
	}
	
	public static InputStream getResoucePathInputStream(String path) throws FileNotFoundException {
	
		return new FileInputStream(ResourceHelper.getResourcePath(path));
