package com.test.automation.POMFramework.utilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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
	

	// ... Running a bat file using Selenium and double click function .... //
	// FindBy (By.xpath("element locator (.bat file location path)"))
	// WebElement element;
    
    
    
    // ... { Mouse effect and select class } ...
    // Locate the element using POM
    
    public void mouseOver(WebElement element) {
        Actions action = new Actions(driver);
        // action.moveToElement(element).build().perform();
        action.moveToElement(element).perform();
    }
    
    // Locate the element without POM
    public void mouseOver(String element) {
        Actions action = new Actions(driver);
        log("doing mouse over on :" + element);
        // action.moveToElement(getElement(element)).build().perform();
        action.moveToElement(getElement(element)).perform();
    }
    
    
    
    // Double click using POM
	public void doubleClick(WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).doubleClick().build().perform();
	}

	// Double click Without POM
	public void doubleClick(String element) {
		Actions action = new Actions(driver);
		action.moveToElement(getElement(element)).doubleClick().build().perform();
	}

	// Double click using POM
	public void rightClick(WebElement element) {
		Actions action = new Actions(driver);
		action.contextClick(element).perform();
	}

	// Double click using POM
	public void rightClick(String element) {
		Actions action = new Actions(driver);
		action.contextClick(getElement(element)).perform();
	}
}
