package com.appium.start.pageobjects;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appium.start.api.Assertion;
import com.appium.start.api.MethodFactory;
import com.appium.start.utilities.ConfigurationReader;
import com.appium.start.utilities.ElementFactory;
import com.appium.start.utilities.ErrorMessages;

/**
 * page object for the welcome page of Start application
 * @author Manan
 *
 */
public class WelcomePage {

	private WebDriver driver;
	private MethodFactory factory;
	private static Logger log = Logger.getLogger(WelcomePage.class);
	
	
	/**
	 * constructor for the welcome page
	 * @param _driver
	 */
	public WelcomePage(WebDriver _driver) {
		
		this.driver = _driver;
		factory = new MethodFactory(_driver);
	}

	/**
	 * clicks next button on welcome page
	 */
	public LoginPage clickNextButton(){
		
		WebElement nextButton = factory.findElement(ElementFactory.WELCOME_NEXT_BUTTON);
		

		if(null == nextButton){
			
			log.info("couldn't find next button on main page for initial run."
					+ " Waiting for it till timeout");
			factory.waitForVisibilityOfElement(nextButton);
		}
		
		nextButton.click();
		
		return new LoginPage(factory.getDriver());
	}
	
	/**
	 * verify that next button is present on welcome page
	 */
	public void verifyNextButtonIsPresent(){
		
		factory.verifyElementPresent(ElementFactory.WELCOME_NEXT_BUTTON);
	}
	
	
	/**
	 * verify that skip button is present on welcome page
	 */
	public void verifySkipButtonIsPresent(){
		
		factory.verifyElementPresent(ElementFactory.WELCOME_SKIP_BUTTON);
	}
	
	/**
	 * verifies that skip button is not displayed
	 */
	public void verifySkipButtonIsNotPresent(){
		
		factory.verifyElementNotPresent(ElementFactory.WELCOME_SKIP_BUTTON,
				Integer.parseInt(ConfigurationReader.get("elementNotPresentTimeout")));
	}
	
	/**
	 * verifies that user is navigated to the expected URL
	 * @param expectedURL
	 */
	public void verifyRedirectURL(String expectedURL){
		
		Assertion.assertTrue(factory.getDriver(), factory.getDriver().getCurrentUrl().
				contains(expectedURL), ErrorMessages.INVALID_REDIRECT
				+ expectedURL + ErrorMessages.CURRENT_URL + 
				factory.getDriver().getCurrentUrl(), "RedirectURL" ); 
	}
	

	/**
	 * verifies the impressum footer displayed in welcome page
	 */
	private void verifyImpressumFooterIsPresent(){
		
		factory.verifyTextPresent("Impressum" ,
				Integer.parseInt(ConfigurationReader.get("waitTimeout")));
	}
	
	/**
	 * clicks on impressum link text
	 */
	private void clickImpressumLink(){
		
		factory.click(ElementFactory.IMPRESSUM_LINK_TEXT);
	}
	
	
	/**
	 * verifies that user is navigated to the expected URL
	 * @param expectedURL
	 */
	public void verifyCurrentURL(String expectedURL, String name){
		
		Assertion.assertTrue(factory.getDriver(),
				factory.getDriver().getCurrentUrl().contains(expectedURL)
				, "User is navigated to " + factory.getDriver().getCurrentUrl()+ "instead of "
						+ expectedURL, name);
	}
	
	/**
	 * checks if impressum link is present and verifies the url
	 * @param impressumURL
	 */
	public void checkImpressumforDE(String impressumURL){
		
		log.info("verify Impressum footer is present in welcome page");
		verifyImpressumFooterIsPresent();

		log.info("click on Impressum link");
		clickImpressumLink();

		String currentTab = driver.getWindowHandle();

		Set<String> allTabs =  driver.getWindowHandles();

		for(String tab : allTabs){

			if(!tab.equalsIgnoreCase(currentTab)){

				log.info("switch to impressum tab");
				driver.switchTo().window(tab);

				log.info("verify impressum URL");
				verifyCurrentURL(impressumURL, this.getClass().getSimpleName());

				log.info("closing impressum tab");
				driver.close();
				
				log.info("switch back to welcome page");
				driver.switchTo().window(currentTab);

				break;
			}
		}
		
	}
}
