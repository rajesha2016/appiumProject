package com.appium.start.exp;

import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appium.start.api.Assertion;
import com.appium.start.api.IdmAccessor;
import com.appium.start.api.MethodFactory;
import com.appium.start.api.MongoDBAccessor;
import com.appium.start.main.MainTest;
import com.appium.start.pageobjects.LoginPage;
import com.appium.start.pageobjects.MainPage;
import com.appium.start.pageobjects.WelcomePage;
import com.appium.start.utilities.ElementFactory;
import com.appium.start.utilities.ErrorMessages;
import com.appium.start.utilities.ExcelReader;
import com.appium.start.utilities.Keywords;
import com.appium.start.utilities.StartExceptionHandler;
import com.appium.start.utilities.TestObject;
import com.appium.start.utilities.Utility;

public class Script2 {

	private static Logger log = Logger.getLogger(Script2.class);
	private MainTest main = new MainTest();
	private MongoDBAccessor mongod = new MongoDBAccessor("tools.bc.sp.vodafone.com",
			"SspUserRepositoryPre1", "mongod", "sspmongo");
	private AppiumDriver driver;
	final String NL_URL= "https://pre.start.vodafone.com/bepre1/ssp/main/nl";
	MethodFactory test;
	WelcomePage welcome;
	LoginPage login;
	MainPage mainPage;
	Utility util;
	public ExcelReader excel = new ExcelReader();
	public TestObject data;
	public LinkedList<String> dataList;
	private final String TESTDATA_ROW = "Script2";
	private int failCounter=1;
	List<String> parameters;



	@Test
	@Parameters(Keywords.DEVICE_NAME)
	public void validateFirstTimeUserTutorialEnabled(String deviceName) 	{

		do{
			try{

				this.setPreconditions(deviceName);


				log.info(" go to pre-setup url ");
				test.open(dataList.get(0));

				log.info("check that page is loaded in 5 seconds");
				test.pageLoadTimeout(Integer.parseInt(dataList.get(1)));

				log.info("check that user is valid, if not check if user is redirected to redirect url");

				if(!Boolean.valueOf(dataList.get(3))){

					log.info("User is not a valid. Going for redirection.");

					log.info("check that user is navigated to redirect page");
					welcome.verifyRedirectURL(dataList.get(4));
				}


				log.info("click on NEXT button ");
				login = welcome.clickNextButton();


				log.info("check if login is implicit");
				if(!Boolean.valueOf(parameters.get(1))){

					Thread.sleep(4000);

					log.info("input password");	
					login.setPassword(dataList.get(6));
					
					log.info("confirm password");
					login.setConfirmPassword(dataList.get(6));

					log.info("click login button");
				mainPage = login.clickLoginButton();
				}
				else{
					
					mainPage = new MainPage(driver);
				}

				log.info(" take screenshot ");
				util.takeScreenShot(this.getClass().getSimpleName());
				
				Thread.sleep(3000);

				log.info("check is tutorial is enabled");
				if(Boolean.valueOf(dataList.get(8))){

					util.takeScreenShot(this.getClass().getSimpleName()+ "-TutorialPageExpected");

					log.info(" close the tutorial");
					test.click(ElementFactory.TUTORIAL_CLOSE);


				}


				log.info("verify welcome text");
				mainPage.verifyWelcomeText(parameters.get(2));
				util.takeScreenShot(this.getClass().getSimpleName() + "-WelcomeTextIsCorrect");

				
				log.info("click on download icon");
				mainPage.clickDownloadIcon();

				log.info("verify navogated URL");
				mainPage.verifyCurrentURL(dataList.get(7), this.getClass().getSimpleName());

				util.takeScreenShot(this.getClass().getSimpleName()+ "-NavigatedToPlayStore");

				log.info("navigate back to main page");
				test.previous();

				Thread.sleep(5000);


				util.takeScreenShot(this.getClass().getSimpleName()+ "-ToolTipNotDisplayedAgain");


				util.alertTestEnding(TESTDATA_ROW);

				log.info("close browser");
				test.close();

				failCounter+=3;
				
			}catch(Exception e){

				failCounter++;

				if(failCounter>2){

					log.error("An Exception occurred after re-running this script. Failing this test script!!");
					StartExceptionHandler.handle(e, driver, 
							this.getClass().getSimpleName(),Utility.getMethodName());
				}
				else{
					log.error("An Exception occured for first time during this execution. Re-running this script!!");
					test.getDriver().quit();
				}
			}

		}while(failCounter==2);

	}


	public void setPreconditions(String deviceName) throws Exception{


		//TODO set wifi off
		//driver.executeScript("mobile:toggleWiFi");

		parameters = new Utility(null).getTestParameters(deviceName);

		log.info("getting appium session from server");
		driver= main.getAppiumSession(parameters.get(0));


		log.info("injecting driver session in functions factory and utilities");
		test = new MethodFactory(driver);
		welcome = new WelcomePage(driver);
		util = new Utility(driver);



		log.info("alert test beginning");
		util.alertTestBeginning(TESTDATA_ROW);

		log.info("Reading test config data from sheet");

		
		log.info("Accessing IDM");

		log.info("remove registered msisdn from IDM");
		IdmAccessor.deleteMsisdn(Long.parseLong(parameters.get(2)));

		log.info("remove user, imei and profile from MongoDB");
		mongod.deleteAll(Long.parseLong(parameters.get(2)));




		log.info("reading data from test data excel sheet");
		data = excel.getTestData(TESTDATA_ROW,parameters.get(3));
		dataList = data.getData();


		log.info("Preconditions satisfied");




	}


}
