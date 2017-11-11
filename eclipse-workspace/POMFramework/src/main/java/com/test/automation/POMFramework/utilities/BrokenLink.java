package com.test.automation.POMFramework.utilities;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.testBase.TestBase;

public class BrokenLink extends TestBase{

//	ChromeOptions option = new ChromeOptions();
	
//	public static WebDriver driver;
	
	@BeforeTest
	public void launchApplication() {
		super.setUp();

	
	}
	
	@Test
	private void findingBrokenLinks() {
		
		 List<WebElement> links = driver.findElements(By.tagName("a"));
		
		System.out.println("The total links on this web page are: " + links.size());
		
		for (WebElement ele : links) {
			
			String url = ele.getAttribute("href");
			
			verifyActiveLink(url);
			
		}
	}
	
	public static void verifyActiveLink(String url_Link) {
			
		try {
			URL url = new URL (url_Link);
			
			HttpURLConnection http_connect_url = (HttpURLConnection)url.openConnection();
			
			http_connect_url.setConnectTimeout(3000);
			
			http_connect_url.connect();
			
			if(http_connect_url.getResponseCode()==200) {
				
				 System.out.println(url_Link+" - "+http_connect_url.getResponseMessage());
				 
            }
          if(http_connect_url.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND)  
        	  
           {
              
        	  System.out.println(url_Link+" - "+http_connect_url.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
        	  
            }
          
        } 
		
		catch (Exception e) {
           
        }				
	}
}
