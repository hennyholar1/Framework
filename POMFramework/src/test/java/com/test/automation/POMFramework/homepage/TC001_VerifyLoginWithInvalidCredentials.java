package com.test.automation.POMFramework.homepage;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
//	import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageActions.HomePage;

public class TC001_VerifyLoginWithInvalidCredentials //extends TestBase{
	{
	public static final Logger log = Logger.getLogger(TC001_VerifyLoginWithInvalidCredentials.class.getName());
	HomePage homepage;
	
//	@BeforeMethod
	public void startTest() {
		setUp();
	}
	
	
//	@Test (priority = 1)
	public void verifyLoginWithInvalidCredentials() {
		log.info("==Starting Test ==");
		homepage = new HomePage(driver);
		homepage.LoginToApplication("danovenos@gmail.com", "admin");
		Assert.assertEquals(homepage.getInvalidLoginText(), "Authentication failed.");
		log.info("== Finished verifyLoginWithInvalidCredentials Test ==");
	}

	//@Test(priority = 2)
	public void LoginWithInvalidCredentials() {
		log.info("==Starting Test ==");
		
		homepage = new HomePage(driver);
		homepage.accountCreationRequirementEmail("danov" + System.currentTimeMillis() + "@gmail.com");
		//homepage.singUpToCreateAccount("baba", "Sala","danov@gmail.com", "kuluso", "Lekan", "Aboki", "72 New Aburi Street", "Florissant", 2, "63031", 1, "3144440987", "My address");
		
		waitForVisibilityOfElement(driver.findElement(By.xpath(".//*[@id='days']")), 5);
		selectDropdowCssId("days", "19");
		selectDropdowXpathId("months", "November");
		selectDropdowXpathId("years", "1980");
	//	locateTextboxAndEnterValue("search","Hello World, this is a generic locator method");
	//	locateTextCaptionedElement("Cart").click();
	//	locateElementById("query_top").sendKeys("Hello World, this is a generic locator method");
	
	//	Assert.assertEquals(homepage.getInvalidLoginText(), "Authentication failed. ");
		log.info("== Finished verifyLoginWithInvalidCredentials Test ==");
	}
	
	@Test
	public void testGit() {
		System.out.println("Test 1");
	}
//	@AfterMethod(alwaysRun=true)
	public void tearDown() {
		closeBrowser();	
		
		System.out.println("Test completed");
		
	}
	
}

