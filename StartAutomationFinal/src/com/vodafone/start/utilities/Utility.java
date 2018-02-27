package com.vodafone.start.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.vodafone.start.api.Assertion;
import com.vodafone.start.api.CustomExpectedConditions;






public class Utility {


	private static Logger log=Logger.getLogger(Utility.class);
	private WebDriver driver;
	private static final String TASKLIST = "tasklist";


	public Utility(WebDriver driver){

		this.driver = driver;
	}

	/**
	 * method highlights the element which is being selected.
	 * 
	 * @param element to be highlighted
	 */
	public void highlightElement(WebElement element) {

		try{


			for (int i = 0; i < 2; i++) {

				JavascriptExecutor js = (JavascriptExecutor) this.driver;

				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "color: blue; border: 3px solid yellow;");
				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						element, "");

			}
		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

	}

	/**
	 * alerts the beginning of test script
	 * 
	 * @param testname
	 */
	public void alertTestBeginning(String testName) {

		try{

			JavascriptExecutor js = (JavascriptExecutor) this.driver;

			js.executeScript(
					"alert(\"Starting - "+ testName +" \");");

			Thread.sleep(1000);

			Alert alert = driver.switchTo().alert();

			alert.accept();


		}catch(Exception e){

			log.error("An error Occurred while executing javascript for alerting test failure."
					+ " \r\nError message is - "+ e.toString());

		}

	}

	/**
	 * alerts the user about ending of test script.
	 * 
	 * @param testName
	 */
	public void alertTestEnding(String testName) {

		try{
			JavascriptExecutor js = (JavascriptExecutor) this.driver;

			js.executeScript(
					"alert(\"Passed - "+ testName +" \");");

			Thread.sleep(1000);

			Alert alert = driver.switchTo().alert();

			alert.accept();


		}catch(Exception e){

			log.error("An error Occurred while executing javascript for alerting test failure."
					+ " \r\nError message is - "+ e.toString());

		}

	}
	/**
	 * alerts the user about fail message of test script.
	 * 
	 * @param testName
	 */
	public void alertTestFail(String failMessage) {

		try{

			JavascriptExecutor js = (JavascriptExecutor) this.driver;

			js.executeScript(
					"alert(\"Failed - "+ failMessage +" \");");

			Thread.sleep(1000);

			Alert alert = driver.switchTo().alert();

			alert.accept();


		}catch(Exception e){

			log.error("An error Occurred while executing javascript for alerting test failure."
					+ " \r\nError message is - "+ e.toString());

		}

	}

	/**
	 * takes screenshot of the present screen
	 * @param name of the saved screenshot
	 * @param driver for the current session
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void takeScreenShot(String name) throws Exception{

			Thread.sleep(1000);
			File scrnshot;

			scrnshot = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);


			FileUtils.copyFile(scrnshot,
					new File(PathReader.readPath("pageScreenshot") + name
							+ ".png"));
			log.debug("Screenshot taken and stored as : " + name
					+ ".png in C:\\seleniumPOC\\screenshots folder");

		

	}

	/**
	 * takes screenshot of the present screen
	 * @param name of the saved screenshot
	 * @param driver for the current session
	 */
	public void takeFailedScreenShot(String name) {

		try {
			Thread.sleep(1000);
			File scrnshot;


			scrnshot = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);


			FileUtils.copyFile(scrnshot,
					new File(PathReader.readPath("failScreenshot") + "Failed-"+name
							+ ".png"));
			log.info("Failed Screenshot taken and stored as : " + name
					+ ".png in " + PathReader.readPath("failScreenshot"));

		} catch (Exception e) {

			//TODO
		}

	}

	/**
	 * scrolls till the given element is visible
	 * @param driver
	 * @param element
	 */
	public void scroll(WebDriver driver, WebElement element){

		try{

			((JavascriptExecutor) this.driver).executeScript("arguments[0].scrollIntoView(true);", element);

		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
	}


	public void login(String username,String password){

		try{

			WebElement msisdn = this.driver.findElement(By.id("username"));

			msisdn.sendKeys(username);

			WebElement _password = this.driver.findElement(By.id("password"));

			_password.sendKeys(password);

			WebElement login = this.driver.findElement(By.id("login"));

			login.click();

		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

	}

	public WebElement waitForPresenceOfElement(final By locator) {

		WebElement elem = null;

		try{

			final long startTime = System.currentTimeMillis();
			Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
					.withTimeout(30, TimeUnit.SECONDS)
					.pollingEvery(5, TimeUnit.SECONDS)
					.ignoring(StaleElementReferenceException.class,
							ElementNotVisibleException.class);
			int tries = 0;
			boolean found = false;
			while ((System.currentTimeMillis() - startTime) < 91000) {
				log.info("Searching for element. Try number " + (tries++));
				try {
					elem = wait.until(ExpectedConditions
							.presenceOfElementLocated(locator));
					this.highlightElement(elem);
					found = true;
					break;
				} catch (StaleElementReferenceException e) {
					log.info("Stale element: \n" + e.getMessage() + "\n");
				}
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			if (found) {
				log.info("Found element after waiting for " + totalTime
						+ " milliseconds.");
			} else {
				log.info("Failed to find element after " + totalTime
						+ " milliseconds.");
			}
		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
		return elem;
	}

	public WebElement waitForVisibilityOfElement(final WebElement element) {

		WebElement elem = null;

		try{

			final long startTime = System.currentTimeMillis();
			Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
					.withTimeout(30, TimeUnit.SECONDS)
					.pollingEvery(5, TimeUnit.SECONDS)
					.ignoring(StaleElementReferenceException.class,
							ElementNotVisibleException.class);
			int tries = 0;
			boolean found = false;

			while ((System.currentTimeMillis() - startTime) < 91000) {
				log.info("Searching for element. Try number " + (tries++));
				try {
					elem = wait.until(ExpectedConditions.visibilityOf(element));
					this.highlightElement(elem);
					found = true;
					break;
				} catch (StaleElementReferenceException e) {
					log.info("Stale element: \n" + e.getMessage() + "\n");
				}
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			if (found) {
				log.info("Found element after waiting for " + totalTime
						+ " milliseconds.");
			} else {
				log.info("Failed to find element after " + totalTime
						+ " milliseconds.");
			}
		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
		return elem;
	}


	public boolean waitForTextPresent(final String expectedText, 
			final int timeoutInMilliSeconds) {

		final long startTime = System.currentTimeMillis();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class,
						ElementNotVisibleException.class);
		int tries = 0;
		boolean found = false;
		while ((System.currentTimeMillis() - startTime) < timeoutInMilliSeconds) {
			log.info("Searching for element. Try number " + (tries++));
			try {
				found = wait.until(CustomExpectedConditions.textToBePresentInPage(expectedText));
				break;
			} catch (StaleElementReferenceException e) {
				log.info("Stale element: \n" + e.getMessage() + "\n");
			}
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		if (found) {
			log.info("Found expected text - "+ expectedText+" after waiting for " + totalTime
					+ " milliseconds.");
		} else {
			log.info("Failed to find expected text - "+ expectedText+" after " + totalTime
					+ " milliseconds.");
		}
		return found;
	}

	/**
	 * gets the name of current method 
	 * @return current method name
	 */
	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	} 

	public void checkAlertText(String textValue) throws InterruptedException {

		int j=0;

		do{
			try{
				
				Thread.sleep(1000);
				
				Alert alert = driver.switchTo().alert();

				if(null != alert){
					
				String alertText = alert.getText();
				
				log.info("Alert Text -"+alertText);
				
				alert.accept();
				
				Assertion.assertTrue(driver, alertText.equalsIgnoreCase(textValue),
						ErrorMessages.ALERT_TEXT_EXPECTED + textValue + 
						ErrorMessages.ALERT_TEXT_ACTUAL +alertText , "AlertTextIsNotAsExpected");

				break;
				}
				
			}catch(NoAlertPresentException n){
				log.info("Alert was not found for "+ j +" time");
				j++;
				
				if(j==5){
					
					Assertion.fail(driver, ErrorMessages.NO_ALERT_FOUND, this.getClass().getSimpleName());
				}
			}
		}while(j<5);
	}

	public void checkAlertNotPresent(String textValue) throws InterruptedException {

		String alertText= "No Alert Text Found";

		try{

			Thread.sleep(1000);


			Alert alert = driver.switchTo().alert();
			alertText = alert.getText();
			if(null != alert){

				alert.accept();
				log.error(ErrorMessages.UNEXPECTED_ALERT +". Alert text is ("+ alertText +")");
				Assertion.fail(driver, ErrorMessages.UNEXPECTED_ALERT, this.getClass().getSimpleName());
			}

		}catch(NoAlertPresentException n){

			log.info("Didn't find alert box which is acoording to the expectations.");

		}

	}

	/**
	 * kills the current session of the browser
	 * @throws IOException 
	 * @throws Exception
	 */
	public void killBrowser() {

		try {
			Runtime.getRuntime().exec("cmd /c start .\\Resources\\killBrowser.bat");

		} catch (IOException e) {

			log.error("Error occurred while hard closing the current session of browser");
			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

	}

	/**
	 * checks if chromedriver.exe is running as process
	 * @return true if process is running else false
	 */
	public boolean isProcessRunning() {
		try{	
			Process p = Runtime.getRuntime().exec(TASKLIST);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {

				System.out.println(line);
				if (line.contains("chromedriver.exe")) {
					return true;
				}
			}

		} catch (IOException e) {

			log.error("Error occurred while checking the running processes");
			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
		return false;

	}


	/**
	 * splits test parameters from comma and returns list of test parameters
	 * @param testParameters string formatted and unsplitted test parameters
	 * @return list of test parameters
	 */
	public List<String> getTestParameters(String testParameters){
		
		List<String> parameters = new LinkedList<String>();
		
		if(testParameters.contains(",")){

			log.info("received test parameters as - " + testParameters);
			parameters = Arrays.asList(testParameters.split(","));

		}
		else{
			
			log.error("Incorrect format of test parameters found - " + testParameters );
		}

		return parameters;
	}
}
