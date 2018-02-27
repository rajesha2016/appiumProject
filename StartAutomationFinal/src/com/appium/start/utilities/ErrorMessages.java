package com.appium.start.utilities;

public class ErrorMessages {

	public static final String INVALID_REDIRECT = "User was not redirected to the expected url - ";
	public static final String CURRENT_URL = "Current Url is - ";
	public static final String NULL = " object unexpectedly Found NULL. Please assign the object properly.";
	public static final String WEBDRIVER = "WebDriver exception occurred";
	public static final String UNKNOWN = "Unexpected exception occurred. ";
	public static final String IOEXCEPTION = "An error occured while reading/writing from/to the file or stream."
			+ "Please check streams are properly initilized or file is reachable";
	public static final String FILENOTFOUND = "An expected file couldn't be found in classpath."
			+ " Please check the path for the file mentioned below.";
	public static final String ALERT_TEXT_EXPECTED = "Expected alert text is - ";
	public static final String ALERT_TEXT_ACTUAL = ".But actual alert text is - ";
	public static final String UNEXPECTED_ALERT = "An unexpected alert found. Please handle the alert pop-up";
	public static final String TEST_SCRIPT_NOT_FOUND = "No Test Scripts found in the given package. "
			+ "Please check the package path properly.";
	public static final String NO_ALERT_FOUND = "Expected an alert pop-up, but didn't find one.";
}
