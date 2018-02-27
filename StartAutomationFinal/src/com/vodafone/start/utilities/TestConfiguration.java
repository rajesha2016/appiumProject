package com.vodafone.start.utilities;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * singleton class to store and reuse test setup data
 * @author Manan
 *
 */
public class TestConfiguration {

	private static HashMap<String,LinkedList<String>> testConfiguration;
	
	/**
	 * private constructor
	 */
	private TestConfiguration(){}
	
	/**
	 * stores and returns test setup data
	 * @return
	 */
	public static HashMap<String,LinkedList<String>> getSetupData(){
		
		if(null == testConfiguration){
			
			return new HashMap<String,LinkedList<String>>();
		}
		
		return testConfiguration;
	}
	
	/**
	 * sets test setup data
	 * @param config
	 */
	public static void setTestSetupData(HashMap<String, LinkedList<String>> config){
		
		testConfiguration = config;
	}
}
