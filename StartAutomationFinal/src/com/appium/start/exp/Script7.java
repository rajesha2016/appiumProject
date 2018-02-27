package com.appium.start.exp;

import io.appium.java_client.AppiumDriver;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appium.start.api.IdmAccessor;
import com.appium.start.api.MethodFactory;
import com.appium.start.api.MongoDBAccessor;
import com.appium.start.main.MainTest;
import com.appium.start.pageobjects.LoginPage;
import com.appium.start.pageobjects.MainPage;
import com.appium.start.pageobjects.WelcomePage;
import com.appium.start.utilities.ExcelReader;
import com.appium.start.utilities.Keywords;
import com.appium.start.utilities.StartExceptionHandler;
import com.appium.start.utilities.TestObject;
import com.appium.start.utilities.Utility;

public class Script7 {

	private static Logger log = Logger.getLogger(Script7.class);
	private MainTest main = new MainTest();
	private MongoDBAccessor mongod = new MongoDBAccessor("tools.bc.sp.vodafone.com",
			"SspUserRepositoryPre1", "mongod", "sspmongo");
	private AppiumDriver driver;
	final String NL_URL= "https://pre.start.vodafone.com/bepre1/ssp/main/nl";
	MethodFactory test;
	MainPage mainPage;
	private WelcomePage welcome;
	private LoginPage login;
	Utility util;
	public ExcelReader excel = new ExcelReader();
	public TestObject data,dashboards;
	public LinkedList<String> dataList,dashboardList;
	private final String TESTDATA_ROW = "Script7";
	private int failCounter = 1;
	List<String> parameters;



	@Test
	@Parameters(Keywords.DEVICE_NAME)
	public void validateCTADisabled(String deviceName) 	{

		do{
			try{

				this.setPreconditions(deviceName);

				log.info(" go to pre-setup url ");
				test.open(dataList.get(0));
				
/*******************************************************/
				
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
				
				/*************************************/
				
				log.info("verify welcome text");
				mainPage.verifyWelcomeText(parameters.get(2));
				util.takeScreenShot(this.getClass().getSimpleName() + "-WelcomeTextIsCorrect");

				log.info("go to given dashboard");
				mainPage.clickDashboardHeader(dataList.get(10));
				
				log.info("verify the alert is not present");
				util.checkAlertNotPresent(parameters.get(5));

				log.info("verify user is navigated to the expected url");
				mainPage.verifyCurrentURL(dataList.get(11),this.getClass().getSimpleName());


				log.info("take screenshot");
				util.takeScreenShot(this.getClass().getSimpleName() + "-mainPageCTAdisabled");

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
