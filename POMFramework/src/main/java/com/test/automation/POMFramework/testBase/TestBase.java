package com.test.automation.POMFramework.testBase;
// Selenium online help community  ==> http://selenium.10932.n7.nabble.com/Selenium-Users-f8051.html 

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import resources.ConfigurationDataSource;
//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import resources.ConfigurationDataSource;
import resources.LogHelper;
import resources.ReadDataFromExcelSheet;
import utilities.ResourceHelper;
import utilities.WaitHelper;

// Extents Report 3.0.6 version imports
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.*;

/**
 * @author Olu Eniola
 */

public class TestBase {

//	public static ATUTestRecorder recorder;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest testInfo;
	public static ExtentReports extent;
//	ConfigGenerator config;

//	protected static  ExtentReports extent;
//	protected static ExtentTest testInfo;
	public static ITestResult result;
	static Calendar calendar = Calendar.getInstance();
	public static String videoFolder = (System.getProperty("user.dir") + "/test-output/TestExecutionVideos");
	static SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
	static String timeFormat = formater.format(calendar.getTime());
	public static String TestVideoRecord = "TestVideo-" + timeFormat;
	public static WebDriver driver;
	public static WebDriverWait wait;
	ReadDataFromExcelSheet excel;
	String testReport = "TestReport_" + timeFormat + ".html";

	public static Logger log = LogHelper.getLogger(TestBase.class);

	@BeforeMethod
	public void beforeMethod(Method result) {

	/**	Extent 3.0 version */
		testInfo = extent.createTest(result.getName(), "is being executed").assignAuthor("Oludare Eniola");
		testInfo.log(Status.INFO, result.getName() + " test Started"); 
		
//		testInfo = extent.startTest(result.getName());
//		testInfo.log(LogStatus.INFO, result.getName() + " test Started");
	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {

		/** Extent 3.0 version */
		if (ITestResult.SUCCESS == result.getStatus()) {
			testInfo.pass(result.getName() + " Passed");
			testInfo.log(Status.PASS, result.getName() + " test passed");
		} else if (ITestResult.STARTED == result.getStatus()) {
			testInfo.log(Status.INFO, result.getName() + " test started");
		} else if (ITestResult.SKIP == result.getStatus()) {
			testInfo.log(Status.SKIP,
					result.getName() + " test is skipped and the reason is:- " + result.getThrowable());
		} else if (ITestResult.FAILURE == result.getStatus()) {
			try { 
				String outcome = takeScreenShotOnFailure((result.getName()));
				testInfo.addScreenCaptureFromPath(outcome); 
				testInfo.fail((result.getName() + " Failed"),
						MediaEntityBuilder.createScreenCaptureFromPath(outcome).build()); 
				// Appends snapshot in the log report
				testInfo.log(Status.ERROR, result.getName() + " test failed" + result.getThrowable());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} 
		
		
		/** Extent 2.0 version 
			
	        if (result.getStatus() == ITestResult.FAILURE) {
	        	try { 			
					String screenshot = takeScreenShotOnFailure((result.getName()));	
					testInfo.log(LogStatus.INFO, (result.getName() + " Failed " + testInfo.addScreenCapture(screenshot)));
					testInfo.log(LogStatus.FAIL, result.getName() + " test failed" + result.getThrowable());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	        } else if (result.getStatus() == ITestResult.SKIP) {
	        	testInfo.log(LogStatus.SKIP, result.getName() + " test is skipped and the reason is:- " + result.getThrowable());
	        } else {
	        	testInfo.log(LogStatus.PASS, "Test passed");
	        }
			extent.endTest(testInfo);
			extent.flush();
			*/
	}

	@BeforeSuite(alwaysRun = true)
	public void init() {
		
	//	new WaitHelper(driver);
	//	extent = getReporter(useFileData("htmlReportPath") + testReport);
		
		/**
		 * // config = ConfigGenerator.getInstance(); // to create & load config.properties file in another way
		 * 
		 * //	 	For Video recording of test execution
		 * 
		 * try { // video recording
		 * 	recorder = new ATUTestRecorder(videoFolder, TestVideoRecord, false); recorder.start();
		 * } catch (ATUTestRecorderException e) { e.printStackTrace(); }			
		 *  */
		
		GetExtent();
	}
	

	@AfterSuite(alwaysRun = true)
	public void endTest() throws Exception {

	/**	 	To send automatic email for test report and failed tests.
	 * //SendEmails.sendEmai(useFileData("failedTestScreenShotPath") + "/failedTestScreenshots/" 
		//			+ result + "_" + timeFormat + ".png", "Scott.Britt@stls.frb.org");
		
		// SendEmails.sendEmai(useFileData("htmlReportPath") + testReport), 
		// "Scott.Britt@stls.frb.org");
		// recorder.stop();	// ** Stop Video Recording **
		// testInfo.log(Status.INFO, "<a href='" + videoFolder + "/" + TestVideoRecord + ".mov"
		// + "'> <span class='label info'> Download Video</span></a>");
		
	//  extent.close();	// * For extent 2.0 ver to close*/
		/** Extent 2.0 version	 
		 public synchronized static ExtentReports getReporter(String filePath) {
		        if (extent == null) {
		            extent = new ExtentReports(filePath, true);	         
		            extent.addSystemInfo("Host Name", "Olu").addSystemInfo("Environment", "QA-Demo");
		        }
		        return extent;
		    } */
		
		extent.flush();
		Thread.sleep(2500);
	//	launchTestResult();
	}
	

	//	Extent 3.0 Version
	public  ExtentReports GetExtent(){

	//	To avoid creating new instance of html file
		if (extent != null)
                return extent; 
           extent = new ExtentReports();
    //	Additional information that makes our report looks nice
		extent.setSystemInfo("Host Name", "Novenos IT Solutions Inc.");
		extent.setSystemInfo("Environment", "FRB CARS Team");
		extent.setSystemInfo("Version", "V-1.0.0");
		extent.setSystemInfo("OS", "Windows-10");
		extent.attachReporter(getHtmlReporter());
		return extent;
	}
	
	private ExtentHtmlReporter getHtmlReporter() {	

		htmlReporter = new ExtentHtmlReporter(useFileData("htmlReportPath") + testReport); 
	//	htmlReporter.loadXMLConfig(new File(useFileData("extent_config_xml")));
        htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK); // Theme background - dark, standard
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.setAppendExisting(true); // allows appending test information to an existing report.
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); // chart location - top, bottom
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a"); // set timeStamp format
        htmlReporter.config().setDocumentTitle("Demo automation report");
        return htmlReporter;
	}/**	*/

