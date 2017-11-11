package com.test.automation.POMFramework.homepage;

import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.registrationPage.TC002_SignUpAccount;
import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageActions.HomePage;

public class TC003_VeryLoginWithDifferentData extends TestBase{
	
	public static final Logger log = Logger.getLogger(TC002_SignUpAccount.class.getName());
	
	HomePage homepage;
	
	@Test (dataProvider = "loginData")
	public void testLogin(String emailAddress ,String LoginPassword, String runMode) {
		
		if(runMode.equalsIgnoreCase("n")) {
			 throw new SkipException("User mark this record as No-Run");
		}
		
		homepage = new HomePage(driver);
		log.info("=== Creating instance of Homepage");
		
		homepage.LoginToApplication(emailAddress, LoginPassword);
		log.info("==== Entering user email, password and clicking on signIn button ====");
		
		homepage.ClickOnlogOut();
		log.info("==== Signing out of the account ====");
	}

	
	@DataProvider(name="loginData")
	public String[][] getTestData() {
		String[][] testRecord = getData("LoginTestData.xlsx", "LogInData");
		return testRecord;
	}
	
		
	@AfterTest
	public void tearDown(){
		closeBrowser();
}
}