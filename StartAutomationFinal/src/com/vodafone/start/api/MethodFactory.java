package com.vodafone.start.api;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.log4testng.Logger;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.vodafone.start.utilities.ElementFactory;
import com.vodafone.start.utilities.StartExceptionHandler;
import com.vodafone.start.utilities.Utility;

public class MethodFactory {

	private static Logger log = Logger.getLogger(MethodFactory.class);
	private WebDriver driver;

	public MethodFactory(WebDriver driver){

		this.driver = driver;
	}


	/**
	 * returns the current instance of driver
	 * @return driver
	 */ 
	public WebDriver getDriver(){

		return this.driver;
	}

	/**
	 * navigates on the previous page
	 */
	public void previous(){

		this.driver.navigate().back();
	}
	
	/**
	 * reloads the current page
	 */
	public void refresh(){
		
		this.driver.navigate().refresh();
	}

	/**
	 * finds the element from the given locator
	 * @param factory containing locators for elements
	 * @return element found
	 * @throws  
	 */
	public WebElement findElement(ElementFactory factory) {

		String by = factory.getLocator();
		String locator = factory.getValue();
		WebElement element = null;
		int j=1;

		do{
			try{

				if(by.equalsIgnoreCase("id")){

					element = driver.findElement(By.id(locator));
					break;

				}else if(by.equalsIgnoreCase("xpath")){

					element = driver.findElement(By.xpath(locator));
					break;

				}else if(by.equalsIgnoreCase("class")){

					element = driver.findElement(By.className(locator));
					break;

				}else if(by.equalsIgnoreCase("name")){

					element = driver.findElement(By.name(locator));
					break;

				}else if(by.equalsIgnoreCase("css")){

					element = driver.findElement(By.cssSelector(locator));
					break;

				}else if(by.equalsIgnoreCase("link")){

					element = driver.findElement(By.partialLinkText(locator));
					break;

				}
				else{

					Assert.fail("Identifier : "+ by+ " is not supported. Please use "
							+ "id, xpath, css, name, link text or class.");
					break;
				}
			}catch(TimeoutException e){

				log.info("Timeout Exception occured for " + j+ " time");
				j++;


			}catch(NoSuchElementException n){

				log.info("NoSuchException occured for " + j+ " time");
				j++;


			}catch(Exception e){

				StartExceptionHandler.handle(e, driver, 
						this.getClass().getSimpleName(),Utility.getMethodName());	
			}

		}while(j<=5);
		return element;
	}

