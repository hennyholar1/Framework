package com.test.automation.POMFramework.uiPageObjects;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePageObjects {

	@FindBy  (xpath =".//*[@id='header']/div[2]/div/div/nav/div[1]/a")
	WebElement singIn;
		
	@FindBy  (xpath = ".//*[@id='email']")  //(xpath= "#txtUsername") 
	WebElement loginEmail;
	
	@FindBy (xpath= ".//*[@id='passwd']")  //(xpath = "#txtPassword") //
	private	WebElement loginPassword;
	
	@FindBy (xpath = ".//*[@id='SubmitLogin']")  //(xpath = "#btnLogin")  // 
	private	WebElement submitButton;
	
	@FindBy(xpath=".//*[@id='center_column']/div[1]/ol/li")
	public WebElement authentication;
	
	
	//SignUP page locators
	@FindBy (xpath=".//*[@id='email_create']")
	private	WebElement signUpEmail;
	
	@FindBy (xpath=".//*[@id='SubmitCreate']")
	private	WebElement createAccountButton;
	
	@FindBy(id ="id_gender1")
	protected WebElement mrRadioButton;
	
	@FindBy (xpath=".//*[@id='customer_firstname']")
	private	WebElement firstName;
	
	@FindBy (xpath =".//*[@id='customer_lastname']")
	private	WebElement lastName;
	
	@FindBy (xpath = ".//*[@id='email']")
	private	WebElement email;
	
	@FindBy (xpath=".//*[@id='passwd']")
	private	WebElement passWord;
	
	@FindBy (xpath =".//*[@id='days']")
	protected WebElement selectDay;
	
	@FindBy (xpath =".//*[@id='months']")
	protected WebElement selectMonth;
	
	
	@FindBy (xpath =".//*[@id='years']")
	protected WebElement selectYear;
		
	@FindBy (xpath=".//*[@id='firstname']")
	protected WebElement firstNameOnAddress;
	
	@FindBy (xpath=".//*[@id='lastname']")
	protected WebElement lastNameOnAddress;
	
	@FindBy (xpath=".//*[@id='company']")
	protected WebElement  companyNameOnAddress;
	
	@FindBy (xpath=".//*[@id='address1']")
	protected WebElement userAddress1;
	
	@FindBy (xpath =".//*[@id='address2']")
	protected WebElement optionalUserAddress2;
		
	@FindBy (xpath =".//*[@id='city']")
	protected WebElement city;
	
	@FindBy (xpath =".//*[@id='id_state']")
	protected WebElement state;
	
	@FindBy (xpath =".//*[@id='postcode']")
	protected WebElement zipCode;
		
	@FindBy (xpath=".//*[@id='id_country']")
	protected WebElement Country;
	
	@FindBy (xpath=".//*[@id='other']")
	protected WebElement additionalInformation;
	
	@FindBy (xpath=".//*[@id='phone']")
	protected WebElement homePhone;
	
	@FindBy (xpath=".//*[@id='phone_mobile']")
	protected WebElement mobilePhone;
	
	@FindBy (xpath =".//*[@id='alias']")
	protected WebElement addressAlias;
	
	@FindBy (xpath=".//*[@id='submitAccount']")
	protected WebElement registerButton;
	
	@FindBy (xpath=".//*[@id='header']/div[2]/div/div/nav/div[2]/a")
	private	WebElement logOut;

	
	public WebElement getFirstName() {
		return firstName;
	}

	public void setFirstName(WebElement firstName) {
		this.firstName = firstName;
	}

	public WebElement getLastName() {
		return lastName;
	}

	public void setLastName(WebElement lastName) {
		this.lastName = lastName;
	}

	public WebElement getEmail() {
		return email;
	}

	public void setEmail(WebElement email) {
		this.email = email;
	}

	public WebElement getPassWord() {
		return passWord;
	}

	public void setPassWord(WebElement passWord) {
		this.passWord = passWord;
	}

	public WebElement getFirstNameOnAddress() {
		return firstNameOnAddress;
	}

	public void setFirstNameOnAddress(WebElement firstNameOnAddress) {
		this.firstNameOnAddress = firstNameOnAddress;
	}

	public WebElement getLastNameOnAddress() {
		return lastNameOnAddress;
	}

	public void setLastNameOnAddress(WebElement lastNameOnAddress) {
		this.lastNameOnAddress = lastNameOnAddress;
	}
	
	public WebElement getSingIn() {
		return singIn;
	}

	public void setSingIn(WebElement singIn) {
		this.singIn = singIn;
	}
	
	public WebElement getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(WebElement loginEmail) {
		this.loginEmail = loginEmail;
	}

	public WebElement getSignUpEmail() {
		return signUpEmail;
	}

	public void setSignUpEmail(WebElement signUpEmail) {
		this.signUpEmail = signUpEmail;
	}

	public WebElement getCreateAccountButton() {
		return createAccountButton;
	}

	public void setCreateAccountButton(WebElement createAccountButton) {
		this.createAccountButton = createAccountButton;
	}

	public WebElement getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(WebElement loginPassword) {
		this.loginPassword = loginPassword;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(WebElement submitButton) {
		this.submitButton = submitButton;
	}

	public WebElement getLogOut() {
		return logOut;
	}

	public void setLogOut(WebElement logOut) {
		this.logOut = logOut;
	}

}

