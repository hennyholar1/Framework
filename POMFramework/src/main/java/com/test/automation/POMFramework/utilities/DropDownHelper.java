package com.test.automation.POMFramework.utilities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import com.test.automation.POMFramework.testBase.TestBase;

public class DropDownHelper extends TestBase{
	
//	private static WebDriver driver;
	public static final Logger log = Logger.getLogger(DropDownHelper.class.getName());

	
	public DropDownHelper(WebDriver driver) {
		TestBase.driver = driver;
		log("DropDownHelper : " + TestBase.driver.hashCode());	}

	
	// ... Select Class .../
	public static Select selectClassAction(WebElement element) {
		return new Select(element);
	}

	// Select class with POM
	public static void selectByIndex(WebElement element, int index) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).selectByIndex(index);
		/*
		 * List<WebElement> select_list = select.getOptions(); for (WebElement
		 * total_element : select_list) // Iterating through the drop-down list/elements
		 * { String selectedItemNames = total_element.getText(); }
		 */
	}

	public static void selectByValue(WebElement element, String value) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).selectByValue(value);
	}

	public static void selectByVisibleText(WebElement element, String text) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).selectByVisibleText(text);
	}

	// Deselect class with POM
	public static void deselectAll(WebElement element) {
		log.info("Deselecting item from " + element + " drop down list");
		selectClassAction(element).deselectAll();
	}

	public static void deselectByIndex(WebElement element, int index) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).deselectByIndex(index);
	}

	public static void deselectByValue(WebElement element, String value) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).deselectByValue(value);
	}

	public static void deselectByVisibleText(WebElement element, String text) {
		log.info("Selecting item from " + element + " drop down list");
		selectClassAction(element).deselectByVisibleText(text);
	}
	
	public static List<String> getAllDropDownValues(WebElement locator) {
		Select select = new Select(locator);	
		List<WebElement> elementList = select.getOptions();	
		List<String> valueList = new LinkedList<String>();	
		for (WebElement element : elementList) {	
			valueList.add(element.getText());		}	
		return valueList;	}
	

	// Select class for drop-down values using xPath
	public static void selectDropdownData(String dropDownElement, String value) {
		log("Selecting value from " + dropDownElement + " drop down list");
		List<WebElement> elements = driver.findElements(By.xpath("//*[@id='"+dropDownElement+"']/option"));
		Iterator<WebElement> itr = elements.iterator();
		while (itr.hasNext()) {
			WebElement clickElement = itr.next();
			String text = clickElement.getText().trim().toString();
			if (text.equals(value)) {
			
				clickElement.click();
		log("Value is selected from " + dropDownElement + " drop down list");
				break;
			}	
		}	
	}
	
	// Select class for drop-down values using CSS
	public static void selectDropdownValue(String dropDownElement, String value) {
		log("Selecting value from " + dropDownElement + " drop down list");
		List<WebElement> elements = driver.findElements(By.cssSelector("*[id~='"+dropDownElement+"']>option"));	// ~ is used as "contains" in CSS
		Iterator<WebElement> itr = elements.iterator();
		while (itr.hasNext()) {
			WebElement clickElement = itr.next();
			String text = clickElement.getText().trim().toString();
			if (text.equals(value)) {
			
				clickElement.click();
		log("Value is selected from " + dropDownElement + " drop down list");
				break;
			}	
		}	
	}

}
