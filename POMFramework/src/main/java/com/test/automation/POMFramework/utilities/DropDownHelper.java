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
	
	private WebDriver driver;
	public static final Logger log = Logger.getLogger(DropDownHelper.class.getName());

	public DropDownHelper(WebDriver driver) {
		this.driver = driver;
		log("DropDownHelper : " + this.driver.hashCode());	}

	
	public void SelectUsingVisibleValue(WebElement element,String visibleValue) {
		
		Select select = new Select(element);	
		select.selectByVisibleText(visibleValue);	
		log.info("Locator : " + element + " Value : " + visibleValue);	}

	
	public String getSelectedValue(WebElement element) {	
		String value = new Select(element).getFirstSelectedOption().getText();
		log.info("WebELement : " + element + " Value : "+ value);
		return value;	}
	
	
	public void SelectUsingIndex(WebElement element,int index) {	
		Select select = new Select(element);
		select.selectByIndex(index);
		log.info("Locator : " + element + " Value : " + index);	}
	
	
	public void SelectUsingVisibleText(WebElement element,String text) {
		Select select = new Select(element);
		select.selectByVisibleText(text);
		log.info("Locator : " + element + " Value : " + text);	}
	
	
	public List<String> getAllDropDownValues(WebElement locator) {
		Select select = new Select(locator);	
		List<WebElement> elementList = select.getOptions();	
		List<String> valueList = new LinkedList<String>();	
		for (WebElement element : elementList) {	
			valueList.add(element.getText());		}	
		return valueList;	}
	

	public void selectDropdowData(String dropDownElement, String value) {
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
			}	}	}
	

	public void selectDropdowValue(String dropDownElement, String value) {
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
			}	}	}
	
}


