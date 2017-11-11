package com.test.automation.POMFramework.testBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.google.common.base.Function;
import com.test.automation.POMFramework.fileReader.ConfigGenerator;
import com.test.automation.POMFramework.fileReader.ExcelReader;
import com.test.automation.POMFramework.utilities.SendEmails;
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;

/**
 * @author Da Novenos
 */
public class TestBase {
		
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest testInfo;
	public static ATUTestRecorder recorder;
	public static ITestResult result;		
	static Calendar calendar = Calendar.getInstance();
	public static String videoFolder = (System.getProperty("user.dir") +"/test-output/TestExecutionVideos");
	static SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
	static String timeFormat = formater.format(calendar.getTime());
	public static String TestVideoRecord = "TestVideo-" + timeFormat;
	public ExtentReports extent;
	public static final Logger log = Logger.getLogger(TestBase.class.getName());
	public WebDriver driver;
	ConfigGenerator config;
	ExcelReader excel;
	
	
	@BeforeMethod
	public void beforeMethod(Method result) {
			testInfo = extent.createTest(result.getName(), "is being executed").assignAuthor("Oludare Eniola");
			testInfo.log(Status.INFO, result.getName() + " test Started");	}
	
	
	@AfterMethod()
	public void captureStatus(ITestResult result) {
		if (ITestResult.SUCCESS == result.getStatus()) {
			testInfo.pass(result.getName() + " Passed");
			testInfo.log(Status.PASS, result.getName() + " test passed");}
		
		else if (ITestResult.STARTED == result.getStatus()) {
			testInfo.log(Status.INFO, result.getName() + " test started");	}
						
		 else if (ITestResult.SKIP == result.getStatus()) {
			testInfo.log(Status.SKIP, result.getName() + " test is skipped and the reason is:- " + result.getThrowable());	}
		
		 else if (ITestResult.FAILURE == result.getStatus()) {
			SendEmails.sendSimleEmail();
		try {	// adding screenshots to upon ItestResult failed status
			String outcome = takeScreenShot((result.getName()));	
	        testInfo.addScreenCaptureFromPath(outcome);	// ExtentTest with snapshot
			testInfo.fail((result.getName()+" Failed"), MediaEntityBuilder.createScreenCaptureFromPath(outcome).build());	// Appends snapshot in the log report
			testInfo.log(Status.ERROR, result.getName() + " test failed" + result.getThrowable());	} 
		catch (Exception e) {
			System.out.println(e.getMessage());	}	} 	}
	
	
	@BeforeTest()
	public void init()  {
		
			config = ConfigGenerator.getInstance();
			if (extent == null)
				extent = new ExtentReports();
		try {
			recorder = new ATUTestRecorder(videoFolder, TestVideoRecord, false);
			recorder.start();	} 
		catch (ATUTestRecorderException e) {
			e.printStackTrace();	}	
		
			htmlReporter = new ExtentHtmlReporter(new File(useFileData("htmlReport")+ "_" + timeFormat +".html"));	// try and see effect of appending time to this
			htmlReporter.loadXMLConfig(new File(useFileData("extent_config_xml")));
			htmlReporter.config().setTheme(Theme.DARK);	// Theme background - dark, standard
			htmlReporter.config().setReportName("Automation Test Report");
			htmlReporter.config().setDocumentTitle("Test Execution - ExtentReports");
		
			htmlReporter.setAppendExisting(true);		//allows appending test information to an existing report.
			
			htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);		// chart location - top, bottom
			htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a");	// set timeStamp format
		
		//	extent.setTestDisplayOrder(TestDisplayOrder.NEWEST_FIRST);		//Set test display order in the Pro version of Extent-Reporter
		//	htmlReporter.config().setCreateOfflineReport(true);		// The HtmlReporter allows creating offline report in the Pro version of Extent-Reporter
			
		//  Additional information that makes our report looks nice
			extent.setSystemInfo("Host Name", "Novenos IT Solutions Inc.");
			extent.setSystemInfo("Environment", "QA/Automation Testing");
			extent.setSystemInfo("Version", "V-1.0.0");
			extent.setSystemInfo("OS", "Windows-10");
			extent.attachReporter(htmlReporter);
			
