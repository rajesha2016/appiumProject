package com.vodafone.start.exp;

import io.appium.java_client.AppiumDriver;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.vodafone.start.api.IdmAccessor;
import com.vodafone.start.api.MethodFactory;
import com.vodafone.start.api.MongoDBAccessor;
import com.vodafone.start.main.MainTest;
import com.vodafone.start.pageobjects.LoginPage;
import com.vodafone.start.pageobjects.MainPage;
import com.vodafone.start.pageobjects.WelcomePage;
import com.vodafone.start.utilities.ElementFactory;
import com.vodafone.start.utilities.ExcelReader;
import com.vodafone.start.utilities.Keywords;
import com.vodafone.start.utilities.StartExceptionHandler;
import com.vodafone.start.utilities.TestObject;
import com.vodafone.start.utilities.Utility;

public class Script20 {

	private static Logger log = Logger.getLogger(Script19.class);
	private MainTest main = new MainTest();
	private MongoDBAccessor mongod = new MongoDBAccessor("tools.bc.sp.vodafone.com",
			"SspUserRepositoryPre1", "mongod", "sspmongo");
	private AppiumDriver driver;
	final String NL_URL= "https://pre.start.vodafone.com/bepre1/ssp/main/nl";
	MethodFactory test;
	Utility util;
	public ExcelReader excel = new ExcelReader();
	public TestObject data,dashboards;
	MainPage mainPage;
	WelcomePage welcome;
	LoginPage login;
	public LinkedList<String> dataList,dashboardList;
	private final String TESTDATA_ROW = "Script20";
	private final String TESTCONFIG_SHEET = "setup";
	private final String DASHBOARDS = "Dashboards";
	private int failCounter = 1;
	List<String> parameters;

	@Test
	@Parameters(Keywords.DEVICE_NAME)
	public void validateDashboardsInAppForRegisteredUser(String deviceName) 	{

		do{
			try{

				this.setPreconditions(deviceName);

				log.info(" go to pre-setup url ");
				test.open(dataList.get(0));

				log.info("check that user is valid, if not check if user is redirected to redirect url ");

				if(!Boolean.valueOf(dataList.get(3))){

					log.info("User is not a valid. Going for redirection.");

					log.info("check that user is navigated to redirect page");
					welcome.verifyRedirectURL(dataList.get(4));
				}


				log.info("take screenshot");
				util.takeScreenShot(this.getClass().getSimpleName());

				log.info("verify that tutorial is not displayed");
				mainPage.verifyTutorialIsNotPresent();
				
				log.info("take screenshot");
				util.takeScreenShot(this.getClass().getSimpleName()+ "-TutorialPageNotExpected");
			

				log.info("verify welcome textis not present");
				mainPage.verifyWelcomeText(parameters.get(2));
				util.takeScreenShot(this.getClass().getSimpleName() + "-WelcomeTextIsCorrect");

				log.info("click on download icon");
				mainPage.clickDownloadIcon();

				log.info("verify navigated URL");
				mainPage.verifyCurrentURL(dataList.get(7), this.getClass().getSimpleName());

				util.takeScreenShot(this.getClass().getSimpleName() + "-navigatedToPlaystore");


				log.info("navigate back to main page");
				test.previous();
				

				log.info("waiting for page to load for 15 seconds");
				Thread.sleep(30000);
				
				log.info("verify tutorial is not displayed");
				mainPage.verifyTutorialIsNotPresent();
				
				log.info("verify that dashboards are present accordingly");
				mainPage.checkDashboards(dashboardList);
				
				util.takeScreenShot(this.getClass().getSimpleName() + "-dashboards");

				
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

		try{
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
			dashboards = excel.getDashboardData(DASHBOARDS);
			dashboardList = dashboards.getData();


			log.info("Preconditions satisfied");


		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

	}


}
