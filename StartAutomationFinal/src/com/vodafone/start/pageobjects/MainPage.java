package com.vodafone.start.pageobjects;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vodafone.start.api.Assertion;
import com.vodafone.start.api.MethodFactory;
import com.vodafone.start.utilities.ConfigurationReader;
import com.vodafone.start.utilities.ElementFactory;
import com.vodafone.start.utilities.StartExceptionHandler;
import com.vodafone.start.utilities.Utility;

/**
 * page object for the main page of start application
 * @author Manan
 *
 */
public class MainPage {

	private WebDriver driver;
	private MethodFactory factory;
	private static Logger log = Logger.getLogger(MainPage.class);
	
	
	/**
	 * constructor for the main page
	 * @param _driver
	 */
	public MainPage(WebDriver _driver) {
		
		this.driver = _driver;
		factory = new MethodFactory(_driver);
	}

	/**
	 * clicks the download icon on main page
	 */
	public void clickDownloadIcon(){
		
		WebElement downloadIcon = factory.findElement(ElementFactory.PLAY_STORE);
		

		if(null == downloadIcon){
			
			log.info("couldn't find download icon on main page for initial run."
					+ " Waiting for it till timeout");
			factory.waitForVisibilityOfElement(downloadIcon);
		}
		
		downloadIcon.click();
	}
	
	/**
	 * verifies the welcome text displayed on main page
	 * @param msisdn part of welcome text
	 */
	public void verifyWelcomeText(String msisdn){
		
		factory.verifyTextPresent(msisdn,
				Integer.parseInt(ConfigurationReader.get("waitTimeout")));
	}
	
	
	/**
	 * verifies the welcome text displayed on main page
	 * @param msisdn part of welcome text
	 */
	public void verifyWelcomeTextIsNotPresent(String msisdn){
		
		factory.verifyTextNotPresent("+" + msisdn);
	}
	
	/**
	 * verifies the welcome text displayed on main page
	 * @param text to be verified
	 */
	public void verifyExtraWelcomeTextIsPresent(String text){
		
		factory.verifyTextPresentInElement(ElementFactory.EXTRA_WELCOME_TEXT_IT,text);
	}
	
	/**
	 * verifies the welcome text displayed on main page
	 */
	public void verifyExtraWelcomeTextIsNotPresent(){
		
		factory.verifyElementNotPresent(ElementFactory.EXTRA_WELCOME_TEXT_IT,
				Integer.parseInt(ConfigurationReader.get("waitTimeout")));
	}
	
	/**
	 * navigates back to previous URL
	 */
	public void previous(){
		
		factory.previous();
	}
	
	/**
	 * compares the actual main page dashboards with given expected dashboards
	 * @param expectedDashboards from the test data sheet
	 */
	public void checkDashboards(LinkedList<String> expectedDashboards){

		try{

			List<WebElement> actualDashboards = factory.findElements
					(ElementFactory.DASHBOARD_TITLE);


			if(actualDashboards.size() == expectedDashboards.size()){


				for (int i=0; i<actualDashboards.size(); i++){

					log.info(i + " :" + actualDashboards.get(i).getText()
							+ " - " + expectedDashboards.get(i) );
					Assertion.assertEquals(factory.getDriver(),actualDashboards.get(i).getText(),
							expectedDashboards.get(i),"Services are not in expected order.","Script 4");

				}

			}
			else{

				Assertion.fail(factory.getDriver(),"Expected " + expectedDashboards.size() + 
						" services, But actually found " + actualDashboards.size() +
						" services",this.getClass().getSimpleName());
			}
		}catch(Exception e){

			StartExceptionHandler.handle(e, factory.getDriver(), 
					this.getClass().getSimpleName(),Utility.getMethodName());
		}

	}

	/**
	 * clicks on specific dashboard header
	 * @param dashboardTitle title of dashboard to click
	 */
	public void clickDashboardHeader(String dashboardTitle){

		try{
			String xpath = "//*[contains(text(),'"+dashboardTitle+"')]";

			int j=1;
			
			do{
		try{
			WebElement elem = factory.getDriver().findElement(By.xpath(xpath));
			
			if(null != elem){
				
				elem.click();
				break;
			}
		}catch(NoSuchElementException n){
			
			log.info("Couldn't find "+dashboardTitle+" for "+ j+" time.");
			j++;
		}
			}while(j<6);

		}catch(Exception e){

			StartExceptionHandler.handle(e, factory.getDriver(), 
					this.getClass().getSimpleName(),Utility.getMethodName());
		}
	}
	
	/**
	 * verifies that user is navigated to the expected URL
	 * @param expectedURL
	 */
	public void verifyCurrentURL(String expectedURL,String classname){
		
		Assertion.assertTrue(factory.getDriver(),
				factory.getDriver().getCurrentUrl().contains(expectedURL)
				, "User is navigated to " + factory.getDriver().getCurrentUrl()+ "instead of "
						+ expectedURL,classname);
	}
	
	/**
	 * verifies that tutorial is not displayed
	 */
	public void verifyTutorialIsNotPresent(){
		
		factory.verifyTextNotPresent("onclick=\"TutorialNew.closeTutorial()\" style=\"display: none;\">");
	}
	
	/**
	 * verifies the impressum footer displayed in welcome page
	 */
	public void verifyImpressumFooterIsPresent(){
		
		factory.verifyTextPresent("Impressum" ,
				Integer.parseInt(ConfigurationReader.get("waitTimeout")));
	}
	
	/**
	 * clicks on impressum link text
	 */
	public void clickImpressumLink(){
		
		factory.click(ElementFactory.IMPRESSUM_LINK_TEXT);
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
				verifyCurrentURL(impressumURL,"impressmForDE");

				log.info("closing impressum tab");
				driver.close();
				
				log.info("switch back to welcome page");
				driver.switchTo().window(currentTab);

				break;
			}
		}
		
	}
	
	/**
	 * verifies  if call button is present in current page
	 */
	public void verifyDiallerExists(){
		
		factory.verifyElementPresent(ElementFactory.NATIVE_DIALLER);
		
	}
	
	/**
	 * verifies if SMS text area is present in current page
	 */
	public void verifySMSTextboxExists(){
		
		factory.verifyElementPresent(ElementFactory.NATIVE_SMS);
	}
}
