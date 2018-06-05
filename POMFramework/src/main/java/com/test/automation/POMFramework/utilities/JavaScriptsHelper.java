package com.test.automation.POMFramework.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class JavaScriptsHelper {

	static WebDriver dr;
	
	public JavaScriptsHelper(WebDriver driver) {
		super();
		dr = driver;
	}

	
	public static Object executeScript(String script) {			

		return ((JavascriptExecutor) dr).executeScript(script);	
	}

	public static Object executeScript(String script, Object... args) {

		return ((JavascriptExecutor) dr).executeScript(script, args);	
	}
	
	
	public static void highlightMe(WebDriver driver, WebElement element)  {		

		executeScript("arguments[0].style.border='4px solid yellow'", element);	
		try {
			Thread.sleep(3000);			
		} catch (InterruptedException e) {
						e.printStackTrace();
		}		
		executeScript("arguments[0].style.border=''", element);	
	}

	public static void highlightElement(WebDriver driver, WebElement element)  {		

		executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);	
		try {			
			Thread.sleep(3000);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		executeScript("arguments[0].style.border=''", element);	
	}

	public void scrollToElement(WebElement element) {	

		executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x, element.getLocation().y);
	}

	public void scrollToElemetAndClick(WebElement element) {		

		scrollToElement(element);		
		element.click();	
	}

	public void scrollIntoView(WebElement element) {	

		executeScript("arguments[0].scrollIntoView()", element);	
	}
	
	public void scrollIntoViewAndClick(WebElement element) {

		scrollIntoView(element);	
		element.click();	
	}
	
	public void scrollDown() {	

		executeScript("window.scrollTo(0, document.body.scrollHeight)");	
	}

	public void scrollUp() {	

		executeScript("window.scrollTo(0, -document.body.scrollHeight)");	
	}
	
	public void scrollDownByPixel(int enterZeroValueHere, int enterPositiveValueHere) {	

		executeScript("window.scrollBy("+ enterZeroValueHere +","+ enterPositiveValueHere + ")");	
	}

	public void scrollUpByPixel(int enterZeroValueHere, int enterNegativeValueHere) {	

		executeScript("window.scrollBy("+ enterZeroValueHere +","+ enterNegativeValueHere + ")");	
	}
	
	public void ZoomInByPercentage(int percentageVale) {

		executeScript("document.body.style.zoom='" + percentageVale + " %'");
	}

	public String getDisplayedValue(WebElement value) {
       
        return (String)executeScript("return $('value').val();");
    }
	
}
