package com.test.automation.POMFramework.registrationPage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.homepage.TC005_AccountCreationConfirmationPage;
import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageFunctions.homePageFunction;
import com.test.automation.POMFramework.uiPageObjects.HomePageObjects;

public class TC002_SignUpAccount extends TestBase{
	
	public static final Logger log = Logger.getLogger(TC002_SignUpAccount.class.getName());
	homePageFunction homepage;
	HomePageObjects hmObj = new HomePageObjects();
	private static WebDriver driver;
	
public TC002_SignUpAccount(WebDriver driver) {
	super();
	this.driver = driver;
	PageFactory.initElements(driver, this);
	waitForElement(hmObj.authentication, 10);
	}
		
//	String emailAddress = System.currentTimeMillis() + "@gmail.com";	// To generate uniquely different email address to run test case

	@BeforeMethod
	public void startTest() {
		launchApplication();
	}
	
	@Test
	public void createNewUserAccount() {
		homepage = new homePageFunction(driver);
		homepage.accountCreationRequirementEmail("danov" + System.currentTimeMillis() + "@gmail.com");
		homepage.createNewAccount("baba", "Sala","danov@gmail.com", "kuluso", "Lekan", "Aboki", "72 New Aburi Street", "Florissant", 2, "63031", 1, "3144440987", "My address");
		Assert.assertEquals(true, homepage.getRegistrationStatus());
	}
	
//	@AfterTest(alwaysRun=true)
	public void tearDown() {
		closeBrowser();	
	}
}
