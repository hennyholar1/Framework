package com.test.automation.POMFramework.homepage;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
//	import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.test.automation.POMFramework.testBase.TestBase;
import com.test.automation.POMFramework.uiPageFunctions.homePageFunction;
import com.test.automation.POMFramework.uiPageObjects.HomePageObjects;

public class TC001_SimpleLogInTest extends TestBase{
	
	public static final Logger log = Logger.getLogger(TC001_SimpleLogInTest.class.getName());
	homePageFunction hpFunction;
	HomePageObjects hmObj = new HomePageObjects();
	private WebDriver driver;
	
	

	@BeforeMethod
	public void startTest() {
		launchApplication();
	}

	//  Note: This could be done with data driven method using TestNG data provider
	@Test (priority = 1)
	public void verifyLoginWithValidCredentials() {
		log.info("== Starting Test ==");
		hpFunction  = new homePageFunction(driver);
		hpFunction.LoginToApplication("danovenos@gmail.com", "admin");	
		Assert.assertEquals(hpFunction.getInvalidLoginText(), "Authentication failed.");
		log.info("== Finished verifyLoginWithInvalidCredentials Test ==");
    //  return new C004_WelcomeHomePage(driver);
	}

//	@Test(priority = 2)
	public void accountCreation() {
		log.info("== Starting Test ==");
		
		hpFunction  = new homePageFunction(driver);
		hpFunction.accountCreationRequirementEmail("danov" + System.currentTimeMillis() + "@gmail.com");
		hpFunction.createNewAccount("baba", "Sala","danov@gmail.com", "kuluso", "Lekan", "Aboki", "72 New Aburi Street", "Florissant", 2, "63031", 1, "3144440987", "My address");
		
	/*	waitForElement(driver.findElement(By.xpath(".//*[@id='days']")), 5);
		DropDownHelper.selectDropdownValue("days", "19");
		DropDownHelper.selectDropdownValue("months", "November");
		DropDownHelper.selectDropdownValue("years", "1980");
	//	locateTextboxAndEnterValue("search","Hello World, this is a generic locator method");
	//	locateTextCaptionedElement("Cart").click();
	//	locateElementById("query_top").sendKeys("Hello World, this is a generic locator method");
	
	//	Assert.assertEquals(homepage.getInvalidLoginText(), "Authentication failed. ");
		*/
		log.info("== Finished verifyLoginWithInvalidCredentials Test ==");
    
	}
	
	
	@AfterMethod(alwaysRun=true)
	public void tearDown() {
		closeBrowser();	
		
		System.out.println("Test completed");
		
	}
	
}

