package com.test.automation.POMFramework.testBase;
// Selenium online help community  ==> http://selenium.10932.n7.nabble.com/Selenium-Users-f8051.html 

/**
 * @author Olu Eniola
 */
package testBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import resources.ConfigurationDataSource;
/** ... Extents Report 3.0.6 version imports ...
//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
//import resources.ConfigurationDataSource;	*/
import resources.LogHelper;
import resources.PropertyFileReader;
import resources.ReadDataFromExcelSheet;
import utilities.CommonHelper;
import utilities.DateTimeHelper;
import utilities.JavaScriptsHelper;
import utilities.ResourceHelper;
import utilities.WaitHelper;
import browserConfiguration.*;
/** Extents Report 3.0.6 version imports */
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.*;

public class TestBase {
	
	// css locator :nth-of-type(n)

	/**
	 * ... Extentreport 2.0 version ... // protected static ExtentReports
	 * extent; // protected static ExtentTest testInfo;
	 */
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentTest testInfo;
	public static ExtentReports extent;
	public static ITestResult result;
	public static File reportDirectery;
	static String testReport = "TestReport_" + DateTimeHelper.currentDateWithTime() + ".html";
	
	private WebDriver driver;
//	private static Wait<WebDriver> wait;
	
	CommonHelper common = new CommonHelper(driver);
	public static Logger log = LogHelper.getLogger(TestBase.class.getName());


	public WebDriver getDriver() {
		return driver;
	}

	public  WaitHelper getWait() {
		return new WaitHelper(driver);
	}

	/*
	public TestBase(){	
		driver = launchApplicationWithUrl();
		wait = new WaitHelper(driver);
		
	//	wait = new WebDriverWait(driver, 10);
	//	setUpDriver(ObjectReader.reader .getBrowserType());	
		
	//		common.init();	Causing recursive launching of the application
	}	
		*/
	
	@BeforeSuite(alwaysRun = true)
	public void init() {
	//	extent = ExtentManager.getInstance();
		ObjectReader.reader = new PropertyFileReader();	
		reportDirectery = new File(propertiesFileData("screenShotPath"));
		setUpDriver();
	//	getWait();
	//	testInfo = extent.createTest(getClass().getSimpleName());
		
	//	setUpDriver(ObjectReader.reader .getBrowserType());
		/**	... Same as the "reportDirectery = new File(propertiesFileData("screenShotPath"));" above ...	*/
	//	reportDirectery = ResourceHelper.getResourcePath(new File(propertiesFileData("screenShotPath")));
		
		GetExtent();
		/**	... For extent report 2.0 version ...	*/
	// 	extent = getReporter(useFileData("htmlReportPath") + testReport);
	}

	@AfterSuite(alwaysRun = true)
	public synchronized void endTest() throws Exception {
		/**
		 * Extent 2.0 version 
		 * public synchronized static ExtentReports getReporter(String filePath) 
		 * { if (extent == null) { extent = new ExtentReports(filePath, true);
		 *  extent.addSystemInfo("Host Name", "Olu").addSystemInfo("Environment", "QA-Demo"); }
		 *   return extent; }
		 * extent.close(); // * For extent 2.0 version to close
		 */

		extent.flush();
		launchTestResult();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		/** Extent 3.0 version */
		testInfo = extent.createTest(method.getName(), " is being executed").assignAuthor("Oludare Eniola");
		testInfo.log(Status.INFO, method.getName() + " test starts executing ");
		log("**************"+method.getName()+" test started ....");
		// testInfo = extent.startTest(result.getName());
		// testInfo.log(LogStatus.INFO, result.getName() + " test Started");
	}

