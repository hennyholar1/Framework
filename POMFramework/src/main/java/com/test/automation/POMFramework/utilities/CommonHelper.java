package com.test.automation.POMFramework.utilities;
https://github.com/LearnByBhanuPratap/keywordDrivenFramework/blob/master/src/test/java/com/companyname/projectname/Test/Keywords.java

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import testBase.TestBase;

import org.openqa.selenium.WebDriver;

public class CommonHelper extends TestBase{


	public static SoftAssert softAssertion= new SoftAssert();
	public static Wait<WebDriver> wait;
	
	public CommonHelper(WebDriver driver) {
		super();
		TestBase.driver = driver;
	}
	
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
		
		softAssertion.assertEquals(getDisplayText(element), uIText);
	//	softAssert.assertAll();
	}
	
	
	public static synchronized boolean verifyTextValue(WebElement element, String expectedText) {

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
	
	
	public static synchronized boolean verifyTextValue(String element, String expectedText) {

		boolean flag = false;
		try 
		{
			String actualText = getWebElement(element).getText();
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
	
	public static Set<String> getWindowHandles() {

		return driver.getWindowHandles();
		}

		public static void SwitchToWindow(int index) {

			LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
			if (index < 0 || index > windowsId.size())
				throw new IllegalArgumentException("Invalid Index : " + index);
			driver.switchTo().window(windowsId.get(index));
		}

		public static void switchToParentWindow() {

			LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
			// driver.switchTo().defaultContent(); // same as the above code
			driver.switchTo().window(windowsId.get(0));
		}

		public static void switchToParentAndCloseChildWindow() {

			LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
			for (int i = 1; i < windowsId.size(); i++) {
				driver.switchTo().window(windowsId.get(i));
				driver.close();
			}
			switchToParentWindow();
		}

		public static void switchToFrame(By locator) {

			driver.switchTo().frame(driver.findElement(locator));
		}

		public static void switchToFrame(String nameOrId) {

			driver.switchTo().frame(nameOrId);
		}

		public void switchToFrame(int index) {

			driver.switchTo().frame(index);
		}

		public static void switchToTab(int indesOfTab) {

			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(indesOfTab));
		}

		public static void switchToDefaultContent() {

			driver.switchTo().defaultContent();
		}

		public static void getApplicationTitle() {

			driver.getTitle();
		}
		
		public static WebElement getWebElement(WebElement locator) {
			 
			 wait = new WebDriverWait(driver, 10);
			return wait.until(ExpectedConditions.visibilityOfElementLocated((By) locator));
		//	return WaitHelper.waitForElement(locator);
		//	return locator;
		}
		
		// Generic xPath locator NB: We can also us CSS
		public static WebElement getWebElement(String locator) {

		return driver.findElement(By.xpath("//*[contains(text(),'" + locator + "')]"));
		//	return (WebElement) wait.until(ExpectedConditions.visibilityOfElementLocated((By) locator));
		//	return WaitHelper.waitForElement(locator);
		}
		
		public static List<WebElement> getWebElements(String locator) {
			
			return (List<WebElement>) WaitHelper.waitForElement(locator);
		//	return (List<WebElement>) wait.until(ExpectedConditions.visibilityOf((By) locator));
		}
			
		
		public static List<WebElement> getWebElements(WebElement locator) {
			
			return (List<WebElement>) WaitHelper.waitForElement(locator);
		//	return (List<WebElement>) wait.until(ExpectedConditions.visibilityOfElementLocated((By) locator));
		}
		
		// Generic CSS locator for Link
		public static WebElement getWebElementByLink(String locator) {
			
			return WaitHelper.waitForElement(locator);
		}
		
		// Generic CSS locator for image
		public static WebElement getWebElementByImage(String locator){
			
			return WaitHelper.waitForElement(locator);
		//	return (WebElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[src*='"+ element +"'']")));
		}
			
		// Generic CSS element locator method
		public static WebElement getWebElementByName(String locator) {

			return WaitHelper.waitForElement(locator);
		//	return (WebElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name*='" + element + "']")));
		}

		// Generic CSS element locator method
		public static WebElement getWebElementByClass(String locator) {

			return WaitHelper.waitForElement(locator);
		//	return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[class*='" + element + "']")));
		}

		// Generic CSS element locator method
		public static WebElement getWebElementById(String locator) {

			return WaitHelper.waitForElement(locator);
		//	return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[id*='" + element + "']")));
		}

		// Generic expandable link/menu method for text displayed element
		public static void clickOnExpandableMenu(String locator) {
			
			 WaitHelper.waitForElement(
					driver.findElement(By.xpath("//*[contains(text(),'" + locator + "') and @aria-expanded='false')]"))).click();
			
		}

		// Generic expanded link/menu method for text displayed element
		public static void clickOnExpandedMenu(String expandedMenu, String locator) {

			 WaitHelper.waitForElement(driver.findElement(By.xpath("//*[contains(text(),'" + expandedMenu + "')"
					+ " and @aria-expanded='true']/following-sibling::ul/child::li/child::a[contains(text),'" 
					+ locator + "')]"))).click();
		}

		public static void clickOnWebElement(WebElement element) {
			
			if (element!=null)
			try
				{
		//	element.click();
				wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
				}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		// For generic Element (non POM)
		public static void clickOnWebElement(String element) {

			getWebElement(element).click();
		}

		public static void setValue(WebElement element, String value) {
			if (element!=null)
				try
			{
				//	wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(value);
					element.sendKeys(value);
					element.sendKeys(Keys.TAB);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		// Generic text/input-box (non POM)
		public static void setValue(String textboxName, String value) {

			getWebElement(textboxName).sendKeys(value);
			getWebElement(textboxName).sendKeys(Keys.TAB);
		}

		public static void clearTextArea(WebElement textbox) {
			if (textbox!=null)
				try
			{
			//	wait.until(ExpectedConditions.visibilityOf(textbox)).clear();
				textbox.clear();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			}
		}

		// Generic text area (non POM)
		public static void clearTextArea(String textbox) {

			getWebElement(textbox).clear();
		}

		
		// Review this bootstrap method later
		public static void bootStrapWithSimilarLocatorsWithoutFrame(WebElement bootStrapElement,
				String bootStrapSubElements, String itemToClick) {

			getWebElement(bootStrapElement);
			List<WebElement> elementList = getWebElements(bootStrapSubElements);

			for (WebElement ele : elementList) {
				String dd_value = ele.getAttribute("innerHTML");

				if (dd_value.contentEquals(itemToClick)) {
					ele.click();
					break;
				}
			}
		}

		public static void bootStrapWithSimilarLocatorsWithinSameFrame(WebElement bootStrapElement,
				String bootStrapSubElements, String itemToClick, String frameName) {

			clickOnWebElement(bootStrapElement);
			switchToFrame(frameName);
			List<WebElement> elementList = getWebElements(bootStrapSubElements);

			for (WebElement ele : elementList) {
				String dd_value = ele.getAttribute("innerHTML");

				if (dd_value.contentEquals(itemToClick)) {
					ele.click();
					break;
				}
				switchToDefaultContent();
			}
		}

		public static WebElement bootStrapItemWithDistinctLocator(WebElement bootStrapElement, String element) {

			clickOnWebElement(bootStrapElement);
			return getWebElement(element);
		}

}
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