	// Initializing and insert config.properties file with its value(s) at run time.
	public static String useFileData(String configFileData) {
		
	//	return ConfigGenerator.configDataHolder.get(configFileData);
		return ConfigurationDataSource.OR.getProperty(configFileData);
	}

	// Excel reader for data-driven
	public String[][] getSpreadSheetData(String excelSheetName, String workbookName) {

		excel = new ReadDataFromExcelSheet(useFileData("excelPath"));
		return excel.getDataFromSpreadSheet(excelSheetName, workbookName);
	}

	public String[][] getSpreadSheetData(String workBookName, String excelSheetName, String FirstColumnText) {

		String excellocation = ResourceHelper.getResourcePath("testDataFilePath") + workBookName;
		excel = new ReadDataFromExcelSheet();
		return excel.getSpreadsheetData(excellocation, excelSheetName, FirstColumnText);
	}

	public Object[][] getExcelData(String excelName, String excelSheetName, String testName) {

		// The argument supplied here is the path to the package that contains all forms of data/files
		String excelLocation = ResourceHelper.getResourcePath("testDataFilePath") + excelName;
		excel = new ReadDataFromExcelSheet();
		return excel.getExcelDataBasedOnStartingPoint(excelLocation, excelSheetName, testName);
	}

	public void updateResult(String excelLocation, String excelSheetName, String testCaseName, String testStatus)
			throws IOException {

		excel = new ReadDataFromExcelSheet();
		excel.updateResult(excelLocation, excelSheetName, testCaseName, testStatus);
	}

