/**
 * 
 */
package com.test.automation.POMFramework.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.test.automation.POMFramework.testBase.TestBase;

/**
 * @author Da Novenos
 *
 */
public class JavaScriptsHelper extends TestBase{
	
	public static void highlightMe(WebDriver driver, WebElement element)  {		
		JavascriptExecutor js = (JavascriptExecutor) driver;	
		js.executeScript("arguments[0].style.border='4px solid yellow'", element);	
		try {
			Thread.sleep(3000);			
		} catch (InterruptedException e) {
						e.printStackTrace();
		}		
		js.executeScript("arguments[0].style.border=''", element);	}
	
	
	public static void highlightElement(WebDriver driver, WebElement element)  {	
		JavascriptExecutor js = (JavascriptExecutor) driver;		
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);	
		try {
			Thread.sleep(3000);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		js.executeScript("arguments[0].style.border=''", element);	}
	
	
	public Object executeScript(String script) {	
		JavascriptExecutor exe = (JavascriptExecutor) driver;		
		return exe.executeScript(script);	}

	
	public Object executeScript(String script, Object... args) {
		JavascriptExecutor exe = (JavascriptExecutor) driver;
		return exe.executeScript(script, args);	}

	
	public void scrollToElemet(WebElement element) {	
		executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x, element.getLocation().y);	}

	
	public void scrollToElemetAndClick(WebElement element) {		
		scrollToElemet(element);		
		element.click();	}

	
	public void scrollIntoView(WebElement element) {	
		executeScript("arguments[0].scrollIntoView()", element);	}

	
	public void scrollIntoViewAndClick(WebElement element) {
		scrollIntoView(element);	
		element.click();	}

	
	public void scrollDownVertically() {	
		executeScript("window.scrollTo(0, document.body.scrollHeight)");	}

	
	public void scrollUpVertically() {	
		executeScript("window.scrollTo(0, -document.body.scrollHeight)");	}

	
	public void scrollDownByPixel() {	
		executeScript("window.scrollBy(0,1500)");	}

	
	public void scrollUpByPixel() {	
		executeScript("window.scrollBy(0,-1500)");	}

	
	public void ZoomInBypercentage() {
		executeScript("document.body.style.zoom='40%'");	}

	
	public void ZoomBy100percentage() {	
		executeScript("document.body.style.zoom='100%'");	}
	
}