	@AfterMethod()
	public void testResult(ITestResult result) {
		/** Extent 3.0 version */
		if (ITestResult.SUCCESS == result.getStatus()) {
			testInfo.pass(result.getName() + " Passed");
			testInfo.log(Status.PASS, result.getName() + " test passed");
			log(" ...... "+result.getName()+" test passed executed successfully ......");
		} else if (ITestResult.SKIP == result.getStatus()) {
			testInfo.log(Status.SKIP,
					result.getName() + " test is skipped and the reason is:- " + result.getThrowable());
		} else if (ITestResult.FAILURE == result.getStatus()) {
			try {
				String outcome = captureScreen((result.getName()));
				MediaEntityModelProvider mediaEntityProvider = MediaEntityBuilder.createScreenCaptureFromPath(outcome).build();
			//	testInfo.addScreenCaptureFromPath(outcome);
				testInfo.fail((result.getName() + " failed"), mediaEntityProvider);
				testInfo.log(Status.ERROR, result.getName() + " failed" + result.getThrowable());
				log("...... "+result.getName()+" test failed during execution ....");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			log.info("...... "+result.getName()+" has finished executing ......");

	//		extent.flush();
		}

		/**
		 * Extent 2.0 version
		 * 
		 * if (result.getStatus() == ITestResult.FAILURE) { try { String
		 * screenshot = captureScreenOnFailure((result.getName()));
		 * testInfo.log(LogStatus.INFO, (result.getName() + " Failed " +
		 * testInfo.addScreenCapture(screenshot))); testInfo.log(LogStatus.FAIL,
		 * result.getName() + " test failed" + result.getThrowable()); } catch
		 * (Exception e) { System.out.println(e.getMessage()); } } else if
		 * (result.getStatus() == ITestResult.SKIP) {
		 * testInfo.log(LogStatus.SKIP, result.getName() + " test is skipped and
		 * the reason is:- " + result.getThrowable()); } else {
		 * testInfo.log(LogStatus.PASS, "Test passed"); }
		 * extent.endTest(testInfo); extent.flush();
		 */
	}

/*	
		@BeforeClass
	public void beforeClass(){
		testInfo = extent.createTest(getClass().getSimpleName());
	}
*/
	
//	@AfterTest
	public void afterAllTest() throws Exception{
		if(driver!=null){
			driver.quit();
		}
	}
	
	/** ... Extent 3.0 version ... */
	public static ExtentReports GetExtent() {
		if (extent != null) {
			return extent;
		}
		extent = new ExtentReports();
	//	Additional information that makes our report looks nice
		extent.setSystemInfo("Host Name", "FRB ST LOUIS, MO");
		extent.setSystemInfo("Environment", "FRB CARS Team");
		extent.setSystemInfo("Version", "V-1.0.0");
		extent.setSystemInfo("OS", "Windows-10");
		extent.attachReporter(getHtmlReporter());
		return extent;
	}

	private static ExtentHtmlReporter getHtmlReporter() {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + propertiesFileData("htmlReportPath") + testReport);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK); // Theme background - dark,	standard
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.setAppendExisting(true);
		/* chart location - top, bottom set */
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);  
		/* timeStamp format */
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a"); 
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setDocumentTitle("Demo automation report");
		htmlReporter.loadXMLConfig(propertiesFileData("extentConfigXml"));
		return htmlReporter;
	}

	// Initializing and insert config.properties file with its value(s) at run time.
	public static String propertiesFileData(String configFileData) {
		return ConfigurationDataSource.OR.getProperty(configFileData);
	}
	
	/* Initializing and insert config.properties file with its value(s) at run time.
		public static String propertiesFileData2(String configFileData) {
			return PropertyFileReader.OR.getProperty(configFileData);
		}	*/

	/** ... Excel reader for data-driven ... */
	public String[][] getExcelData(String workBookName, String excelSheetName) {
		String excelLocation = ResourceHelper.getResourcePath(propertiesFileData("testDataFilePath"))  + workBookName;
		return ReadDataFromExcelSheet.getDataFromSpreadSheet(excelLocation, excelSheetName);
	}

	public static Object[][] getAllDataInOneExcelSheet(String workBookName, String excelSheetName, String testName) {
		String excelLocation = ResourceHelper.getResourcePath(propertiesFileData("testDataFilePath")) + workBookName;
		return ReadDataFromExcelSheet.getDataFromSpreadSheetBasedOnStartingPoint(excelLocation, excelSheetName, testName);
	}
	
	public String getExcelCellData(String workBookName, String excelSheetName, String colName, int rowNum) {
		String excelLocation = ResourceHelper.getResourcePath(propertiesFileData("testDataFilePath"))  + workBookName;
		return ReadDataFromExcelSheet.getSpreadsheetCellData(excelLocation, excelSheetName, colName, rowNum);
	}

	public void updateResult(String workBookName, String excelSheetName, String testCaseName, String testStatus) throws IOException {
		String excelLocation = ResourceHelper.getResourcePath(propertiesFileData("testDataFilePath")) + workBookName;
		ReadDataFromExcelSheet.updateResult(excelLocation, excelSheetName, testCaseName, testStatus);
	}

	public Object[][] dataParsing(String[][] data, int col) {
		return ReadDataFromExcelSheet.parseData(data, col);
	}

	@SuppressWarnings("deprecation")
	public WebDriver launchWebBrowser(String browserType) {

		System.out.println(System.getProperty("os.name"));
		if (System.getProperty("os.name").contains("Windows")) {
			if (browserType.equalsIgnoreCase("chrome")) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(propertiesFileData("chromeDriver"), propertiesFileData("winChromeDriverPath"));
				ChromeOptions options = new ChromeOptions();
				options.setBinary(propertiesFileData("winChromeDriverBinaryPath"));
				options.addArguments("--test-type");
				options.addArguments("--disable-popup-blocking");
				options.addArguments("disable-infobars");
				options.addArguments("--disable-extensions");
				options.addArguments("--start-maximized");
				/**	 * To launch headless browser * 
				 * //	options.addArguments("headless");
				 */
				DesiredCapabilities chrome = DesiredCapabilities.chrome();
				chrome.setJavascriptEnabled(true);
				options.setCapability(ChromeOptions.CAPABILITY, chrome);
				
				log("Launching " + browserType + " browser");
				new ChromeDriver(options);
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
			}
			else if (browserType.equalsIgnoreCase("firefox")) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(propertiesFileData("firefoxDriver"), propertiesFileData("winFirefoxDriverPath"));
				System.setProperty(propertiesFileData("firefoxDriver"), propertiesFileData("winFirefoxDriverPath"));
				DesiredCapabilities firefox = DesiredCapabilities.firefox();
				FirefoxProfile profile = new FirefoxProfile();
				profile.setAcceptUntrustedCertificates(true);
				profile.setAssumeUntrustedCertificateIssuer(true);
				firefox.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
				firefox.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true); 
				firefox.setCapability(FirefoxDriver.PROFILE, profile); 
				firefox.setCapability("marionette", true);
				driver = new FirefoxDriver(firefox);
				
				/**		OR
				 * FirefoxOptions firefoxOptions = new FirefoxOptions(firefox);
				 * 	//	OR
				 * WebDriver driver = new MarionetteDriver(firefox); 
				 * 	//	OR 
				 * WebDriver driver = new RemoteWebDriver(firefox);
				 */				
				driver.manage().window().maximize();
			}
			else if (browserType.equalsIgnoreCase("IE")) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(propertiesFileData("ieDriver"), propertiesFileData("winIEDriverPath"));
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability("requireWindowFocus", true);
				capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		//		log("Launching " + browserType + " browser");
				driver = new InternetExplorerDriver(capabilities);
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
			}

			else if (System.getProperty("os.name").contains("Mac")) {
				if (browserType.equalsIgnoreCase("IE")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM iexplore");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.setProperty(propertiesFileData("ieDriver"), propertiesFileData("macIEDriverPath"));
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					capabilities.setCapability("requireWindowFocus", true);
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					log("Launching " + browserType + " browser");
					driver = new InternetExplorerDriver(capabilities);
					driver.manage().deleteAllCookies();
					driver.manage().window().maximize();
					}

			 else if (browserType.equalsIgnoreCase("firefox")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.setProperty(propertiesFileData("firefoxDriver"), propertiesFileData("winFirefoxDriverPath"));
					DesiredCapabilities firefox = DesiredCapabilities.firefox();
					FirefoxProfile profile = new FirefoxProfile();
					profile.setAcceptUntrustedCertificates(true);
					profile.setAssumeUntrustedCertificateIssuer(true);
					firefox.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
					firefox.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true); 
					firefox.setCapability(FirefoxDriver.PROFILE, profile); 
					firefox.setCapability("marionette", true);
					driver = new FirefoxDriver(firefox);
					
					/**		OR
					 * FirefoxOptions firefoxOptions = new FirefoxOptions(firefox);
					 * 	//	OR
					 * WebDriver driver = new MarionetteDriver(firefox); 
					 * 	//	OR 
					 * WebDriver driver = new RemoteWebDriver(firefox);
					 */				
					driver.manage().window().maximize();
					} 
			else if (browserType.equalsIgnoreCase("chrome")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM chrome");
					} catch (IOException e) {
						e.printStackTrace();
					}
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--test-type");
					options.addArguments("--disable-popup-blocking");
					options.addArguments("disable-infobars");
					options.addArguments("--disable-extensions");
					options.addArguments("--start-maximized");
					/**	 * To launch headless browser * 
					 * //	options.addArguments("headless");
					 */
					DesiredCapabilities chrome = DesiredCapabilities.chrome();
					chrome.setJavascriptEnabled(true);
					options.setCapability(ChromeOptions.CAPABILITY, chrome); 
					
					log("Launching " + browserType + " browser");
					driver = new ChromeDriver(options);
					driver.manage().deleteAllCookies();
					driver.manage().window().maximize();
				}
			}
		}
		return driver;
	}

	/*
	public String captureScreenOnFailure(String result) {
		if(driver == null){
			log.info("driver is null..");
			return null;
		}
		if (result == "") {
			result = "blank";
		}
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destFile = new File(propertiesFileData("screenShotPath") + "/failedTestScreenshots/" + result + "_"
				+ DateTimeHelper.currentDateWithTime() + ".png");
		try {
			FileUtils.copyFile(source, destFile);
			System.out.println("while test fails, a screenshot is taken");
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='80'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
		}
		return destFile.toString();
	}
	*/

	public String captureScreen(String result) {
		if(driver == null){
			log.info("driver is null..");
			return null;
		}
		if (result == "") {
			result = "blank";
		}
		Reporter.log("captureScreen method called");
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	//	File destFile = new File(propertiesFileData("screenShotPath") + "/pageScreenshots/" + result + "_" + DateTimeHelper.currentDateWithTime() + ".png");
		File destFile =new File(reportDirectery + "/pageScreenshots/" + result + "_" + DateTimeHelper.currentDateWithTime() + ".png");
		try {
			FileUtils.copyFile(scrFile, destFile);
			System.out.println("Web page screenshot captured");
			Reporter.log("<a href='" + destFile + "'> <img src='" + destFile
					+ "' height='100' width='80'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot" + e.getMessage());
		}
		return destFile.toString();
	}
	
	public void setUpDriver() {
		log("Launching the application ..");
		driver = launchWebBrowser(propertiesFileData("browser"));
	//	getWait();
		getWait().pageLoadTime(30);
	//	driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		log("Web page loaded!");
	}
	
	public void launchTestResult() {
		log("Launching the test result/report page ...");
	//	selectBrowser(propertiesFileData("browser"));
		driver.navigate().to((System.getProperty("user.dir") + propertiesFileData("htmlReportPath") + testReport));
		getWait().pageLoadTime(10);
	//	driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	//	log("Test result/report page loaded!");
	}

	public void getPageNavigationScreenshot(String uIPageName) {
		log.info("capturing ui navigation screen...");
		 String screen = captureScreen(uIPageName);
		 try {
			 new JavaScriptsHelper(driver).ZoomInByPercentage(60);
			 testInfo.addScreencastFromPath(screen);
			 new JavaScriptsHelper(driver).ZoomInByPercentage(100);
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	public void getApplicationUrl(){
		driver.navigate().to(propertiesFileData("url"));
	}

	/*
	 public WebDriver getBrowserObject(BrowserType browser) throws Exception{
		try{
			switch(browser){
			case Chrome:

				ChromeBrowser chrome = ChromeBrowser.class.newInstance();
				ChromeOptions option = chrome.getChromeOptions();
				return chrome.getChromeDriver(option);

			case Firefox:
				FirefoxBrowser firefox = FirefoxBrowser.class.newInstance();
				FirefoxOptions options = firefox.getFirefoxOptions();
				return firefox.getFirefoxDriver(options);	

			case Iexplorer:
				IExploreBrowser ie = IExploreBrowser.class.newInstance();
				InternetExplorerOptions cap = ie.getIExplorerCapabilities();
				return ie.getIExplorerDriver(cap);

			default:
				throw new Exception("Browser type: " + browser.name() + " not yet supported");
			}
		}
		catch(Exception e){
			log.info(e.getMessage());
			throw e;
		}
	}
	 
	public void setUpDriver(BrowserType btype) throws Exception{
		driver = getBrowserObject(btype);
		log.info("Initialize Web driver: "+driver.hashCode());
	//	wait = new WaitHelper(driver);	
		waitHelper.waitImplicitly(ObjectReader.reader.getImpliciteWait());
		waitHelper.pageLoadTime(ObjectReader.reader.getPageLoadTime());
		driver.manage().window().maximize();
	}		*/

	public static void changeLogLevel(Level level) {
		Enumeration<?> loggers = LogManager.getCurrentLoggers();
		while (loggers.hasMoreElements()) {
			Logger logger = (Logger) loggers.nextElement();
			logger.setLevel(level);
		}
	}

	/*
	// Moved to the Enum folder of Master package
	public enum logLevel {
		INFO, TRACE, DEBUG, WARN, ERROR, FATAL, OFF
	}	*/

	public static void logType(String comment) {

		switch (ObjectReader.reader.getLogLevel()) {

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

	public static void log(String comment, Level LevelDotLoglevelInUpperCase) {
		changeLogLevel(LevelDotLoglevelInUpperCase);
		log.info(comment);
		log.trace(comment);
		log.debug(comment);
		log.warn(comment);
		log.error(comment);
		log.fatal(comment);
		testInfo.log(Status.INFO, comment);
	/** ... Log output info for extent report 2.0 version  ...*/
	// testInfo.log(LogStatus.INFO, comment);
	}

	public static void log(String comment) {
		log.info(comment);
		Reporter.log(comment);
		testInfo.log(Status.INFO, comment);
		
		/**	... Log output info for extent report 2.0 version	.. */
		// test.log(LogStatus.INFO, comment); 
	}

	public static void logDebug(String comment) {
		log.debug(comment);
	}

	public static void logTrace(String comment) {
		log.trace(comment);
	}
}
