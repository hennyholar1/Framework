package com.test.automation.POMFramework.utilities;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;

public class WaitHelper {

	private static WebDriver driver;
	private static WebDriverWait wait;


	public WaitHelper(WebDriver driver, long timeOutInSeconds) {

		super();
		this.driver = driver;
		this.wait = new WebDriverWait(driver, timeOutInSeconds);
	}
	
	public WaitHelper(WebDriver driver) {

		super();
		this.driver = driver;
		this.wait = new WebDriverWait(driver, 10);
	}

	/**
	 * This method will wait for element
	 * @param element
	 * @param time
	 * @return 
	 */
	

	
	public static void fluentWait(WebElement element) {

		wait = (WebDriverWait) new FluentWait<WebDriver>(driver).withTimeout(10, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"));
			}
		});
	}


	public static WebElement waitForElement(WebElement element) {
		
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(ElementNotVisibleException.class);
		wait.ignoring(StaleElementReferenceException.class);
		wait.ignoring(ElementNotFoundException.class);
		wait.pollingEvery(200, TimeUnit.MILLISECONDS);
		wait.until(ExpectedConditions.visibilityOf(element));
		wait.until(elementLocated(element));	
		return element;
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
