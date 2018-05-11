package utilities;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;

import testBase.TestBase;

public class WaitHelper {

//	private static Logger log = LogHelper.getLogger(WaitHelper.class);
	private WebDriver driver;
	public Wait<WebDriver> wait;


	public WaitHelper(WebDriver driver) {

		super();
		this.driver = driver;
	}

	

	/**
	 * This method will wait for element
	 * @param element
	 * @param time
	 */
	
	public void waitForElement(WebElement element){
		
		TestBase.log("waiting for element..");
		wait.until(ExpectedConditions.elementToBeClickable(element));
		TestBase.log("element is present...");
	}
	
	
	public void waitForElement(WebElement element, long timeOutInSeconds) {

		TestBase.log("wait for element presence..");
		wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
		TestBase.log("element is present...");
	}
	
	public WebElement waitForElementToBeClickable(WebDriver driver, long time, WebElement element) {

		TestBase.log("waiting for element to be present..");
		WebDriverWait wait = new WebDriverWait(driver, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
		
	public void fluentWait(WebElement element) {

		TestBase.log("waiting for element to be present..");

		wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				TestBase.log("Locating element " + element);
				return driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"));
			}
		});
	}

		
	public void waitForElementWithPolling(WebElement element, int timeOutInSeconds) {

		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(ElementNotVisibleException.class);
		wait.ignoring(StaleElementReferenceException.class);
		wait.ignoring(ElementNotFoundException.class);
		wait.pollingEvery(200, TimeUnit.MILLISECONDS);
		wait.until(elementLocated(element));
	}
	private static Function<WebDriver, Boolean> elementLocated(final WebElement element) {

		return new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return element.isDisplayed();
			}
		};
	}


	public void waitImplicitly(long timeout) {

		TestBase.log("waiting for all elements to be present..");
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}


}
