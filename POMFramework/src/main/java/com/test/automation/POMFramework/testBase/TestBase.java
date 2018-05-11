package com.test.automation.POMFramework.testBase;

// Selenium online help community  ==> http://selenium.10932.n7.nabble.com/Selenium-Users-f8051.html  

import java.io.File;
import java.io.IOException;
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
import org.openqa.selenium.By;
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
import org.openqa.selenium.support.ui.Wait;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import resources.ConfigurationDataSource;
import resources.LogHelper;
import resources.ReadDataFromExcelSheet;
import resources.ResourceHelper;
import utilities.WaitHelper;

/**
 * @author Olu Eniola
 */

public class TestBase {

	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest testInfo;
//	public static ATUTestRecorder recorder;
	public ExtentReports extent;
	public static ITestResult result;
	static Calendar calendar = Calendar.getInstance();
	public static String videoFolder = (System.getProperty("user.dir") + "/test-output/TestExecutionVideos");
	static SimpleDateFormat formater = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");
	static String timeFormat = formater.format(calendar.getTime());
	public static String TestVideoRecord = "TestVideo-" + timeFormat;
	public static WebDriver driver;
	public static Wait<WebDriver> wait;
	ReadDataFromExcelSheet excel;
//	ConfigGenerator config;
	
	public static Logger log = LogHelper.getLogger(TestBase.class);

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
		} else if (ITestResult.STARTED == result.getStatus()) {
			testInfo.log(Status.INFO, result.getName() + " test started");
		} else if (ITestResult.SKIP == result.getStatus()) {
			testInfo.log(Status.SKIP,
					result.getName() + " test is skipped and the reason is:- " + result.getThrowable());
		} else if (ITestResult.FAILURE == result.getStatus()) {
			try { // ...... adding screenshots upon ItestResult failed status ......new File
				String outcome = takeScreenShotOnFailure((result.getName()));
			// 	ExtentTest with snapshot
 				testInfo.addScreenCaptureFromPath(outcome); 
				testInfo.fail((result.getName() + " Failed"),
						MediaEntityBuilder.createScreenCaptureFromPath(outcome).build()); 
				// Appends snapshot in the log report
				testInfo.log(Status.ERROR, result.getName() + " test failed" + result.getThrowable());

			} catch (Exception e) {
				System.out.println(e.getMessage());
 			}
		}

	}

	@BeforeTest(alwaysRun = true)
	public void init() {

		// config = ConfigGenerator.getInstance();
		/*
		 * try { recorder = new ATUTestRecorder(videoFolder, TestVideoRecord,
		 * false); recorder.start();
		 * 
 		 * } catch (ATUTestRecorderException e) { e.printStackTrace(); }
		 */
		
		if (extent == null)
			extent = new ExtentReports();
		// .... Additional information that makes our report looks nice ....
		extent.setSystemInfo("Host Name", "Novenos IT Solutions Inc.");
		extent.setSystemInfo("Environment", "QA/Automation Testing");
		extent.setSystemInfo("Version", "V-1.0.0");
		extent.setSystemInfo("OS", "Windows-10");
		extent.attachReporter(htmlReporter);

		// ..... Effect of appending time to the generated report ....new File
		htmlReporter = new ExtentHtmlReporter((useFileData("htmlReportPath") + "_" + timeFormat + ".html"));
		htmlReporter.loadXMLConfig((useFileData("extent_config_xml")));
		htmlReporter.config().setTheme(Theme.DARK); // Theme background - dark, // standard
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.config().setDocumentTitle("Test Execution - ExtentReports");
		// allows appending test information to an existing report.
		htmlReporter.setAppendExisting(true); 
		// chart location - top, bottom
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); 		
		// set timeStamp format
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a"); 
		}

	@AfterTest(alwaysRun = true)
	public void endTest() throws Exception {

		//SendEmails.sendEmai(useFileData("failedTestScreenShotPath") + "/failedTestScreenshots/" 
		//			+ result + "_" + timeFormat + ".png", "Britt.Scott@stls.frb.org");
		
		// SendEmails.sendEmai(useFileData("htmlReportPath") + "_" + timeFormat + ".html", "Britt.Scott@stls.frb.org");
		// recorder.stop();
		// testInfo.log(Status.INFO, "<a href='" + videoFolder + "/" + TestVideoRecord + ".mov"
		// + "'> <span class='label info'> Download Video</span></a>");
		extent.flush();
		// closeBrowser();
		Thread.sleep(4000);
		launchHtmlReport();
	}

	// To insert properties file value
	public static String useFileData(String configFileData) {

	//	new ConfigurationDataSource();
		// return ConfigGenerator.configDataHolder.get(configFileData);
		return ConfigurationDataSource.OR.getProperty(configFileData);
	}

	// Excel reader for data-driven
	public String[][] getSpreadSheetData(String excelSheetName, String workbookName) {

		excel = new ReadDataFromExcelSheet(useFileData("excelPath"));
		return excel.getDataFromSpreadSheet(excelSheetName, workbookName);
	}

	public String[][] getSpreadSheetData(String excelFileName, String excelSheetName, String FirstColumnText) {

		String excellocation = ResourceHelper.getResourcePath("testDataFilePath") + excelFileName;
		excel = new ReadDataFromExcelSheet();
		return excel.getSpreadsheetData(excellocation, excelSheetName, FirstColumnText);
	}

	public Object[][] getExcelData(String excelName, String excelSheetName, String testName) {

		// The argument supplied here is the path to the package that contains
		// all forms of data/files
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
				/** To launch headless browser */
				// options.addArguments("headless"); 

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

				System.setProperty(useFileData("ie"), useFileData("winInternetExplorerDriverPath"));
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability("requireWindowFocus", true);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

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

					System.setProperty(useFileData("ie"), useFileData("macInternetExplorerDriverPath"));
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
					// To launch headless chrome browser
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

	public String takeScreenShotOnSuccess(String result) {

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

	public void launchHtmlReport() {

		log("Launching browser...");
		selectBrowser(useFileData("browser"));
	//	Path top the Extent HTML Report which would be launched upon completion of test
		getUrl(useFileData("htmlReportPath" + "_" + timeFormat + ".html"));
		loadPage(10);
		log("Page loaded!");
	}

	protected static void getUrl(String url) {

		log("Navigating to: " + url);
		driver.navigate().to(url);
		new WaitHelper(driver).waitImplicitly(5);
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

	public static void log(String result) {

		log.info(result);
		Reporter.log(result);
	}

	public static void getApplicationTitle() {

		driver.getTitle();
	}

	public static String getElementAttribute(String element, String htmlAttribute) {

		log("Getting attribute of " + element);
		return getElement(element).getAttribute(htmlAttribute);
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

	
	public static WebElement locateElement(WebElement locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	// Generic xPath locator NB: We can also us CSS
	public static WebElement getElement(String data) {

		return driver.findElement(By.xpath("//*[contains(text(),'" + data + "')]"));
	}

	// Generic CSS locator for Link
		public static WebElement getElementByLink(String data) {
			// I need to make this method work by updating the syntax
		return driver.findElement(By.cssSelector("a[link*='"+ data + "']"));
		}
		
	// Generic elements locator using CSS NB: We can also us xPath
	public static List<WebElement> getElements(String elements) {

		log("Locating web element " + elements);
		return driver.findElements(By.cssSelector("[id*='" + elements + "']"));
	//	return driver.findElements(By.cssSelector("*[id~='" + elements + "']"));	
	}

	// Generic CSS element locator method
	public static WebElement getElementByName(String element) {

		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[name*='" + element + "']"));
	}

	// Generic CSS element locator method
	public static WebElement getElementByClass(String element) {

		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[class*='" + element + "']"));
	}

	// Generic CSS element locator method
	public static WebElement getElementById(String element) {

		log("Locating web element " + element);
		return driver.findElement(By.cssSelector("[id*='" + element + "']"));
	}

	// Generic expandable link/menu method for text displayed element
	public static void clickOnExpandableMenu(String element) {

		driver.findElement(By.xpath("//*[contains(text(),'" + element + "') and @aria-expanded='false')]")).click();
		log("Clicking on expandable menu " + element);
	}

	// Generic expanded link/menu method for text displayed element
	public static void clickOnExpandedMenu(String expandedMenu, String element) {

		driver.findElement(By.xpath("//*[contains(text(),'" + expandedMenu

				+ "') and @aria-expanded='true']/following-sibling::ul/child::li/child::a[contains(text),'" 
				+ element + "')]")).click();
		log("Clicking on expanded menu " + element);
	}

	public static void clickOrSelectElement(WebElement element) {

		log("Clicking on " + element);
		element.click();
	}

	// For generic Element (non POM)
	public static void clickOrSelectElement(String element) {

		log("Clicking on " + element);
		getElement(element).click();
	}

	public static void setValueFor(WebElement element, String value) {

		log("Clicking on " + element);
		element.sendKeys(value);
	}

	// Generic text/input-box (non POM)
	public static void setValueFor(String textboxName, String value) {

		log("Entering data in :" + textboxName);
		getElement(textboxName).sendKeys(value);
	}

	public static void clearTextArea(WebElement textbox) {

		log("Clearing on " + textbox);
		textbox.clear();
	}

	// Generic text area (non POM)
	public static void clearTextArea(String textbox) {

		log("Clearing on " + textbox);
		getElement(textbox).clear();
	}

	// Review this bootstrap method later
	public static void bootStrapWithSimilarLocatorsWithoutFrame(WebElement bootStrapElement,
			String bootStrapSubElements, String itemToClick) {

		clickOrSelectElement(bootStrapElement);
		List<WebElement> elementList = getElements(bootStrapSubElements);

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

		clickOrSelectElement(bootStrapElement);
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

	public static WebElement bootStrapItemWithDistinctLocator(WebElement bootStrapElement, String element) {

		clickOrSelectElement(bootStrapElement);
		return getElement(element);
	}

}
