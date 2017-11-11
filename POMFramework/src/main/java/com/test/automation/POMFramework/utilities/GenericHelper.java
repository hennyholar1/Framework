package com.test.automation.POMFramework.utilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import com.test.automation.POMFramework.testBase.TestBase;

/**
 * @author Da Novenos
 *
 */
public class GenericHelper extends TestBase{
	

	public static String getValueFromElement(WebElement element) {

		if (null == element){
			
			return null;
		}

		boolean displayed = false;
		try {
			
			displayed = isDisplayed(element);
			
		} catch (Exception e) {
			
			return null;
		}

		if (!displayed)
			
			return null;
		
		String text = element.getText();

		return text;
	}
	

	public static String getElementAttributeValue(WebElement element) {
		
		if (null == element)
			
			return null;
		
		if (!isDisplayed(element))
			
			return null;
		String value = element.getAttribute("value");
		
		return value;	}
	
	
	public static boolean isDisplayed(WebElement element) {
		
		try {
			element.isDisplayed();
			
			return true;
		
		} catch (Exception e) {
			
			return false;
		}
	}
	
	
	protected static boolean isNotDisplayed(WebElement element) {
		
		try {
			element.isDisplayed();
			
			return false;
		
		} catch (Exception e) {
			
			return true;
		}
	}
	
	
	public static boolean isEnabled(WebElement element) {
		
		try {
			element.isEnabled();
			
			return true;
		
		} catch (Exception e) {
			
			return false;
		}
	}
	
	protected static boolean isDisabled(WebElement element) {
		
		try {
			element.isEnabled();
			
			return false;
		
		} catch (Exception e) {
			
			return true;
		}
	}
	
	
	public static boolean isSelected(WebElement element) {
		
		try {
			element.isSelected();
			
			return true;
		
		} catch (Exception e) {
			
			return false;
		}
	}
	

	protected static boolean isNotSelected(WebElement element) {
		
		try {
			element.isSelected();
			
			return false;
		
		} catch (Exception e) {
			
			return true;
		}
	}
	
	
	protected static String getDisplayText(WebElement element) {
		
		if (null == element)
			
			return null;
		
		if (!isDisplayed(element))
		
			return null;
		
		return element.getText();
	}
	

	public static synchronized String getElementText( WebElement element) {
		
		if (null == element) {
			
			return null;
		}
		
		String elementText = null;
		
		try {
		
			elementText = element.getText();
		
		} catch (Exception ex) {
			
		}
		return elementText;
	}
	
	
	public void hiddenElementHandlingWithId(String arg1) {
			List<WebElement> elements = driver.findElements(By.id(arg1));
		 for (WebElement element : elements) { 
			 int x = element.getLocation().getX();		 
			 if(x!=0) {			 
				 element.click();	 
				 break;
			 }	 }	}

	
	public void hiddenElementHandlingWithXpath(String arg1) {
			List<WebElement> elements = driver.findElements(By.xpath(arg1));	
		 for (WebElement element : elements) {	 
			 int x = element.getLocation().getX(); 
			 if(x!=0) {	 
				 element.click();	 
				 break;	 }	 }	}
	

}
