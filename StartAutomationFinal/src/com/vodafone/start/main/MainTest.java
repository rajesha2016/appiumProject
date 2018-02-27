package com.vodafone.start.main;


import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.vodafone.start.utilities.ExcelReader;
import com.vodafone.start.utilities.FileManager;
import com.vodafone.start.utilities.Keywords;
import com.vodafone.start.utilities.LogConfig;
import com.vodafone.start.utilities.PathReader;
import com.vodafone.start.utilities.TestObject;
import com.vodafone.start.utilities.TestSuiteGenerator;


public class MainTest {

	private static Logger log = Logger.getLogger(MainTest.class);
	AppiumDriver dr;
	private ExcelReader reader = new ExcelReader();


	public AppiumDriver getAppiumSession(String deviceName) {



		TestObject deviceData = reader.getDeviceConfig(deviceName);
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability(CapabilityType.BROWSER_NAME, "");
		cap.setCapability("deviceName", deviceData.getData().get(0));
		cap.setCapability("platformName", deviceData.getData().get(1));
		cap.setCapability("platformVersion", deviceData.getData().get(2));
		cap.setCapability("app",deviceData.getData().get(3));

		try {
			dr = new AppiumDriver(new URL("http://127.0.0.1:4722/wd/hub"), cap);
		}catch (UnreachableBrowserException u) {

			log.error("An exception occurred. Please check if Appium Server is up and running."
					+ " \r\n Error Message is - " + u.toString());

			u.printStackTrace();
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return dr;

	}	


	public static void main(String[] args) throws IOException{

		try{
			/** setting logger **/
			LogConfig.setLogger();

			log.info("Loaded logger configuration");

			/**setting http/https proxies**/
			System.getProperties().put("java.net.preferIPv4Stack", "true");
			System.getProperties().put("https.proxyHost", "195.233.25.20");
			System.getProperties().put("https.proxyPort", "8080");
			System.getProperties().put("https.nonProxyHosts", "localhost|127.0.0.1");
			System.getProperties().put("http.proxyHost", "195.233.25.20");
			System.getProperties().put("http.proxyPort", "8080");
			System.getProperties().put("http.nonProxyHosts", "localhost|127.0.0.1");

			log.info("http proxies set");

			log.info("reading test setup data");
			HashMap<String,LinkedList<String>> setupData = new ExcelReader().getTestSetupData();

			LinkedList<String> suites = setupData.get(Keywords.TEST_SUITE_NAME);
			LinkedList<String> inclusion = setupData.get(Keywords.INCLUDE);
			LinkedList<String> device = setupData.get(Keywords.DEVICE_NAME);
			LinkedList<String> scripts = setupData.get(Keywords.TEST_SUITE);
			LinkedList<String> implicitLogin = setupData.get(Keywords.IMPLICIT_LOGIN);

			LinkedList<LinkedList<String>> params = new LinkedList<LinkedList<String>>();

			params.add(setupData.get(Keywords.TEST_SUITE_NAME));
			params.add(setupData.get(Keywords.INCLUDE));
			params.add(setupData.get(Keywords.DEVICE_NAME));
			params.add(setupData.get(Keywords.TEST_SUITE));
			params.add(setupData.get(Keywords.IMPLICIT_LOGIN));
			params.add(setupData.get(Keywords.MSISDN));
			params.add(setupData.get(Keywords.TEST_DATA_SHEET));
			params.add(setupData.get(Keywords.CTA_TIMES));
			params.add(setupData.get(Keywords.CTA_TEXT));

			FileManager fileManager = new FileManager();

			int suiteCount = 0;
			for(int i=0; i<inclusion.size();i++){

				if(inclusion.get(i).equalsIgnoreCase(Keywords.YES)){

					suiteCount++;

					log.info("deleting previous test reports");
					fileManager.deleteTestReports(new File(PathReader.readPath("reportSrc")));


					log.info("Starting test suite - " + suites.get(i) + " on device - " + device.get(i));
					new TestSuiteGenerator().createTestSuite(i,params,"src//com//vodafone//start//exp");

					log.info("Copying input test data into reports");
					fileManager.copyTestData(PathReader.readPath("inputDir"), PathReader.readPath("reportTestData"));

					String suiteName=fileManager.generateFileName(suites.get(i));

					log.info("Renaming test-reports folder to - "+ PathReader.readPath("reportDest") + suiteName);
					fileManager.copyTestData(PathReader.readPath("reportSrc"), PathReader.readPath("reportDest") + suiteName);

					//					File directoryToZip = new File(PathReader.readPath("reportSrc"));
					//
					//					List<File> fileList = new ArrayList<File>();
					//					log.info("Getting references to all files in: " + directoryToZip.getCanonicalPath());
					//					ZipDirectory.getAllFiles(directoryToZip, fileList);
					//					log.info("Creating zip file - " + suiteName);
					//					ZipDirectory.writeZipFile(directoryToZip, fileList,suiteName);
					//
					//					log.info("Test reports zipped successfully");
				}

			}


			if(suiteCount == 0){

				log.error("No test suite selected. Please select atleast "
						+ "one test suite for test execution");

			}

		}catch(Exception e){

			log.error("An error occurred. Error Message is - " + e.toString());
			throw new AssertionError();
		}
	}



}
