package com.test.automation.POMFramework.homepage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.registrationPage.TC002_SignUpAccount;
import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageFunctions.homePageFunction;
import com.test.automation.POMFramework.uiPageObjects.HomePageObjects;

public class TC003_VeryLogInWithDifferentData extends TestBase{
	
	public static final Logger log = Logger.getLogger(TC002_SignUpAccount.class.getName());
	HomePageObjects hmObj;
	homePageFunction hpFunction;
	
	public TC003_VeryLogInWithDifferentData(WebDriver driver) {
		super();
		this.driver = driver;
		PageFactory.initElements(driver, this);
		waitForElement(hmObj.authentication, 10);
		}
	
	@Test (dataProvider = "loginData")
	public void testLogIn(String emailAddress ,String LoginPassword, String runMode) {
		
		if(runMode.equalsIgnoreCase("n")) {
			 throw new SkipException("User mark this record as No-Run");
		}
		
		hpFunction = new homePageFunction(driver);
		log("=== Creating instance of Homepage");
		
		hpFunction  = new homePageFunction(driver);
		hpFunction.LoginToApplication(emailAddress, LoginPassword);	
	//	Assert.assertEquals(hpFunction.getInvalidLoginText(), "Authentication failed.");
		log("== Finished VeryLogInWithDifferentData Test ==");
        
        //	homepage.ClickOnlogOut();
        //	log.info("==== Signing out of the account ====");}
        
   //     return new C004_WelcomeHomePage(driver);
	}
		
		
	@DataProvider(name="loginData")
	public String[][] getTestData() {
		String[][] testRecord = getDataFromSpreadsheet("LoginTestData.xlsx", "LogInData");
		return testRecord;
	}
	
		
//	@AfterTest
	public void tearDown(){
		closeBrowser();
}
}