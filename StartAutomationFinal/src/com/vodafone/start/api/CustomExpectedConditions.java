package com.vodafone.start.api;

import java.io.IOException;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class CustomExpectedConditions {


	private  CustomExpectedConditions(){}

	/**
	 * An expectation for checking if the given text is present in the current
	 * page.
	 */
	public static ExpectedCondition<Boolean> textToBePresentInPage(
			final String text) {

		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return driver.getPageSource().contains(text);
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "text ('"+ text +"') to be present in element found by" + text;
			}


		};
	}

	public static void main(String args[]) throws IOException{
		
		Runtime runtime = Runtime.getRuntime();
		
		Process process = runtime.exec("adb devices > C:/Users/PatelM3/workspace/StartAutomation/abcdabcd.txt");
		
		System.out.println(process.toString());
	}

}
