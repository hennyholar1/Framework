package com.test.automation.POMFramework.utilities;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.WebDriver;

public class CommonHelper {

	public static WebDriver driver;

	
	public static synchronized boolean verifyElementPresent( WebElement element) {

		boolean isDispalyed = false;
		try
			{
				isDispalyed = element.isDisplayed();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return isDispalyed;
	}

	
	public static synchronized boolean verifyElementNotPresent( WebElement element) {

		boolean isDispalyed = false;
		try 
			{
			 	element.isDisplayed();
			}
		catch(Exception ex) {
		
			isDispalyed = true;
			}

		return isDispalyed;
	}

	
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
		return element.getText();	
	}


	public static String getElementAttributeValue(WebElement element, String htmlAttributeValue) {

		if (null == element)
			return null;
		if (!isDisplayed(element))
			return null;
		return element.getAttribute(htmlAttributeValue);	
	}
	
	
	public static boolean isDisplayed(WebElement element) {

		try {
			element.isDisplayed();
			return true;
		} catch (Exception e) {

			return false;
		}
	}


	public static boolean isNotDisplayed(WebElement element) {

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
	

	public static boolean isDisabled(WebElement element) {

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


	public static boolean isNotSelected(WebElement element) {	

		try {
			element.isSelected();
			return false;
		} catch (Exception e) {
			return true;
		}
	}


	public static synchronized String getDisplayText(WebElement element) {

		if (null == element) {
			return null;
		}
		String elementText = null;
		try {
			if (!isDisplayed(element))
				return null;
			elementText = element.getText();
		} catch (Exception ex) {
			return null;
			}
		return elementText;
	}

		
	public static void clickOnHiddenElementsHandlingById(String arg1) {

			List<WebElement> elements = driver.findElements(By.id(arg1));
		 for (WebElement element : elements) { 
			 int x = element.getLocation().getX();		 
			 if(x!=0) {			 
				 element.click();	 
				 break;
			 }	 
		}		 
	}


	public static void clickOnHiddenElementsHandling(String arg1) {

			List<WebElement> elements = driver
					.findElements(By.xpath("//*[contains(text(),'" + arg1 + "')]"));	
		 for (WebElement element : elements) {	 
			 int x = element.getLocation().getX(); 
			 if(x!=0) {	 
				 element.click();	 
				 break;	 
				 }	
			 }	
		 }
	
	
	public static void hiddenElementsDisplayHandling(String arg1) {

		List<WebElement> elements = driver
				.findElements(By.xpath("//*[contains(text(),'" + arg1 + "')]"));	
	 for (WebElement element : elements) {	 
		 int x = element.getLocation().getX(); 
		 if(x!=0) {	 
			 element.isDisplayed();
			 break;	 
			 }	 
		 }	
	 }

	public static void hasValue(WebElement elementToClick, String elementToVerify) {
		WebElement element = WaitHelper.waitForElement(elementToClick);
		element.click();
		hiddenElementsDisplayHandling(elementToVerify);
	}
	
	public static void hardAssertion(WebElement element, String uIText){
			
			Assert.assertEquals(getDisplayText(element), uIText);
	}
	
	
	public static void softAssertion(WebElement element, String uIText){
		
	SoftAssert softAssert = new SoftAssert();
	softAssert.assertEquals(getDisplayText(element), uIText);
	softAssert.assertAll();
	}
	
	
	public static synchronized boolean compareTextValues(WebElement element, String expectedText) {

		boolean flag = false;
		try 
		{
			String actualText = element.getText();
			if(actualText.equals(expectedText))		{
				return !flag;
			}
			else 
			{
				return flag;
			}	
		}
		catch(Exception ex) {		
			return flag;
		}
	}
	
}