	public Object[][] dataParsing(String[][] data, int col) {

		excel = new ReadDataFromExcelSheet();
		return excel.parseData(data, col);
	}

	public void selectBrowser(String browserType) {

		System.out.println(System.getProperty("os.name"));
		if (System.getProperty("os.name").contains("Windows")) {
			if (browserType.equalsIgnoreCase("chrome")) {
				System.out.println(browserType);
				try {
					Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(useFileData("chromeDriver"), useFileData("winChromeDriverPath"));				
				ChromeOptions options = new ChromeOptions();
				options.setBinary(useFileData("winChromeDriverBinaryPath"));
				options.addArguments("disable-infobars");
				options.addArguments("--disable-extensions");
				options.addArguments("--start-maximized");	
				/** To launch headless browser *		*/
				// options.addArguments("headless"); 	
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options); 
				log("Launching " + browserType + " browser");
				driver = new ChromeDriver(options);
				driver.manage().deleteAllCookies();
				maximizeWindow();
			}

			else if (browserType.equalsIgnoreCase("InternetExplorer")) {
				System.out.println(browserType);
				try {
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(useFileData("ieDriver"), useFileData("winInternetExplorerDriverPath"));
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,	true);
				capabilities.setCapability("requireWindowFocus", true);
				//capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				log("Launching " + browserType + " browser");
				driver = new InternetExplorerDriver(capabilities);
				driver.manage().deleteAllCookies();
				maximizeWindow();
			}
			
			else if (System.getProperty("os.name").contains("Mac")) {
				if (browserType.equalsIgnoreCase("InternetExplorer")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.setProperty(useFileData("ieDriver"), useFileData("macInternetExplorerDriverPath"));
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					capabilities.setCapability("requireWindowFocus", true);
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

					log("Launching " + browserType + " browser");
					driver = new InternetExplorerDriver(capabilities);
					driver.manage().deleteAllCookies();
					maximizeWindow();
					
				} else if (browserType.equalsIgnoreCase("chrome")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM iexplore");
					} catch (IOException e) {
						e.printStackTrace();
					}
					ChromeOptions options = new ChromeOptions();
					options.addArguments("disable-infobars");
					options.addArguments("--disable-extensions");
					options.addArguments("--start-maximized");
					/** To launch headless browser *		*/
					// options.addArguments("headless"); 

					log("Launching " + browserType + " browser");
					driver = new ChromeDriver(options);
					driver.manage().deleteAllCookies();
					maximizeWindow();
				}
			}
		}
	}

	
	public String takeScreenShotOnFailure(String result) {

		if (result == "")
			result = "blank";
		File destFile = null;
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		destFile = new File(useFileData("failedTestScreenShotPath") + "/failedTestScreenshots/" 
					+ result + "_" + timeFormat + ".png");
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

	public String takeScreenShot(String result) {

		if (result == "")
			result = "blank";
		File destFile = null;
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ useFileData("screenShotPath");
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

		log("Launching browser...");
		selectBrowser(useFileData("browser"));
		getUrl(useFileData("url"));
		loadPage(20);
		log("Page loaded!");
	}

	public void launchApplication(String browserType, long timeInSeconds, String url) {

		log("Launching browser...");
		selectBrowser(browserType);
		loadPage(timeInSeconds);
		getUrl(url);
		log("Page loaded!");
	}

	public void launchTestResult() {

		log("Launching browser...");
		selectBrowser(useFileData("browser"));
		getUrl(useFileData("htmlReportPath") + testReport);
	//	getUrl("www." + useFileData(("htmlReportPath") + testReport);	
		loadPage(10);
		log("Page loaded!");
	}

	protected static void getUrl(String url) {

		log("Navigating to: " + url);
		driver.navigate().to(url);
	//	WaitHelper.waitImplicitly(5);
	}

	public static void loadPage(long timeOut) {

		driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
	}

	public static void closeBrowser() {

		driver.close();
	}

	public static void closeAllBrowsers() {

		driver.quit();
	}

	public static void maximizeWindow() {

		driver.manage().window().maximize();
	}

	public static void navigateBack() {

		driver.navigate().back();
	}

	public static void navigateForward() {

		driver.navigate().forward();
	}

	public static void refresh() {

		driver.navigate().refresh();
	}

	public static void changeLogLevel(Level level) {
	    Enumeration<?> loggers = LogManager.getCurrentLoggers();
	    while(loggers.hasMoreElements()) {
	        Logger logger = (Logger) loggers.nextElement();
	        logger.setLevel(level);
	    }
	}
	
	public enum logLevel {
		INFO, TRACE, DEBUG, WARN, ERROR, FATAL, OFF
	}

	public static void log(logLevel logValue, String comment){ 
		
	switch (logValue) { 
	
	case INFO:
		log.info(comment);
		break;

	case TRACE:
		log.trace(comment);
		break;
		
	case DEBUG:
		log.debug(comment);
		break;
		
	case WARN:
		 log.warn(comment);
		break;
		
	case ERROR:
		 log.error(comment);
		break;
		
	case FATAL:
		log.fatal(comment);
		break;
		
	default:
			log.notifyAll();
		break;
	}
	
	}
	
	public static void log(String comment, Level LevelDotLoglevelInUpperCase){
	
		changeLogLevel(LevelDotLoglevelInUpperCase);	
		log.info(comment);	
		log.trace(comment);		
		log.debug(comment);	
		log.warn(comment);
		log.error(comment);
		log.fatal(comment);	
		testInfo.log(Status.INFO, comment);
	//	testInfo.log(LogStatus.INFO, comment);	// Log output info for extent report 2.0 version
	}
	
	
	public static void log(String comment) {

		log.info(comment);
		testInfo.log(Status.INFO, comment);
	//	testInfo.log(LogStatus.INFO, comment);	// Log output info for extent report 2.0 version
	}
	
	public static void logDebug(String comment) {

		log.debug(comment);
	}
	
	public static void logTrace(String comment) {

		log.trace(comment);
	}

	public static Set<String> getWindowHandles() {

	return driver.getWindowHandles();
	}

	public static void SwitchToWindow(int index) {

		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		if (index < 0 || index > windowsId.size())
			throw new IllegalArgumentException("Invalid Index : " + index);
		driver.switchTo().window(windowsId.get(index));
	}

	public static void switchToParentWindow() {

		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		// driver.switchTo().defaultContent(); // same as the above code
		driver.switchTo().window(windowsId.get(0));
	}

	public static void switchToParentAndCloseChildWindow() {

		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandles());
		for (int i = 1; i < windowsId.size(); i++) {
			driver.switchTo().window(windowsId.get(i));
			driver.close();
		}
		switchToParentWindow();
	}

	public static void switchToFrame(By locator) {

		driver.switchTo().frame(driver.findElement(locator));
	}

	public static void switchToFrame(String nameOrId) {

		driver.switchTo().frame(nameOrId);
	}

	public void switchToFrame(int index) {

		driver.switchTo().frame(index);
	}

	public static void switchToTab(int indesOfTab) {

		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(indesOfTab));
	}

	public static void switchToDefaultContent() {

		driver.switchTo().defaultContent();
	}

	public static void getApplicationTitle() {

		driver.getTitle();
	}
	
	public static WebElement getWebElement(WebElement locator) {
		return wait.until(ExpectedConditions.visibilityOf(locator));
	//	return locator;
	}
	
	// Generic xPath locator NB: We can also us CSS
	public static WebElement getWebElement(String data) {

	//	return wait.until(ExpectedConditions
	//			.visibilityOf(driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"))));
		return (driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]")));
	}
	
	public static List<WebElement> getWebElements(String locator) {
		
		return  driver.findElements(By.xpath("//*[contains(text(),'" + locator + "')]"));
	}
		
	@SuppressWarnings("unchecked")
	public static List<WebElement> getWebElements(WebElement locators) {
		return (List<WebElement>) wait.until(ExpectedConditions.visibilityOf(locators));
	}
	
	// Generic CSS locator for Link
		public static WebElement getWebElementByLink(String data) {
		
	return wait.until(ExpectedConditions
			.visibilityOf( driver.findElement(By.cssSelector("a[href*='"+ data + "']"))));
	}
	
	// Generic CSS locator for image
	public static WebElement getWebElementByImage(String element){
		
		return wait.until(ExpectedConditions
				.visibilityOf(driver.findElement(By.cssSelector("img[src*='"+ element +"'']"))));
	}
		
	// Generic CSS element locator method
	public static WebElement getWebElementByName(String element) {

		return driver.findElement(By.cssSelector("[name*='" + element + "']"));
	}

	// Generic CSS element locator method
	public static WebElement getWebElementByClass(String element) {

		return driver.findElement(By.cssSelector("[class*='" + element + "']"));
	}

	// Generic CSS element locator method
	public static WebElement getWebElementById(String element) {

		return driver.findElement(By.cssSelector("[id*='" + element + "']"));
	}

	// Generic expandable link/menu method for text displayed element
	public static void clickOnExpandableMenu(String element) {

		driver.findElement(By.xpath("//*[contains(text(),'" + element + "') and @aria-expanded='false')]")).click();
	}

	// Generic expanded link/menu method for text displayed element
	public static void clickOnExpandedMenu(String expandedMenu, String element) {

		driver.findElement(By.xpath("//*[contains(text(),'" + expandedMenu

				+ "') and @aria-expanded='true']/following-sibling::ul/child::li/child::a[contains(text),'" 
				+ element + "')]")).click();
	}

	public static void clickOnWebElement(WebElement element) {
		
		if (element!=null)
		try
			{
		element.click();
	//	wait.until(ExpectedConditions.elementToBeClickable(element)).click();		
			}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	// For generic Element (non POM)
	public static void clickOnWebElement(String element) {

		getWebElement(element).click();
	}

	public static void setValue(WebElement element, String value) {
		if (element!=null)
			try
		{
				wait.until(ExpectedConditions.visibilityOf(element));
				element.sendKeys(value);
				element.sendKeys(Keys.TAB);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	// Generic text/input-box (non POM)
	public static void setValue(String textboxName, String value) {

		getWebElement(textboxName).sendKeys(value);
		getWebElement(textboxName).sendKeys(Keys.TAB);
	}

	public static void clearTextArea(WebElement textbox) {
		if (textbox!=null)
			try
		{
		textbox.clear();
	}
	catch(Exception ex) {
		ex.printStackTrace();
		}
	}

	// Generic text area (non POM)
	public static void clearTextArea(String textbox) {

		getWebElement(textbox).clear();
	}

	
	// Review this bootstrap method later
	public static void bootStrapWithSimilarLocatorsWithoutFrame(WebElement bootStrapElement,
			String bootStrapSubElements, String itemToClick) {

		getWebElement(bootStrapElement);
		List<WebElement> elementList = getWebElements(bootStrapSubElements);

		for (WebElement ele : elementList) {
			String dd_value = ele.getAttribute("innerHTML");

			if (dd_value.contentEquals(itemToClick)) {
				ele.click();
				break;
			}
		}
	}

	public static void bootStrapWithSimilarLocatorsWithinSameFrame(WebElement bootStrapElement,
			String bootStrapSubElements, String itemToClick, String frameName) {

		clickOnWebElement(bootStrapElement);
		switchToFrame(frameName);
		List<WebElement> elementList = getWebElements(bootStrapSubElements);

		for (WebElement ele : elementList) {
			String dd_value = ele.getAttribute("innerHTML");

			if (dd_value.contentEquals(itemToClick)) {
				ele.click();
				break;
			}
			switchToDefaultContent();
		}
	}

	public static WebElement bootStrapItemWithDistinctLocator(WebElement bootStrapElement, String element) {

		clickOnWebElement(bootStrapElement);
		return getWebElement(element);
	}

}
