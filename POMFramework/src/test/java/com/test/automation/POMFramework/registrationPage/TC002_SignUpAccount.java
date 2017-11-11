package com.test.automation.POMFramework.registrationPage;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageActions.HomePage;

public class TC002_SignUpAccount extends TestBase{
	
	public static final Logger log = Logger.getLogger(TC002_SignUpAccount.class.getName());
	HomePage homepage;
	
//	String emailAddress = System.currentTimeMillis() + "@gmail.com";	// To generate uniquely different email address to run test case

	@Test
	public void createAccount() {
		homepage = new HomePage(driver);
		homepage.accountCreationRequirementEmail("danov" + System.currentTimeMillis() + "@gmail.com");
		homepage.singUpToCreateAccount("baba", "Sala","danov@gmail.com", "kuluso", "Lekan", "Aboki", "72 New Aburi Street", "Florissant", 2, "63031", 1, "3144440987", "My address");
		Assert.assertEquals(true, homepage.getRegistrationStatus());
	}
	
	@AfterTest(alwaysRun=true)
	public void tearDown() {
		closeBrowser();	
	}
}
