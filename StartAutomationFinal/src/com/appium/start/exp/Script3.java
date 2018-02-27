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
import com.appium.start.pageobjects.MainPage;
import com.appium.start.pageobjects.WelcomePage;
import com.appium.start.utilities.ElementFactory;
import com.appium.start.utilities.ErrorMessages;
import com.appium.start.utilities.ExcelReader;
import com.appium.start.utilities.Keywords;
import com.appium.start.utilities.StartExceptionHandler;
import com.appium.start.utilities.TestObject;
import com.appium.start.utilities.Utility;

public class Script3 {

	private static Logger log = Logger.getLogger(Script3.class);
	private MainTest main = new MainTest();
	private MongoDBAccessor mongod = new MongoDBAccessor("tools.bc.sp.vodafone.com",
			"SspUserRepositoryPre1", "mongod", "sspmongo");
	private AppiumDriver driver;
	final String NL_URL= "https://pre.start.vodafone.com/bepre1/ssp/main/nl";
	MethodFactory test;
	Utility util;
	public ExcelReader excel = new ExcelReader();
	public TestObject data;
	MainPage mainPage;
	public LinkedList<String> dataList;
	private final String TESTDATA_ROW = "Script3";
	private final String TESTCONFIG_SHEET = "setup";
	private int failCounter = 1;
	List<String> parameters;



	@Test
	@Parameters(Keywords.DEVICE_NAME)
	public void validateExisitingUser(String deviceName) 	{

		do{
			try{

				this.setPreconditions(deviceName);


				log.info(" go to pre-setup url ");
				test.open(dataList.get(0));

				log.info("check that page is loaded in 5 seconds");
				test.pageLoadTimeout(Integer.parseInt(dataList.get(1)));


				log.info(" verify tutorial page is not present ");
				mainPage.verifyTutorialIsNotPresent();

				log.info(" take screenshot ");
				util.takeScreenShot(this.getClass().getSimpleName() + "-NoTutorialPage");


				log.info("verify welcome text");
				mainPage.verifyWelcomeText(parameters.get(2));
				util.takeScreenShot(this.getClass().getSimpleName() + "-WelcomeTextIsCorrect");

				log.info("click on download icon");
				mainPage.clickDownloadIcon();

				log.info("verify navigated URL");
				mainPage.verifyCurrentURL(dataList.get(7), this.getClass().getSimpleName());
				

				log.info(" take screenshot ");
				util.takeScreenShot(this.getClass().getSimpleName() + "-NavigatedToPlayStore");


				log.info("navigate back to main page");
				mainPage.previous();

				Thread.sleep(5000);


				log.info(" take screenshot ");
				util.takeScreenShot(this.getClass().getSimpleName() + "-NoToolTipPresent");

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




	public void setPreconditions(String deviceName){


		//TODO set wifi off
		//driver.executeScript("mobile:toggleWiFi");


		parameters = new Utility(null).getTestParameters(deviceName);

		log.info("getting appium session from server");
		driver= main.getAppiumSession(parameters.get(0));


		log.info("injecting driver session in functions factory and utilities");
		test = new MethodFactory(driver);
		util = new Utility(driver);
		mainPage = new MainPage(driver);



		log.info("alert test beginning");
		util.alertTestBeginning(TESTDATA_ROW);

		log.info("Reading test config data from sheet");

		log.info("reading data from test configuration excel sheet");
		data = excel.getTestConfig(TESTCONFIG_SHEET);
		

		log.info("reading data from test data excel sheet");
		data = excel.getTestData(TESTDATA_ROW,parameters.get(3));
		dataList = data.getData();


		log.info("Preconditions satisfied");

	}



}
