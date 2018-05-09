package com.test.automation.POMFramework.testBase;
// Selenium online help community  ==> http://selenium.10932.n7.nabble.com/Selenium-Users-f8051.html  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
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
import com.test.automation.POMFramework.utilities.AssertionTestScript;
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
	public static String videoFolder = (System.getProperty("user.dir") + "/test-output/TestExecutionVideos");
	static SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
	static String timeFormat = formater.format(calendar.getTime());
	public static String TestVideoRecord = "TestVideo-" + timeFormat;
	public ExtentReports extent;
	public static final Logger log = Logger.getLogger(TestBase.class.getName());
	public static WebDriver driver;
	ConfigGenerator config;
	ExcelReader excel;

	@BeforeMethod
	public void beforeMethod(Method result) {
		testInfo = extent.createTest(result.getName(), "is being executed").assignAuthor("Oludare Eniola");
		testInfo.log(Status.INFO, result.getName() + " test Started");
	}

	@AfterMethod()
	public void getStatus(ITestResult result) {
		if (ITestResult.SUCCESS == result.getStatus()) {
			testInfo.pass(result.getName() + " Passed");
			testInfo.log(Status.PASS, result.getName() + " test passed");
		}

		else if (ITestResult.STARTED == result.getStatus()) {
			testInfo.log(Status.INFO, result.getName() + " test started");
		}

		else if (ITestResult.SKIP == result.getStatus()) {
			testInfo.log(Status.SKIP,
					result.getName() + " test is skipped and the reason is:- " + result.getThrowable());
		}

		else if (ITestResult.FAILURE == result.getStatus()) {
			SendEmails.sendEmai(useFileData("htmlReportPath"), "emailaddress goes here");
			try { // adding screenshots upon ItestResult failed status
				String outcome = takeScreenShot((result.getName()));
				testInfo.addScreenCaptureFromPath(outcome); // ExtentTest with snapshot
				testInfo.fail((result.getName() + " Failed"),
						MediaEntityBuilder.createScreenCaptureFromPath(outcome).build()); // Appends snapshot in the log
																							// report
				testInfo.log(Status.ERROR, result.getName() + " test failed" + result.getThrowable());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@BeforeTest()
	public void init() {

		config = ConfigGenerator.getInstance();
		if (extent == null)
			extent = new ExtentReports();
		try {
			recorder = new ATUTestRecorder(videoFolder, TestVideoRecord, false);
			recorder.start();
		} catch (ATUTestRecorderException e) {
			e.printStackTrace();
		}
		// try and see effect of appending time to this
		htmlReporter = new ExtentHtmlReporter(new File(useFileData("htmlReport") + "_" + timeFormat + ".html")); 
		htmlReporter.loadXMLConfig(new File(useFileData("extent_config_xml")));
		htmlReporter.config().setTheme(Theme.DARK); // Theme background - dark, standard
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.config().setDocumentTitle("Test Execution - ExtentReports");

		htmlReporter.setAppendExisting(true); // allows appending test information to an existing report.

		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); // chart location - top, bottom
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a"); // set timeStamp format

		// extent.setTestDisplayOrder(TestDisplayOrder.NEWEST_FIRST); //Set test display
		// order in the Pro version of Extent-Reporter
		// htmlReporter.config().setCreateOfflineReport(true); // The HtmlReporter
		// allows creating offline report in the Pro version of Extent-Reporter

		// Additional information that makes our report looks nice
		extent.setSystemInfo("Host Name", "Novenos IT Solutions Inc.");
		extent.setSystemInfo("Environment", "QA/Automation Testing");
		extent.setSystemInfo("Version", "V-1.0.0");
		extent.setSystemInfo("OS", "Windows-10");
		extent.attachReporter(htmlReporter);

		// loading log file
		PropertyConfigurator.configure(useFileData("logFile"));
	}

	@AfterClass(alwaysRun = true)
	public void endTest() throws Exception {
		recorder.stop();
		testInfo.log(Status.INFO, "<a href='" + videoFolder + "/" + TestVideoRecord + ".mov"
				+ "'> <span class='label info'> Download Video</span></a>");
		extent.flush();
		// closeBrowser();
		SendEmails.sendEmai(useFileData("htmlReportPath"), "emailaddress goes here");
		Thread.sleep(4000);
		launchHtmlReport();
	}

	// To insert properties files value
	public String useFileData(String configFileData) {
		return ConfigGenerator.configDataHolder.get(configFileData);
	}

	// Excel reader for data-driven
	public String[][] getDataFromSpreadsheet(String excelWorkbookName, String sheetName) {
		excel = new ExcelReader(useFileData("excelPath"));
		String[][] data = excel.getDataFromSheet(sheetName, excelWorkbookName);
		return data;
	}
	
	public Object[][] getExcelData(String excelName, String sheetName, String testName) {
    //  The argument supplied here is the path to the package that contains all forms of data/files
		String excellocation = TestBase.getResourcePath("/src/main/java/com/test/automation/POMFramework/dataFile/") + excelName;
		ExcelReader readDataFromExcelSheet = new ExcelReader();
		return readDataFromExcelSheet.getExcelDataBasedOnStartingPoint(excellocation, sheetName, testName);
	}

	public void updateResult(String excellocation, String sheetName, String testCaseName, String testStatus) throws IOException {
		ExcelReader readDataFromExcelSheet = new ExcelReader();
		readDataFromExcelSheet.updateResult(excellocation, sheetName, testCaseName, testStatus);
	}
	
	public Object[] dataParsing(String[][] data, int col){
		ExcelReader readDataFromExcelSheet = new ExcelReader();
		return readDataFromExcelSheet.parseData(data,col);
	}
	
	public void selectBrowser(String browserType) {
		System.out.println(System.getProperty("os.name"));

		if (System.getProperty("os.name").contains("Windows 10")) {
			if (browserType.equalsIgnoreCase("firefox")) {
				System.setProperty(useFileData("firefoxDriver"), useFileData("windowsFirefoxDriverPath"));
				// System.setProperty(useFileData("firefoxDriver"),  System.getProperty("user.dir") + "/path to the driver location");
				// System.setProperty("webdriver.gecko.driver"), getResourcePath(resource) NB: resource arg will be declared at method definition level
				log("Lauching " + browserType + " browser");
				driver = new FirefoxDriver();
				maximizeWindow();
			} else if (browserType.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars");
				options.addArguments("--disable-extensions");
				options.addArguments("--start-maximized");
				// options.addArguments("headless"); // To launch headless browser
				driver = new ChromeDriver(options);
				maximizeWindow();
			}

			else if (System.getProperty("os.name").contains("Mac")) {
				if (browserType.equalsIgnoreCase("firefox")) {
					System.setProperty(useFileData("firefoxDriver"), useFileData("macFirefoxDriverPath"));
					driver = new FirefoxDriver();
					maximizeWindow();
				} else if (browserType.equalsIgnoreCase("chrome")) {
					driver = new ChromeDriver();
					maximizeWindow();
				}
			}
		}
	}

	/**
	 * This method will return File resource path/location irrespective of driver location
	 */
    
    // I might need to move this to another package!   =============================================================================!!!!!!!!!!!!!!!!!!!!!!
	public static String getResourcePath(String resource) {
		return (System.getProperty("user.dir") + resource);
	}

	public static InputStream getResourcePathInputStream(String path) throws FileNotFoundException {
		return new FileInputStream(TestBase.getResourcePath(path));
	}

	public static void waitForElement(WebElement element, int timeOutInSeconds) {
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
			}	};
	}

	public static void waitForElement(WebElement element, long timeout) { // using locator
		log("waiting for element to be present..");
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(ExpectedConditions.visibilityOf(element));
		log("element is present...");
	}

	public static void waitImplicitly(long timeout) {
		log("waiting for all elements to be present..");
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}

	public void fluentWait(String element) {
		log("waiting for element to be present..");
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(20, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS).ignoring(org.openqa.selenium.NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() {

			public WebElement apply(WebDriver driver) {
				log("Locating element " + element);
				return driver.findElement(By.xpath("//*[contains(text(),'" + element + "')]"));
			}
		});
	}

	public WebElement waitForElementToBeClickable(WebDriver driver, long time, WebElement element) {
		log("waiting for element to be present..");
		WebDriverWait wait = new WebDriverWait(driver, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public String takeScreenShot(String result) {
		if (result == "")
			result = "blank";
		File destFile = null;
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		destFile = new File(
				useFileData("failedTestScreenShot") + "/failure_screenshots/" + result + "_" + timeFormat + ".png");
		try {
			FileUtils.copyFile(source, destFile);
			System.out.println("Screenshot taken");
			// This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
		}
		return destFile.toString();
	}

	public String takeScreenShotOnSuccess(String result) {
		if (result == "")
			result = "blank";
		File destFile = null;
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ useFileData("screenShot");
			destFile = new File((String) reportDirectory + "/" + result + "_" + timeFormat + ".png");
			FileUtils.copyFile(scrFile, destFile);
			System.out.println("Screenshot taken");
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot" + e.getMessage());
		}
		return destFile.toString();
	}

	public void launchApplication() {
		selectBrowser(useFileData("browserType"));
		pageLoad(25);
		getUrl(useFileData("url"));
	}

	// No reference to Configuration File (Generic)
	public void launchApplication(String browserType, long timeInSeconds, String url) {
		selectBrowser(browserType);
		pageLoad(timeInSeconds);
		getUrl(url);
	}

	public void launchHtmlReport() {
		selectBrowser(useFileData("browserType"));
		pageLoad(10);
	// 	Path top the Extent HTML Report which would be launched upon completion of test
		getUrl(useFileData("htmlReport") + "_" + timeFormat + ".html");
	}

	protected void getUrl(String url) {
		log("Navigating to: " + url);
		driver.get(url);
		waitImplicitly(5);
	}

	public void pageLoad(long timeOut) {
		driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
	}

	public void closeBrowser() {
		driver.close();
	}

	public void closeAllBrowsers() {
		driver.quit();
	}

	public void maximizeWindow() {
		driver.manage().window().maximize();
	}

	public void navigateBack() {
		driver.navigate().back();
	}

	public void navigateForward() {
		driver.navigate().forward();
	}

	public void refresh() {
		driver.navigate().refresh();
	}

	public static void log(String result) {
		log.info(result);
		Reporter.log(result);
	}

	public void getApplicationTitle() {
		driver.getTitle();
	}

	public String getElementAttribute(String element, String htmlAttribute) {
		log("Getting attribute of " + element);
		return getElement(element).getAttribute(htmlAttribute);
	}

	
	
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public void SwitchToWindow(int index) {
		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		if (index < 0 || index > windowsId.size())
			throw new IllegalArgumentException("Invalid Index : " + index);
		driver.switchTo().window(windowsId.get(index));
	}

	public void switchToParentWindow() {
		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		// driver.switchTo().defaultContent(); // same as the above code
		driver.switchTo().window(windowsId.get(0));
	}

	public void switchToParentAndCloseChildWindow() {
		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		for (int i = 1; i < windowsId.size(); i++) {
			driver.switchTo().window(windowsId.get(i));
			driver.close();
		}
		switchToParentWindow();
	}

	public void switchToFrame(By locator) {
		driver.switchTo().frame(driver.findElement(locator));
	}

	public void switchToFrame(String nameOrId) {
		driver.switchTo().frame(nameOrId);
	}

	public void switchToFrame(int index) {
		driver.switchTo().frame(index);
	}

	public void switchToTab(int indesOfTab) {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(indesOfTab));
	}

	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}
	
	

	// Generic xPath locator NB: We can also us CSS
	public WebElement getElement(String data) {
		return driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"));
	}

	// Generic elements locator using CSS NB: We can also us xPath
	public List<WebElement> getElements(String elements) {
		log("Locating web element " + elements);
		return driver.findElements(By.cssSelector("*[id~='" + elements + "']"));
	}

	// Generic CSS element locator method
	public WebElement getElementByName(String element) {
		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[name*='" + element + "']"));
	}

	// Generic CSS element locator method
	public WebElement getElementByClass(String element) {
		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[class*='" + element + "']"));
	}

	// Generic CSS element locator method
	public WebElement getElementById(String element) {
		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[id*='" + element + "']"));
	}

	public WebElement locateElement(By locator) {
		if (AssertionTestScript.isElementPresentQuick(locator))
			return driver.findElement(locator);
		try {
			throw new NoSuchElementException("Element Not Found : " + locator);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	

	
	// Generic expandable link/menu method for text displayed element
	public void clickOnExpandableMenu(String element) {
		driver.findElement(By.xpath("//*[contains(text(),'" + element + "') and @aria-expanded='false')]")).click();
		log("Clicking on expandable menu " + element);
	}

	// Generic expanded link/menu method for text displayed element
	public void clickOnExpandedMenu(String expandedMenu, String element) {
		driver.findElement(By.xpath("//*[contains(text(),'" + expandedMenu
				+ "') and @aria-expanded='true']/following-sibling::ul/child::li/child::a[contains(text),'" + element
				+ "')]")).click();
		log("Clicking on expanded menu " + element);
	}

	// For POM Element
	public void click_OR_Select_Element(WebElement element) {
		log("Clicking on " + element);
		element.click();
	}

	// For generic Element (non POM)
	public void click_OR_Select_Element(String element) {
		log("Clicking on " + element);
		getElement(element).click();
	}

	// For POM Element
	public void setValueFor(WebElement element, String value) {
		log("Clicking on " + element);
		element.sendKeys(value);
	}

	// Generic text/input-box
	public void setValueFor(String textbox, String value) {
		log("Entering data in :" + textbox);
		getElement(textbox).sendKeys(value);
	}

	// For POM Element text area with POM
	public void clearTextArea(WebElement textbox) {
		log("Clearing on " + textbox);
		textbox.clear();
	}

	// Generic text area without POM
	public void clearTextArea(String textbox) {
		log("Clearing on " + textbox);
		getElement(textbox).clear();
	}

	// Review this bootstrap method later
	public void bootStrapWithSimilarLocatorsWithoutFrame(WebElement bootStrapElement, String bootStrapSubElements,
			String itemToClick) {
		click_OR_Select_Element(bootStrapElement);
		List<WebElement> elementList = getElements(bootStrapSubElements);
		for (WebElement ele : elementList) {
			String dd_value = ele.getAttribute("innerHTML");
			if (dd_value.contentEquals(itemToClick)) {
				ele.click();
				break;
			}
		}
	}

	public void bootStrapWithSimilarLocatorsWithinSameFrame(WebElement bootStrapElement, String bootStrapSubElements,
			String itemToClick, String frameName) {
		click_OR_Select_Element(bootStrapElement);
		switchToFrame(frameName);
		List<WebElement> elementList = getElements(bootStrapSubElements);
		for (WebElement ele : elementList) {
			String dd_value = ele.getAttribute("innerHTML");
			if (dd_value.contentEquals(itemToClick)) {
				ele.click();
				break;
			}
			switchToDefaultContent();
		}
	}

	public WebElement bootStrapItemWithDistinctLocator(WebElement bootStrapElement, String element) {
		click_OR_Select_Element(bootStrapElement);
		return getElement(element);
	}

}
