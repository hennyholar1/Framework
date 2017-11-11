package com.test.automation.POMFramework.uiPageActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class HomePage {

	public static final Logger log = Logger.getLogger(HomePage.class.getName());
	WebDriver driver;

	@FindBy(xpath =".//*[@id='header']/div[2]/div/div/nav/div[1]/a")
	WebElement singIn;
	
	@FindBy(xpath = ".//*[@id='email']")
	WebElement loginEmail;
	
	@FindBy (xpath= ".//*[@id='passwd']")
	WebElement loginPassword;
	
	@FindBy (xpath = ".//*[@id='SubmitLogin']") 
	WebElement submitButton;
	
	@FindBy(xpath=".//*[@id='center_column']/div[1]/ol/li")
	WebElement authentication;
	
	
	//SignUP page locators
	@FindBy (xpath=".//*[@id='email_create']")
	WebElement signUpEmail;
	
	@FindBy (xpath=".//*[@id='SubmitCreate']")
	WebElement createAccountButton;
	
	@FindBy(id ="id_gender1")
	WebElement mrRadioButton;
	
	@FindBy (xpath=".//*[@id='customer_firstname']")
	WebElement firstName;
	
	@FindBy (xpath =".//*[@id='customer_lastname']")
	WebElement lastName;
	
	@FindBy (xpath = ".//*[@id='email']")
	WebElement email;
	
	@FindBy (xpath=".//*[@id='passwd']")
	WebElement passWord;
	
	@FindBy (xpath =".//*[@id='days']")
	WebElement selectDay;
	
	@FindBy (xpath =".//*[@id='months']")
	WebElement selectMonth;
	
	
	@FindBy (xpath =".//*[@id='years']")
	WebElement selectYear;
		
	@FindBy (xpath=".//*[@id='firstname']")
	WebElement firstNameOnAddress;
	
	@FindBy (xpath=".//*[@id='lastname']")
	WebElement lastNameOnAddress;
	
	@FindBy (xpath=".//*[@id='company']")
	WebElement companyNameOnAddress;
	
	@FindBy (xpath=".//*[@id='address1']")
	WebElement userAddress1;
	
	@FindBy (xpath =".//*[@id='address2']")
	WebElement optionalUserAddress2;
	
	
	@FindBy (xpath =".//*[@id='city']")
	WebElement city;
	
	@FindBy (xpath =".//*[@id='id_state']")
	WebElement state;
	
	
	@FindBy (xpath =".//*[@id='postcode']")
	WebElement zipCode;
		
	@FindBy (xpath=".//*[@id='id_country']")
	WebElement Country;
	
	@FindBy (xpath=".//*[@id='other']")
	WebElement additionalInformation;
	
	@FindBy (xpath=".//*[@id='phone']")
	WebElement homePhone;
	
	@FindBy (xpath=".//*[@id='phone_mobile']")
	WebElement mobilePhone;
	
	@FindBy (xpath =".//*[@id='alias']")
	WebElement addressAlias;
	
	@FindBy (xpath=".//*[@id='submitAccount']")
	WebElement registerButton;
	
	@FindBy (xpath=".//*[@id='header']/div[2]/div/div/nav/div[2]/a")
	WebElement logOut;
	
	
	public HomePage(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	
	public void singUpToCreateAccount(String firstname, String lastName, String emailAddress, String password, String addressFirstName, String addressLastName, 
										String address, String city, int state, String zipcode, int country, String cellphone, String addressalias) 
	{
		log.info("Filling the fields in create account page with all required/mandatory fields ");
		
		mrRadioButton.click();
		
		firstName.clear();	firstName.sendKeys(firstname);
		
		this.lastName.clear();		this.lastName.sendKeys(lastName);
		
		email.clear();		email.sendKeys(emailAddress);
		
		passWord.clear();		passWord.sendKeys(password);
		
		firstNameOnAddress.clear();		firstNameOnAddress.sendKeys(addressFirstName);
		
		lastNameOnAddress.clear();		lastNameOnAddress.sendKeys(addressLastName);
		
		userAddress1.clear();		userAddress1.sendKeys(address);
		
		this.city.clear();		this.city.sendKeys(city);
		
		log.info("Selecting value from drop down list");
		Select selectState = new Select(this.state);
		selectState.selectByIndex(state);
		
				
		zipCode.clear();		zipCode.sendKeys(zipcode);
		
		
		log.info("Selecting value from drop down list");
		Select selectCountry = new Select(Country);
		selectCountry.selectByIndex(country);
		
		
		mobilePhone.clear();		mobilePhone.sendKeys(cellphone);
		
		addressAlias.clear();		addressAlias.sendKeys(emailAddress);
		
		registerButton.click();
		
		log.info("submitting application by clicking on " + registerButton.toString());	
	}
	
	
	public void accountCreationRequirementEmail( String emailAddress) 
	{
		singIn.click();
		log.info("Clicking on "+ singIn.toString());
	
		
	//	driver.switchTo().frame(".//*[@id='header']/div[2]/div/div/nav/div[1]/a");
	//	waitForElement(signUpEmail, 10);
		log.info("Clearing textbox");
	//	signUpEmail.clear();
	//	driver.findElement(By.xpath(".//*[@id='email_create']")).clear();
	//	driver.findElement(By.xpath(".//*[@id='email_create']")).sendKeys(emailAddress);
		log.info("Entering the email address for create account field");
		signUpEmail.sendKeys(emailAddress);
		
		
		createAccountButton.click();
		log.info("Submitting the email address to be used for account creation");
	}
	
		
	public void LoginToApplication(String emailAddress, String password) {
		singIn.click();
		log.info("Clicking on "+ singIn.toString());
		
		loginEmail.clear();
		loginEmail.sendKeys(emailAddress);
		log.info("Entering user email in the " + loginEmail.toString());
		
		loginPassword.clear();
		loginPassword.sendKeys(password);
		log.info("Entering user password in the " + loginPassword.toString());
		
		submitButton.click();
		log.info("Clicking on "+ submitButton.toString());
	}
	
	
	public void ClickOnlogOut() {
		logOut.click();
		log.info("Clicking on "+ logOut.toString());
	}
	
	public String getInvalidLoginText() {
		log.info("Error message is: "+ authentication.getText());
		return authentication.getText();
	}
	
	public boolean getRegistrationStatus() {
		try {
			log.info("verifying account creation success");
			driver.findElement(By.className("navigation_page")).isDisplayed();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
			
		}
	}
}

