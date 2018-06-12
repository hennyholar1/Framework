package com.test.automation.POMFramework.utilities;

package utilities;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.base.Function;

public class WaitHelper {

	private static WebDriver driver;
	public static Wait<WebDriver> wait;
//	public static WebDriverWait wait;

	/**
	 * This method will wait for element
	 * @param element
	 * @return 
	 */
	

	public static void sleep(int timeInSecond){
		
		try {
			Thread.sleep(timeInSecond * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static WebElement waitForElementFluently(WebElement element) {

		wait = new FluentWait<WebDriver>(driver)
			.withTimeout(Duration.ofSeconds(15))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(Exception.class);  
		 return element;
	}
	
	public static WebElement waitForElementFluently(String element) {

		  wait = new FluentWait<WebDriver>(driver)
			.withTimeout(Duration.ofSeconds(15))
            .pollingEvery(Duration.ofSeconds(1))
            .ignoring(Exception.class);
		  return driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"));
	}


	public static WebElement waitForElement(WebElement element) {
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(element));
		wait.until(elementLocated(element));
		wait.until(ExpectedConditions.presenceOfElementLocated((By) element));
		return element;	}
	
	
	public static WebElement waitForElement(String element) {
		
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf 
				(driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"))));
		wait.until(elementLocated((driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]")))));
		wait.until(ExpectedConditions.presenceOfElementLocated((By) 
				(driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]")))));
		 return driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"));
		}
	
	private static Function<WebDriver, Boolean> elementLocated(final WebElement element) {
		return new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return element.isDisplayed();
			}
		};
	}


	public static void waitImplicitly(long timeout) {

		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}

}