			PropertyConfigurator.configure(useFileData("logFile"));	 
			}
	
	
	@AfterClass(alwaysRun = true)
	public void endTest() throws Exception {
			recorder.stop();
			testInfo.log(Status.INFO, "<a href='" + videoFolder + "/" + TestVideoRecord + ".mov" + "'> <span class='label info'> Download Video</span></a>");
			extent.flush();
		//	closeBrowser();
			Thread.sleep(4000);
			launchHtmlReport();	}
	
	
	// To insert properties files value
	public String useFileData(String configFileData) {
			return ConfigGenerator.configDataHolder.get(configFileData);		}
		
		
	public void selectBrowser(String browser) {
			System.out.println(System.getProperty("os.name"));
		
		if(System.getProperty("os.name").contains("Windows 10")){
			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty(useFileData("firefoxDriver"), useFileData("windowsFirefoxDriverPath"));	
				log("Lauching " + browser + " browser");
				driver = new FirefoxDriver();
				maximizeWindow();	}
			else if (browser.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");				
				options.addArguments("--disable-extensions");			
				options.addArguments("--start-maximized");		
			//	options.addArguments("headless");	// To launch headless browser		
				driver = new ChromeDriver(options);		}
				
		else if(System.getProperty("os.name").contains("Mac")){
			if(browser.equalsIgnoreCase("firefox")){
				System.setProperty(useFileData("firefoxDriver"), useFileData("macFirefoxDriverPath"));
				driver = new FirefoxDriver();
				driver.manage().window().maximize();	}
			else if(browser.equalsIgnoreCase("chrome")){
				driver = new ChromeDriver();
				driver.manage().window().maximize();	}	}	}	}
	
	
	protected void getUrl(String url) {
			log("Navigating to: "+ url);
			driver.get(url);
			waitImplicitly(5);	}

	
	public void waitForElement(WebElement element, int timeOutInSeconds) {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.ignoring(NoSuchElementException.class);
			wait.ignoring(ElementNotVisibleException.class);
			wait.ignoring(StaleElementReferenceException.class);
			wait.ignoring(ElementNotFoundException.class);
			wait.pollingEvery(200, TimeUnit.MILLISECONDS);
			wait.until(elementLocated(element));	}
	private Function<WebDriver, Boolean> elementLocated(final WebElement element) {
		return new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				return element.isDisplayed();	}	};	}
	
	public void waitForVisibilityOfElement(WebElement element, long timeout) {	// using locator 
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			wait.until(ExpectedConditions.visibilityOf(element));	}
	
	
	public void waitImplicitly(long timeout) {
			driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS );	}

	
	public void fluentWait(String element) {	
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20,TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
			wait.until(new Function<WebDriver, WebElement>() {
			
			public WebElement apply(WebDriver driver) {
				log("Location element "+ element);	
				return driver.findElement(By.xpath("//*[contains(text(),'"+element+"')]"));	
			}	});		}
	
	
	public WebElement waitForElementToBeClickable(WebDriver driver,long time,WebElement element){
			WebDriverWait wait = new WebDriverWait(driver, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));	}
	
	
	public void pageLoad(long timeOut) {
			driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);	}
	
	
	public  String takeScreenShot(String result) {
		if (result == "") 
			result = "blank";	
			File destFile = null;
			File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			destFile = new File(useFileData("failedTestScreenShot") + "/failure_screenshots/" + result +"_" + timeFormat + ".png");
		try {	
			FileUtils.copyFile(source, destFile);
			System.out.println("Screenshot taken");
		//  This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");	} 
		catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());	}	
		return destFile.toString();}
	
	
	public String takeScreenShotOnSuccess(String result) {	
		if (result == "") 
			result = "blank";
		 	File destFile = null;		 
		 	File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + useFileData("screenShot");
			destFile = new File((String) reportDirectory + "/" + result + "_" + timeFormat + ".png");
			FileUtils.copyFile(scrFile, destFile);
			System.out.println("Screenshot taken");
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot"+ e.getMessage());
		}	return destFile.toString();}
	
	
	public String[][] getData(String excelWorkbookName, String sheetName) {	// Excel reader for data-driven
			excel = new ExcelReader(useFileData("excelPath"));
			String [][] data =excel.getDataFromSheet(sheetName, excelWorkbookName);
		return data;	}
	
	
	public void launchHtmlReport() {
			selectBrowser(useFileData("browser"));
			maximizeWindow();
			pageLoad(10);
			// Path top the Extent HTML Report which would be launched upon completion of test
			getUrl(useFileData("htmlReport")+ "_" +timeFormat+".html");	}
	
	public void setUp() {
		selectBrowser(useFileData("browser"));
		pageLoad(25);
		getUrl(useFileData("url"));		}
	
	
	public void getApplicationTitle() {
		driver.getTitle();	}
	
	
	public String getElementAttribute(String element, String htmlAttribute) {
		log("Getting attribute of "+ element);	
		return driver.findElement(By.xpath("//*[contains(text(),'"+element+"')]")).getAttribute(htmlAttribute);	}
	
	
	public void closeBrowser() {
			driver.close(); }
	
	
	public void closeAllBrowsers() {
			driver.quit();	}

	
	public void log (String result) {
			log.info(result);
			Reporter.log(result);	}
	
	
	public void maximizeWindow() {	
		 	driver.manage().window().maximize();	}


	public void navigateBack() {		
			driver.navigate().back();	}

	
	public void navigateForward() {
			driver.navigate().forward();	}

	
	public void refresh() {		
			driver.navigate().refresh();	}

	
	public Set<String> getWindowHandles() {	
		return driver.getWindowHandles();	}
	
	public void SwitchToWindow(int index) {
			LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		if (index < 0 || index > windowsId.size())	
			throw new IllegalArgumentException("Invalid Index : " + index);
			driver.switchTo().window(windowsId.get(index));	}

	
	public void switchToParentWindow() {	
			LinkedList<String> windowsId = new LinkedList<String>(	
				getWindowHandles());
		//	driver.switchTo().defaultContent();	// same as the above code
			driver.switchTo().window(windowsId.get(0));	}

	
	public void switchToParentAndCloseChildWindow() {	
			LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		for (int i = 1; i < windowsId.size(); i++) {		
			driver.switchTo().window(windowsId.get(i));	
			driver.close();		}
			switchToParentWindow();	}
	
	
	public void switchToFrame(By locator) {	
			driver.switchTo().frame(driver.findElement(locator));	}
	
	
	public void switchToFrame(String nameOrId) {	
			driver.switchTo().frame(nameOrId);	}	

	public void switchToFrame(int index) {
			driver.switchTo().frame(index);	}
	
	
	public void switchToTab(int indesOfTab) {
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(indesOfTab));	}
	
	
	public void switchToDefaultContent() {
			driver.switchTo().defaultContent();	}
	
	
	
	// Locate the element by POM Element or by generic method
	public void mouseOver(WebElement element) {
			Actions action = new Actions(driver);		
			action.moveToElement(element).perform(); }
		
	
	// Locate the element by POM Element or by generic method
	public void mouseOverBuild(WebElement element) {		
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();; 	}
	
	
	// Locate the element by generic method
	public void mouseOver(String data){
			log("doing mouse over on :"+data);
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(By.xpath("//*[contains(text(),'"+data+"')]"))).build().perform();	}	

	
	// Generic elements locator method
	public List<WebElement> getElements(String elements){
			log("Locating web element "+ elements);
		return (driver.findElements(By.xpath("//*[contains(text(),'" + elements + "')]")));	}	
		
		
	public boolean isElementPresentQuick(By locator) {	
		boolean flag = driver.findElements(locator).size() >= 1;	
		return flag;	}
	
	
	public WebElement locateElement(By locator) {		
		if (isElementPresentQuick(locator))
		return driver.findElement(locator);
		try {
			throw new NoSuchElementException("Element Not Found : " + locator);	
		} catch (RuntimeException re) {		
			throw re;	}	}	
	
	
	public void selectDropdowXpathId(String dropDownElement, String value) {
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
	
	
	public void selectDropdowCssId(String dropDownElement, String value) {
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
	
	
	public void selectByIndex(WebElement dropDownElement, int index) {
			log.info("Selecting item from " + dropDownElement + " drop down list");
			Select select = new Select(dropDownElement);
			select.selectByIndex(index);
			/*		List<WebElement> select_list = select.getOptions();
			for (WebElement total_element : select_list)	// Iterating through the drop-down list/elements	
				{String selectedItemNames = total_element.getText();}	*/
		}
	
	
	// Generic expandable link/menu method for text displayed element
	public void clickOnExpandableMenu(String item){
			driver.findElement(By.xpath("//*[contains(text(),'" + item + "') and @aria-expanded='false')]")).click();
			log("Clicking on expandable menu "+ item);	}	
	
	
	// Generic expanded link/menu method for text displayed element
	public void clickOnExpandedItem(String expandedMenu, String product) {
			driver.findElement(By.xpath("//*[contains(text(),'"+ expandedMenu +"') and @aria-expanded='true']/following-sibling::ul/child::li/child::a[contains(text),'"+product+"')]")).click();
			log("Clicking on expanded menu "+ product);		}
	
	
	
	// For POM Element
	public void clickOnItem(WebElement element){
			log("Clicking on "+ element);	
			element.click();	}	
	
	
	// For POM Element
	public void enterValueInTextbox(WebElement element, String value){
			log("Clicking on "+ element);
			element.sendKeys(value);	}	
		
	
	// Generic text/input-box  method
	public void locateTextboxAndEnterValue(String elementName, String value) {
			log("Entering data in :"+ elementName.toString());
			driver.findElement(By.cssSelector("input[id*='" + elementName + "']")).sendKeys(value);	}

	
	// Generic Link/Button/Radio/Check-box selection method for text displayed element
	public WebElement locateTextCaptionedElement(String text){
		log("Location element "+ text);	
		return driver.findElement(By.xpath("//*[contains(text(),'"+text+"')]"));	}	
	
	
	// Generic text area clear method 
	public WebElement locateAndClearTextArea(String textbox){
			log("Clicking on "+ textbox);	
			return driver.findElement(By.xpath("//*[contains(text(),'"+textbox+"')]"));	}	
		
	
	// Generic Css element locator method
	public WebElement locateElementByName(String element){
			log("Locating web element "+ element);
			return driver.findElement(By.cssSelector("[name*='" + element + "']"));	}	
	
	
	// Generic Css element locator method
	public WebElement locateElementByClass(String element){
			log("Locating web element "+ element);
			return driver.findElement(By.cssSelector("[class*='" + element + "']"));	}	
	
	
	// Generic Css element locator method
	public WebElement locateElementById(String element){
			log("Locating web element "+ element);
				return driver.findElement(By.cssSelector("[id*='" + element + "']"));	}	
	
			
	// Review later
	public void bootStrapWithSimilarLocatorsWithoutFrame(WebElement bootStrapElement, String bootStrapSubElements, String itemToClick) {
			clickOnItem(bootStrapElement);	
			List<WebElement> elementList = getElements(bootStrapSubElements);
		for (WebElement ele : elementList) {	
			String dd_value = ele.getAttribute("innerHTML");
			if (dd_value.contentEquals(itemToClick)){
				ele.click();	
			break; }	}	}
			
	
	public void bootStrapWithSimilarLocatorsWithinSameFrame(WebElement bootStrapElement, String bootStrapSubElements, String itemToClick, String frameName) {
			clickOnItem(bootStrapElement);	
			switchToFrame(frameName);
			List<WebElement> elementList = getElements(bootStrapSubElements);
		for (WebElement ele : elementList) {	
			String dd_value = ele.getAttribute("innerHTML");
			if (dd_value.contentEquals(itemToClick)){
				ele.click();	
			break;	}	
			switchToDefaultContent();	}	}
	
	
	public WebElement bootStrapItemWithDistinctLocator(WebElement bootStrapElement, String element) {
			clickOnItem(bootStrapElement);	
			return locateTextCaptionedElement(element);	}
	
}