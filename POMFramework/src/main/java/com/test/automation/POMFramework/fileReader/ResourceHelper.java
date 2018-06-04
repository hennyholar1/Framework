package com.test.automation.POMFramework.fileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceHelper {

	/**
	 * This method will return resource path / project location irrespective of
	 * driver location
	 * 
	 * @param resource
	 * @param path
	 * @return
	 */

	public static String getResourcePath(String resource) {
		String getRelativePath = System.getProperty("user.dir");

		return getRelativePath + resource;
	}

	
	public static File getResourcePath(File resource) {

		return resource;
	}

	
	public static InputStream getResourcePathInputStream(String path) throws FileNotFoundException {

		return new FileInputStream(ResourceHelper.getResourcePath(path));
	}

	
	public static InputStream getResourcePathInputStream(File path) throws FileNotFoundException {

		return new FileInputStream(ResourceHelper.getResourcePath(path));
	}
}
