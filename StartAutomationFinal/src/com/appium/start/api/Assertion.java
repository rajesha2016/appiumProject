package com.appium.start.api;

import org.openqa.selenium.WebDriver;

import com.appium.start.utilities.Utility;

public class Assertion {

	/**
	 * asserts a condition and fails with a message and screenshot if not true 
	 * @param driver current instance of browser
	 * @param message failed message
	 */
	public static void fail(WebDriver driver, String message, String className){

		Utility util = new Utility(driver);
		
		if(null != driver){

			new Utility(driver).alertTestFail(message);
			
			if(null == className){
				
				className = "";
				
			}
			
			util.takeFailedScreenShot(className); //new Timestamp(System.currentTimeMillis()).toString());
			driver.quit();
			//util.killBrowser();
		}
		
		
		throw new AssertionError(message);
	}

	/**
	 * asserts a condition and fails with a message and screenshot if not true 
	 * @param driver current instance of browser
	 * @param message failed message
	 */
	public static void assertTrue(WebDriver driver, boolean condition, String message,String name){

		if(!condition){

			//TODO set valid name for screenshot	
			new Utility(driver).takeFailedScreenShot(name);
			driver.quit();
			throw new AssertionError("Expected the condition to be true, but found false!!" + message);

		}

	}
	
	/**
	 * asserts a condition and fails with a message and screenshot if true 
	 * @param driver current instance of browser
	 * @param message failed message
	 */
	public static void assertFalse(WebDriver driver, boolean condition, String message){

		if(!condition){

			//TODO set valid name for screenshot	
			new Utility(driver).takeFailedScreenShot(message);
			driver.quit();
			throw new AssertionError("Expected the condition to be false, but found true!!" + message);

		}

	}
	
	 /**
	   * Asserts that two objects are equal. If they are not,
	   * an AssertionError, with the given message, is thrown.
	   * @param actual the actual value
	   * @param expected the expected value
	   * @param message the assertion error message
	   */
	  public static void assertEquals(WebDriver driver,Object actual, Object expected, String message,String name) {
	    if((expected == null) && (actual == null)) {
	      return;
	    }
	    if(expected != null) {
	       if (expected.equals(actual)) {
	        return;
	      }
	    }
	    fail(driver, message,name);
	  }

}
