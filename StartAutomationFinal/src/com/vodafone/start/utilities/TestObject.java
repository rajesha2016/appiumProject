package com.vodafone.start.utilities;

import java.util.HashMap;
import java.util.LinkedList;

/**this class stores the test data defined in spreadsheet
 * @author Manan
 *
 */
public class TestObject {

	private LinkedList<String> testData;
	private HashMap<String,String> testSetup;
	
	
	
	public HashMap<String,String> getTestConfig() {
		return testSetup;
	}
	public void setTestConfig(HashMap<String,String> testSetup) {
		this.testSetup = testSetup;
	}
	public LinkedList<String> getData() {
		return testData;
	}
	public void setData(LinkedList<String> testData) {
		this.testData = testData;
	}
	
	
	
	

	
	
	
}