	/**
	 * finds the element from the given locator
	 * @param by locator to find element
	 * @param locator value of the locator
	 * @return list of found elements
	 */
	public List<WebElement> findElements(ElementFactory factory){

		String by = factory.getLocator();
		String locator = factory.getValue();

		List<WebElement> elements = null;
		int j=1;

		do{
		try{

			if(by.equalsIgnoreCase("id")){

				elements = driver.findElements(By.id(locator));
				break;

			}else if(by.equalsIgnoreCase("xpath")){

				elements = driver.findElements(By.xpath(locator));
				break;

			}else if(by.equalsIgnoreCase("class")){

				elements = driver.findElements(By.className(locator));
				break;

			}else if(by.equalsIgnoreCase("name")){

				elements = driver.findElements(By.name(locator));
				break;

			}else if(by.equalsIgnoreCase("css")){

				elements = driver.findElements(By.cssSelector(locator));
				break;

			}else if(by.equalsIgnoreCase("link")){

				elements = driver.findElements(By.partialLinkText(locator));
				break;

			}
			else{

				Assert.fail("Identifier : "+ by+ " is not supported. Please use "
						+ "id, xpath, css, name, link text or class.");
			}
			
		}catch(TimeoutException e){

			log.info("Timeout Exception occured for " + j+ " time");
			j++;


		}catch(NoSuchElementException n){

			log.info("Timeout Exception occured for " + j+ " time");
			j++;
			
		
		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
		}while(j<=5);
		return elements;
	}

	
	/**
	 * generic method to click any clickable element
	 * @param driver current instance of browser
	 * @param factory for the elements
	 */
	public void click(ElementFactory factory){

			WebElement element = findElement(factory);

			this.waitForVisibilityOfElement(element);

			element.click();


		
	}


	/**
	 * generic method to send input text to a input field 
	 * @param factory containing locators for elements
	 * @param input string to pass as input
	 */
	public void write(ElementFactory factory, String input){

		try{

			WebElement element = findElement(factory);

			this.waitForVisibilityOfElement(element);

			element.clear();
			element.sendKeys(input);


		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
	}

	/**
	 * generic method to get text of an element 
	 * @param factory containing locators for elements
	 * @return default text of element
	 */
	public String getText(ElementFactory factory){

		String text = null;

		try{

			WebElement element = findElement(factory);

			this.waitForVisibilityOfElement(element);

			text = element.getText();


		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

		return text;
	}




	/**
	 * verify that the text is present in the page
	 * @param expectedText
	 */
	public void verifyTextPresent(String expectedText,int timeoutInMilliSeconds){

		

			new Utility(driver).waitForTextPresent(expectedText, timeoutInMilliSeconds);

			Assertion.assertTrue(driver, driver.getPageSource().contains(expectedText),"Expected text"
					+ " - "+expectedText+". But could not find it in the current page.", expectedText + "NotPresent");

		
	}

	/**
	 * verify that the text is not present in the page
	 * @param expectedText
	 */
	public void verifyTextNotPresent(String expectedText){

		try{

			Assertion.assertTrue(driver, !driver.getPageSource().contains(expectedText),"Didn't expected text"
					+ " - "+expectedText+". But found it in the current page.", expectedText+"TextPresent");


		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}
	}

	/**
	 * generic method to send input text to an element element
	 * @param factory containing element locator
	 * @param expectedText 
	 */
	public void verifyTextPresentInElement(ElementFactory factory, String expectedText){

		try{

			WebElement element = findElement(factory);

			this.waitForVisibilityOfElement(element);

			Assertion.assertTrue(driver, element.getText().contains(expectedText), "Expected text"
					+ " - "+expectedText+". But could not find it in the given element.",expectedText+"TextNotPresentInElement");


		}catch(Exception e){

			StartExceptionHandler.handle(e, driver, 
					this.getClass().getSimpleName(),Utility.getMethodName());	
		}

	}

	/**
	 * generic method to verify element is present in a current page
	 * @param factory containing element locator
	 */
	public void verifyElementPresent(ElementFactory factory){

	
//			this.waitForVisibilityOfElement(findElement
//					(factory));

			List<WebElement> element = findElements(factory);

			boolean isPresent = element.size() == 1;

			Assertion.assertTrue(driver, isPresent, "Expected element not found by "
					+ "locator : "+ factory.getLocator(), "ElementNotPresent");

	}

	/**
	 * generic method to verify that given element is not present
	 * @param factory containing element locator
	 * @param timeOutInMilliSeconds 
	 */
	public void verifyElementNotPresent(ElementFactory factory, int timeOutInMilliSeconds){

		try{

			List<WebElement> element=new ArrayList<WebElement>();
			int i=0;
			while(i<3){
				
				element = findElements(factory);
				
				if(element.size()>0){
					
					Assertion.fail(driver, "Expected no element but found"
							+ " "+ element.size()+"  element by "
					+ "locator : "+ factory.getLocator(),this.getClass().getSimpleName());				
				}
				Thread.sleep(3000);
				i++;
			}

			boolean isPresent = element.size() == 0;

			Assertion.assertTrue(driver, isPresent, "Expected element not found by "
					+ "locator : "+ factory.getLocator(), "ElementIsPresent");

		}catch(TimeoutException el){

			log.info("Expected exception : Element not found ");
		} catch (InterruptedException e) {
			
			log.error("Interrupted Exception occurred in thread." + e.getMessage());
		}

	}

	/**
	 * navigates to the given url
	 * @param url to be navigated to
	 */
	public void open(String url){

		driver.get(url);

	}

	/**
	 * Sets the amount of time to wait for
	 * a page load to complete before throwing an error.
	 * @param timeoutInSeconds timeout value in seconds
	 */
	public void pageLoadTimeout(int timeoutInSeconds){

			driver.manage().timeouts().pageLoadTimeout(timeoutInSeconds,
					TimeUnit.SECONDS);

	}

	/**
	 * close the browser and stop the browser process
	 */
	public void close(){

	
			if(null != driver){

				driver.quit();
				
			}
			
			
	}

	public WebElement waitForPresenceOfElement(final By locator) {

		final long startTime = System.currentTimeMillis();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class,
						ElementNotVisibleException.class);
		int tries = 0;
		boolean found = false;
		WebElement elem = null;
		while ((System.currentTimeMillis() - startTime) < 91000) {
			log.info("Searching for element. Try number " + (tries++));
			try {
				elem = wait.until(ExpectedConditions
						.presenceOfElementLocated(locator));
				new Utility(driver).highlightElement(elem);
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
		return elem;
	}

	public WebElement waitForVisibilityOfElement(final WebElement element) {

		final long startTime = System.currentTimeMillis();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class,
						ElementNotVisibleException.class);
		int tries = 0;
		boolean found = false;
		WebElement elem = null;
		while ((System.currentTimeMillis() - startTime) < 91000) {
			log.info("Searching for element. Try number " + (tries++));
			try {
				elem = wait.until(ExpectedConditions.visibilityOf(element));
				new Utility(driver).highlightElement(elem);
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
		return elem;
	}

	public WebElement waitForVisibilityOfElementTillTimeout(final WebElement element,int timeOutInMilliSeconds) {

		final long startTime = System.currentTimeMillis();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(StaleElementReferenceException.class,
						ElementNotVisibleException.class);
		int tries = 0;
		boolean found = false;
		WebElement elem = null;
		while ((System.currentTimeMillis() - startTime) < timeOutInMilliSeconds) {
			log.info("Searching for element. Try number " + (tries++));
			try {
				elem = wait.until(ExpectedConditions.visibilityOf(element));
				new Utility(driver).highlightElement(elem);
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

}
