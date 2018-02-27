package com.vodafone.start.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.log4testng.Logger;

import com.vodafone.start.api.Assertion;

@SuppressWarnings("serial")
public class StartExceptionHandler extends Exception{
	
	private static Logger log = Logger.getLogger(StartExceptionHandler.class);

	public static void handle(Exception e, WebDriver driver, String className, String method){
		
		if(e instanceof NullPointerException){
			
			Assertion.fail(driver, ErrorMessages.NULL + "\r\n Calling Method : " +className+ "."+ method
					+ "\r\nError Message :" + e.getMessage(),className);
			log.error(ErrorMessages.NULL + " Error Message :" + e.getMessage());
			
		} else if(e instanceof WebDriverException){
			
			Assertion.fail(driver, ErrorMessages.WEBDRIVER + "\r\n Calling Method : " +className+ "."+ method
					+ "\r\nError Message :" + e.getMessage(),className);
			log.error(ErrorMessages.WEBDRIVER + " Error Message :" + e.getMessage());
			
		} else if(e instanceof TimeoutException){
			
			Assertion.fail(driver, "Page (" + driver.getCurrentUrl() + ") couldn't be loaded in specified time."+
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage(),className);
			log.error("Page (" + driver.getCurrentUrl() + ") couldn't be loaded in specified time."+
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage());
			
		} else if(e instanceof IOException){
			
			Assertion.fail(driver, ErrorMessages.IOEXCEPTION +
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage(),className);
			log.error(ErrorMessages.IOEXCEPTION +
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage());
			
		}else if(e instanceof FileNotFoundException){
			
			Assertion.fail(driver, ErrorMessages.IOEXCEPTION +
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage(),className);
			log.error(ErrorMessages.IOEXCEPTION +
					 "\r\n Calling Method : " +className+ "."+ method
		
					 
					 + "\r\nError Message :" + e.getMessage());
		}else if(e instanceof UnhandledAlertException){
			
			Assertion.fail(driver, ErrorMessages.UNEXPECTED_ALERT +
					 "\r\n Calling Method : " +className+ "."+ method
						+ "\r\nError Message :" + e.getMessage(),className);
			log.error(ErrorMessages.UNEXPECTED_ALERT +
					 "\r\n Calling Method : " +className+ "."+ method
					 + "\r\nError Message :" + e.getMessage());
		}else{
			
			Assertion.fail(driver, ErrorMessages.UNKNOWN + "\r\n Calling Method : " +className+ "."+ method
					+ "\r\nError Message :" + e.toString(), className);
		}
		
		
	}
	
}
