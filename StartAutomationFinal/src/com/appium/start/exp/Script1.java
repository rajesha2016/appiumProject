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
import com.appium.start.pageobjects.WelcomePage;
import com.appium.start.utilities.ElementFactory;
import com.appium.start.utilities.ErrorMessages;
import com.appium.start.utilities.ExcelReader;
import com.appium.start.utilities.Keywords;
import com.appium.start.utilities.StartExceptionHandler;
import com.appium.start.utilities.TestObject;
import com.appium.start.utilities.Utility;

public class Script1 {

	private static Logger log = Logger.getLogger(Script1.class);
	private MainTest main = new MainTest();
	private MongoDBAccessor mongod = new MongoDBAccessor("tools.bc.sp.vodafone.com",
			"SspUserRepositoryPre1", "mongod", "sspmongo");
	private AppiumDriver driver;
	final String NL_URL= "https://pre.start.vodafone.com/bepre1/ssp/main/nl";
	MethodFactory test;
	Utility util;
	WelcomePage welcome;
	public ExcelReader excel = new ExcelReader();
	public TestObject data;
	public LinkedList<String> dataList;
	List<String> parameters;
	private final String TESTDATA_SHEET = "Smoke";
	private final String TESTDATA_ROW = "Script1";
	private final String TESTCONFIG_SHEET = "setup";
	private int failCounter=1;


	@Test
	@Parameters(Keywords.DEVICE_NAME)
	public void validateWelcomePageWithSkipButton(String testparameters) 	{
		do{
			try{

				this.setPreconditions(testparameters);

				log.info(" go to pre-setup url ");
				test.open(dataList.get(0));

				log.info("check that page is loaded in 5 seconds");
				test.pageLoadTimeout(Integer.parseInt(dataList.get(1)));

				log.info("check that user is valid, if not check if user is redirected to redirect url ");

				if(Boolean.valueOf(dataList.get(3))){

					log.info("User is valid. Going to Welcome Page");


					log.info("check that skip button is present");
					welcome.verifySkipButtonIsPresent();

					log.info("check that next button is present");
					welcome.verifyNextButtonIsPresent();
				}
				else{

					log.info("User is not a valid. Going for redirection.");

					log.info("check that user is navigated to redirect page");
					welcome.verifyRedirectURL(dataList.get(4));
				}

				log.info(" take screenshot ");
				util.takeScreenShot(this.getClass().getSimpleName());

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

	public void setPreconditions(String testparameters) throws Exception{


		//TODO set wifi off
		//driver.executeScript("mobile:toggleWiFi");
		parameters = new Utility(null).getTestParameters(testparameters);

		log.info("getting appium session from server");
		driver= main.getAppiumSession(parameters.get(0));


		log.info("injecting driver session in functions factory and utilities");
		test = new MethodFactory(driver);
		welcome = new WelcomePage(driver);
		util = new Utility(driver);



		log.info("alert test beginning");
		util.alertTestBeginning(TESTDATA_ROW);

		log.info("Reading test config data from sheet");

		log.info("reading data from test configuration excel sheet");
		data = excel.getTestConfig(TESTCONFIG_SHEET);
		

		log.info("Accessing IDM");

		log.info("remove registered msisdn from IDM");
		IdmAccessor.deleteMsisdn(Long.parseLong(parameters.get(2)));

		log.info("remove user, imei and profile from MongoDB");
		mongod.deleteAll(Long.parseLong(parameters.get(2)));



		log.info("Reading test data from sheet");

		log.info("reading data from test data excel sheet");
		data = excel.getTestData(TESTDATA_ROW,parameters.get(3));
		dataList = data.getData();


		log.info("Preconditions satisfied");

	}


}
