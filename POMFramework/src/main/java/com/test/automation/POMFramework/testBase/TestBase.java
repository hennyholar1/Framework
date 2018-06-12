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

/**  ... Extentreport 2.0 version ...
 * 	//	protected static  ExtentReports extent;
 * 	//	protected static ExtentTest testInfo; 
 */
	public static ITestResult result;
	static Calendar calendar = Calendar.getInstance();
	public static String videoFolder = (System.getProperty("user.dir") + "/test-output/TestExecutionVideos");
	static String testReport = "TestReport_" + DateTimeHelper.getCurrentAndDateTime() + ".html";
	public static String TestVideoRecord = "TestVideo-" + DateTimeHelper.getCurrentAndDateTime();
	public static WebDriver driver;
	public static WebDriverWait wait;
	ReadDataFromExcelSheet excel;
	

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
		WaitHelper.sleep(1);
		launchTestResult();
	}
	
/** 	...	Extent 3.0 version	...	*/	
	public static  ExtentReports GetExtent(){

		if (extent != null) { return extent;} 
           extent = new ExtentReports();
    //	Additional information that makes our report looks nice
		extent.setSystemInfo("Host Name", "Novenos IT Solutions Inc.");
		extent.setSystemInfo("Environment", "My Mini-framework");
		extent.setSystemInfo("Version", "V-1.0.0");
		extent.setSystemInfo("OS", "Windows-10");
		extent.attachReporter(getHtmlReporter());
		return extent;
	}
	
	private static ExtentHtmlReporter getHtmlReporter() {	

		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + useFileData("htmlReportPath") + testReport); 
        	htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK); // Theme background - dark, standard
		htmlReporter.config().setReportName("Automation Test Report");
		htmlReporter.setAppendExisting(true); 
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP); // chart location - top, bottom
		htmlReporter.config().setTimeStampFormat("mm/dd/yyyy hh:mm:ss a"); // set timeStamp format
        	htmlReporter.config().setDocumentTitle("Demo automation report");
    		htmlReporter.loadXMLConfig(useFileData("extentConfigXml"));
        return htmlReporter;
	}

	// Initializing and insert config.properties file with its value(s) at run time.
	public static String useFileData(String configFileData) {
		
		return ConfigurationDataSource.OR.getProperty(configFileData);
	}

	/**	...	 Excel reader for data-driven	...	*/
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

		String excelLocation = ResourceHelper.getResourcePath("testDataFilePath") + excelName;
		excel = new ReadDataFromExcelSheet();
		return excel.getExcelDataBasedOnStartingPoint(excelLocation, excelSheetName, testName);


	public void updateResult(String excelLocation, String excelSheetName, String testCaseName, String testStatus)
			throws IOException {

		excel = new ReadDataFromExcelSheet();
		excel.updateResult(excelLocation, excelSheetName, testCaseName, testStatus);
	}

	public Object[][] dataParsing(String[][] data, int col) {

		excel = new ReadDataFromExcelSheet();
		return excel.parseData(data, col);
	}

	@SuppressWarnings("deprecation")
	public void selectBrowser(String browserType) {

		System.out.println(System.getProperty("os.name"));
		if (System.getProperty("os.name").contains("Windows")) {
			if (browserType.equalsIgnoreCase("chrome")) {
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
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				/**
				 * To launch headless browser * //
				 * options.addArguments("headless");
				 */
				log("Launching " + browserType + " browser");
				driver = new ChromeDriver(options);
				driver.manage().deleteAllCookies();
				driver.manage().window().maximize();
			} else if (browserType.equalsIgnoreCase("firefox")) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(useFileData("firefoxDriver"), useFileData("winFirefoxDriverPath"));
				FirefoxProfile myProfile = new FirefoxProfile();
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

				/**
				 * capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
				 * true); capabilities.setCapability(FirefoxDriver.PROFILE,
				 * myProfile); DesiredCapabilities.firefox();
				 */

				/**
				 * DesiredCapabilities capabilities =
				 * DesiredCapabilities.firefox();
				 * capabilities.setCapability("marionette", true); WebDriver
				 * driver = new MarionetteDriver(capabilities); // OR WebDriver
				 * driver = new RemoteWebDriver(capabilities);
				 */
				driver = new FirefoxDriver(capabilities);
				myProfile.setAcceptUntrustedCertificates(true);
				myProfile.setAssumeUntrustedCertificateIssuer(true);

				driver.manage().window().maximize();
			} else if (browserType.equalsIgnoreCase("InternetExplorer")) {
				try {
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.setProperty(useFileData("ieDriver"), useFileData("winInternetExplorerDriverPath"));
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
					driver.manage().window().maximize();
				} else if (browserType.equalsIgnoreCase("firefox")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.setProperty(useFileData("firefoxDriver"), useFileData("winFirefoxDriverPath"));
					FirefoxProfile myProfile = new FirefoxProfile();
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

					/**
					 * capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
					 * true); capabilities.setCapability(FirefoxDriver.PROFILE,
					 * myProfile); DesiredCapabilities.firefox();
					 */

					/**
					 * DesiredCapabilities capabilities =
					 * DesiredCapabilities.firefox();
					 * capabilities.setCapability("marionette", true); WebDriver
					 * driver = new MarionetteDriver(capabilities); // OR
					 * WebDriver driver = new RemoteWebDriver(capabilities);
					 */
					driver = new FirefoxDriver(capabilities);
					myProfile.setAcceptUntrustedCertificates(true);
					myProfile.setAssumeUntrustedCertificateIssuer(true);
					driver.manage().window().maximize();
				} else if (browserType.equalsIgnoreCase("chrome")) {
					try {
						Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
					} catch (IOException e) {
						e.printStackTrace();
					}
					ChromeOptions options = new ChromeOptions();
					options.addArguments("disable-infobars");
					options.addArguments("--disable-extensions");
					options.addArguments("--start-maximized");
					/**
					 * To launch headless browser * //
					 * options.addArguments("headless");
					 */
					log("Launching " + browserType + " browser");
					driver = new ChromeDriver(options);
					driver.manage().deleteAllCookies();
					driver.manage().window().maximize();
				}
			}
		}
	}	
}

	
	public String takeScreenShotOnFailure(String result) {

		if (result == ""){result = "blank";}
		File destFile = null;
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		destFile = new File(useFileData("failedTestScreenShotPath") + "/failedTestScreenshots/" 
					+ result + "_" + DateTimeHelper.getCurrentAndDateTime() + ".png");
		try {
			FileUtils.copyFile(source, destFile);
			System.out.println("Screenshot taken");
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
		}
		return destFile.toString();
	}

	public String takeScreenShot(String result) {

		if (result == ""){result = "blank";}
		File destFile = null;
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ useFileData("screenShotPath");
			destFile = new File((String) reportDirectory + "/" + result + "_" + DateTimeHelper.getCurrentAndDateTime() + ".png");
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

		selectBrowser(useFileData("browser"));
		driver.navigate().to(useFileData("url"));
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		log("Page loaded!");
	}

	public void launchTestResult() {

		selectBrowser(useFileData("browser"));
	//	getUrl(useFileData("htmlReportPath") + testReport);
		driver.navigate().to((System.getProperty("user.dir") + useFileData("htmlReportPath") + testReport));	
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		log("Page loaded!");
	}

	public static void changeLogLevel(Level level) {
	    Enumeration<?> loggers = LogManager.getCurrentLoggers();
	    while(loggers.hasMoreElements()) {
	        Logger logger = (Logger) loggers.nextElement();
	        logger.setLevel(level);
	    }
	}
	
	// 	To be moved to the Enum folder of Master package
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
}
