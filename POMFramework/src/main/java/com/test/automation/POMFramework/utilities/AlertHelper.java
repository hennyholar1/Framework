package com.test.automation.POMFramework.utilities;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class AlertHelper {
	
	private WebDriver driver;
	private Logger Log = Logger.getLogger(AlertHelper.class.getName());
	
	public AlertHelper(WebDriver driver) {
		
		this.driver = driver;
	}
	
	
	public Alert getAlert() {
		
		return driver.switchTo().alert();
	}
	
	
	public void acceptAlert() {
		
		getAlert().accept();
	}
	
	
	public void dismissAlert() {
		
		getAlert().dismiss();
	}

	
	public String getAlertText() {
		
		String text = getAlert().getText();
		Log.info(text);
		return text;
	}

	
	public boolean isAlertPresent() {
		
		try {
		
			driver.switchTo().alert();
			
			return true;
		
		} catch (NoAlertPresentException e) {
			
			return false;
		}
	}

	
	public void AcceptAlertIfPresent() {
		
		if (!isAlertPresent())
		
			return;
		
		acceptAlert();		
	}

	
	public void DismissAlertIfPresent() {

		if (!isAlertPresent())
			
			return;
		
		dismissAlert();
	}
	
	
	public void acceptPrompt(String text) {
		
		if (!isAlertPresent())
			
			return;
		
		Alert alert = getAlert();
		
		alert.sendKeys(text);
		
		alert.accept();
	}
}
