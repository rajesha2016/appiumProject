package com.vodafone.start.pageobjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vodafone.start.api.MethodFactory;
import com.vodafone.start.utilities.ElementFactory;

/**
 * page object of Login page of Start application
 * @author Manan
 *
 */
public class LoginPage {

	private WebDriver driver;
	private MethodFactory factory;
	private static Logger log = Logger.getLogger(LoginPage.class);
	
	/**
	 * constructor for Login Page
	 * @param _driver
	 */
	public LoginPage(WebDriver _driver){
		
		this.driver = _driver;
		factory = new MethodFactory(_driver);
	}
	
	
	/**
	 * set input parameter into password field of start login page
	 * @param _password text to set in password field
	 */
	public void setPassword(String _password){
		
		
		WebElement pwdfield = factory.findElement(ElementFactory.PASSWORD);
		
		if(null == pwdfield){
			
			log.info("couldn't find Password field on login page by initial run."
					+ " Waiting for it till timeout");
			
			factory.waitForVisibilityOfElement(pwdfield);
		}
	
		pwdfield.clear();
		pwdfield.sendKeys(_password);
		
	}
	
	/**
	 * clicks on login button
	 * @return object of main page
	 */
	public MainPage clickLoginButton(){
		
		WebElement loginButton = factory.findElement(ElementFactory.LOGIN);
		

		if(null == loginButton){
			
			log.info("couldn't find Login Button on login page by initial run."
					+ " Waiting for it till timeout");
			factory.waitForVisibilityOfElement(loginButton);
		}
		
		loginButton.click();
		
		return new MainPage(factory.getDriver());
		
	}
	
	/**
	 * clicks on show password checkbox
	 */
	public void clickShowPasswordCheckbox(){
		
		WebElement showPwdBox = factory.findElement(ElementFactory.SHOW_PASSWORD);
		

		if(null == showPwdBox){
			
			log.info("couldn't find Show Password checkbox on login page by initial run."
					+ " Waiting for it till timeout");
			factory.waitForVisibilityOfElement(showPwdBox);
		}
		
		showPwdBox.click();
		
	}
	
	/**
	 * set input parameter into confirm password field of start login page
	 * @param _password text to set in password field
	 */
	public void setConfirmPassword(String _password){
		
		
		WebElement pwdfield = factory.findElement(ElementFactory.CONFIRM_PASSWORD);
		
		if(null == pwdfield){
			
			log.info("couldn't find Password field on login page by initial run."
					+ " Waiting for it till timeout");
			
			factory.waitForVisibilityOfElement(pwdfield);
		}
	
		pwdfield.clear();
		pwdfield.sendKeys(_password);
		
	}
	
}
