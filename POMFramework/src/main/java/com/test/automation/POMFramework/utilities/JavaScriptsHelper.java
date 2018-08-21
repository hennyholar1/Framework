package com.test.automation.POMFramework.utilities;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class JavaScriptFunctions {

	private static WebDriver myDriver;
	JavascriptExecutor executor;
	
	public JavaScriptFunctions(WebDriver driver) {
		myDriver = driver;
		executor = ((JavascriptExecutor) myDriver);
	}


	public String getDisplayedValue(WebElement value) {
        return (String) executor.executeScript("return $('" + value +" ').val();");
    }
	
	public void clickOnElement(WebElement element){
		executor.executeScript("arguments[0].click();", element);
	}
	
	public void highlightElement(WebElement uiElement, String color)  {		
		executor.executeScript(
				"arguments[0].setAttribute('style', 'background: lava; border: 3px solid " + color + ";');", uiElement);
		WaitFunctions.sleep(1);
		executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", uiElement, "");
	}
	
	public void highlightElement(WebElement element)  {	
		executor.executeScript(
				"arguments[0].setAttribute('style', 'background: lava; border: 3px solid red;');", element);
		WaitFunctions.sleep(1);
		executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");	
	}

	public void scrollToElement(WebElement element) {	
		executor.executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x, element.getLocation().y);
	}

	public void scrollToElemetAndClick(WebElement element) {		
		scrollToElement(element);		
		element.click();	
	}

	public void scrollIntoView(WebElement element) {	
		executor.executeScript("arguments[0].scrollIntoView()", element);	
	}
	
	public void scrollIntoViewAndClick(WebElement element) {
		scrollIntoView(element);	
		element.click();	
	}
	
	public void scrollDown() {	
		executor.executeScript("window.scrollTo(0, document.body.scrollHeight)");	
	}

	public void scrollUp() {	
		executor.executeScript("window.scrollTo(0, -document.body.scrollHeight)");	
	}
	
	public void scrollDownByPixel(int enterIntegerValueHere) {	
		executor.executeScript("window.scrollBy(0,"+ enterIntegerValueHere + ")");	
	}

	public void scrollUpByPixel(int enterZeroValueHere, int enterIntegerValueHere) {	
		executor.executeScript("window.scrollBy("+ enterZeroValueHere +",-"+ enterIntegerValueHere + ")");	
	}
	
	public void ZoomInByPercentage(int percentageVale) {
		executor.executeScript("document.body.style.zoom='" + percentageVale + " %'");
	}
	
	public void setValue(WebElement webElement, String inputVale) {
		executor.executeScript("arguments[0].setAttribute('value', '" + inputVale +"')", webElement);
	//	executor.executeScript("document.getElementById('elementID').setAttribute('value', ' " + inputVale + "')");
	}
	
	public void setValue(String uiInputFieldId, String inputVale) {
		executor.executeScript("document.getElementById('"+ uiInputFieldId +"').setAttribute('value', '" + inputVale + "')");
	}
	
	public void getPageInfo(String pageInfomation) {
		String value = ("." + pageInfomation + ";");
		executor.executeScript("return document'" + value + "'").toString();
	}
	
	
	/**	JavaScript locator uses document.getElementBy..('locatorName')  * in place of driver.findElement(By...("locatorName"))
	 * 		setAttribute('inputValue')  * in place of sendKeys("inputValue")
	 	executor.executeScript("document.getElementByLOCATORTYPE..('elementName').setAttribute('value','text@gmail.com')");
	 */
}
