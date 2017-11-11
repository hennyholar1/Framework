/**
 * 
 */
package com.test.automation.POMFramework.utilities;

import org.openqa.selenium.WebElement;
import com.test.automation.POMFramework.testBase.TestBase;

/**
 * @author Da Novenos	==> Hard Assertion examples
 *
 */
public class AssertionTestScript extends TestBase{	

	public static synchronized boolean verifyElementPresent( WebElement element) {
		
		boolean isDispalyed = false;
		
		try 
		{
			isDispalyed= element.isDisplayed();
		}
		catch(Exception ex) {
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
	
	
	public static synchronized boolean verifyTextEquals( WebElement element,String expectedText) {
		boolean flag = false;
		try 
		{
			String actualText=element.getText();
			
			if(actualText.equals(expectedText)) {
				
				return flag=true;
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
