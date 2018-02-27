package com.appium.start.utilities;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * this class reads test data from the excel sheet
 * @author Manan
 *
 */
public class ExcelReader {

	private static Logger log = Logger.getLogger(ExcelReader.class);

	private final String testDataPath = PathReader.readPath("testData");
	private final String testConfigPath = PathReader.readPath("testConfig");



	/**
	 * reads  test data from excel sheet and stores it in java POJO
	 * @param _row to be read from sheet
	 * @return TestData object containing test data
	 */
	public TestObject getTestData(String testId,String sheetName) {
		TestObject data = new TestObject();
		LinkedList<String> testData = new LinkedList<String>();

		try {

			Workbook workbook = Workbook.getWorkbook(new File(testDataPath));
			Sheet sheet = workbook.getSheet(sheetName);
			int _column = 0;
			int _row = 0;

			for(int i=0;i<sheet.getRows();i++){
				
				if(sheet.getCell(_column + 1, i).getContents().equalsIgnoreCase(testId)){
					
					_row = i;
					break;
				}
			}
			
			int j = 3; //skipping first three columns
			while( sheet.getCell(_column + j, _row).getContents().trim() != null){
			
				testData.add(sheet.getCell(_column + j, _row).getContents().trim());
				
				log.info("added column " + j + " with data " + sheet.getCell(_column + j, _row).getContents().trim());
			
				j++;
			}
			

			log.info("Row no. " + _row + " read from Excel sheet. ");


		}	catch (ArrayIndexOutOfBoundsException a) {
				
			/**setting excel data into list**/
			data.setData(testData);
			
		} catch (Exception e) {
			log.error("An error occured while reading data from excel.");
			Assert.fail("An error occured while reading data from excel. "
					+ "Error message is : "+ e.getMessage());
		}
		return data;
	}

	/**
	 * reads  test configuration from excel sheet and stores it in java POJO
	 * @param _row to be read from sheet
	 * @return TestData object containing test data
	 */
	public TestObject getTestConfig(String sheetName) {
		TestObject testConfig = new TestObject();

		try {

			Workbook workbook = Workbook.getWorkbook(new File(testConfigPath));
			System.out.println("testConfig path - " + testConfigPath);
			Sheet sheet = workbook.getSheet(sheetName);
			HashMap<String,String> config = new HashMap<String, String>();
			int _column = 0;

			for(int i=0;i<sheet.getRows();i++){
				
				if(sheet.getCell(_column, i).getContents() != null &&
						(!sheet.getCell(_column, i).getContents().trim().equals(""))	){
					
					config.put(sheet.getCell(_column, i).getContents().trim(),
							sheet.getCell(_column +1 , i).getContents().trim());
					
				}
			}
			
			testConfig.setTestConfig(config);
			
			log.info( sheet.getRows() +" read from "+ sheetName +" sheet of "+testConfigPath );

		} catch (Exception e) {
			
			StartExceptionHandler.handle(e, null, 
					this.getClass().getSimpleName(),Utility.getMethodName());
		}
		
		return testConfig;
	}

	/**
	 * reads dashboard data from excel sheet and stores it in java POJO
	 * @param sheetname from where data is to be read
	 * @return TestData object containing test data
	 */
	public TestObject getDashboardData(String sheetName) {
		TestObject data = new TestObject();
		LinkedList<String> testData = new LinkedList<String>();

		try {

			Workbook workbook = Workbook.getWorkbook(new File(testDataPath));
			Sheet sheet = workbook.getSheet(sheetName);
			int _column = 0;
			int _row = 1;

			int j = 0;
			while( sheet.getCell(_column, _row + j).getContents().trim() != null){
			
				testData.add(sheet.getCell(_column, _row + j).getContents().trim());
				
				System.out.println("added row " + j + " with data " + sheet.getCell(_column , _row + j).getContents().trim());
			
				j++;
			}
			

			log.info(testData.size() + "dashboards read from Excel sheet. ");


		}	catch (ArrayIndexOutOfBoundsException a) {
				
			/**setting excel data into list**/
			data.setData(testData);
			log.info("Test Data set successfully in test containers");

		} catch (Exception e) {
			log.error("An error occured while reading data from excel.");
			Assert.fail("An error occured while reading data from excel. "
					+ "Error message is : "+ e.getMessage());
		}
		return data;
	}
	
	
	public HashMap<String,String> getTestId(String sheetName){
		
		HashMap<String,String> config = new LinkedHashMap<String, String>();
		
		try {

			Workbook workbook = Workbook.getWorkbook(new File(testDataPath));
			Sheet sheet = workbook.getSheet(sheetName);

			int _column = 0;

			for(int i=1;i<sheet.getRows();i++){
				
				if((sheet.getCell(_column , i).getContents() != null &&
						(!sheet.getCell(_column, i).getContents().trim().equals("")))){
						//&& sheet.getCell(_column , i).getContents().trim().equals("Yes")){
					
				
				if((sheet.getCell(_column +1, i).getContents() != null &&
						(!sheet.getCell(_column +1, i).getContents().trim().equals("")))){
					
					config.put(sheet.getCell(_column +1, i).getContents().trim(),
							sheet.getCell(_column +2 , i).getContents().trim());
					
				}
			}
			}
			
			
			
			log.info( sheet.getRows() +" read from "+ sheetName +" sheet of "+testConfigPath );

		} catch (Exception e) {
			
			log.error("Exception occured while reading test configuration from "+  testConfigPath +"\r\n Error message is - " + e.getMessage());
			StartExceptionHandler.handle(e, null, 
					this.getClass().getSimpleName(),Utility.getMethodName());
		}
		
		return config;
	}
	
	

	/**
	 * reads device configuration data from excel sheet and stores it in java POJO
	 * @param deviceName name of device to use
	 * @return TestData object containing test data
	 */
	public TestObject getDeviceConfig(String deviceName) {
		TestObject data = new TestObject();
		LinkedList<String> testData = new LinkedList<String>();

		try {

			Workbook workbook = Workbook.getWorkbook(new File(testConfigPath));
			Sheet sheet = workbook.getSheet("Configuration");
			int _column = 0;
			int _row = 0;

			for(int i=0;i<sheet.getRows();i++){
				
				if(sheet.getCell(_column , i).getContents().trim().equalsIgnoreCase(deviceName)){
					
					_row = i;
					break;
				}
			}
			
			int j = 1; //skipping first column
			while( sheet.getCell(_column + j, _row).getContents().trim() != null &&
					(!sheet.getCell(_column + j, _row).getContents().trim().equals(""))){
			
				testData.add(sheet.getCell(_column + j, _row).getContents().trim());
				
				log.info("added column " + j + " with  device data " + sheet.getCell(_column + j, _row).getContents().trim());
			
				j++;
			}
			

			log.info("Row no. " + _row + " read from Excel sheet. ");
			log.info("setting excel data into list");
			data.setData(testData);
			log.info("Device config data from TestConfig set succefully in test containers");
			

		} catch (ArrayIndexOutOfBoundsException e) {
			
			log.info("Index exceeded : setting excel data into list");
			data.setData(testData);
		}
		catch (Exception e) {
			log.error("An error occured while reading data from excel.");
			Assert.fail("An error occured while reading data from excel. "
					+ "Error message is : "+ e.getMessage());
		}
		return data;
	}
	
	
	/**
	 * stores test setup data into singleton hashmap
	 * @return
	 */
	public HashMap<String,LinkedList<String>> getTestSetupData(){
	
	
		try {

			Workbook workbook = Workbook.getWorkbook(new File(testConfigPath));
			System.out.println("testConfig path - " + testConfigPath);
			Sheet sheet = workbook.getSheet("setup");
			HashMap<String,LinkedList<String>> config = new HashMap<String, LinkedList<String>>();
			LinkedList<String> values = new LinkedList<String>();
			int _column = 0;

			for(int i=0;i<sheet.getRows();i++){
				
				if(sheet.getCell(_column, i).getContents() != null &&
						(!sheet.getCell(_column, i).getContents().trim().equals(""))	){
					
					int j = 1; //skipping first column
					while( sheet.getCell(_column + j, i).getContents().trim() != null && (!sheet.getCell(_column + j, i).getContents().trim().equals(""))){
					
						values.add(sheet.getCell(_column + j, i).getContents().trim());
						
						log.info("added column " + j + " with data " + sheet.getCell(_column + j, i).getContents().trim() + " in setup list.");
					
						j++;
					}
					
					config.put(sheet.getCell(_column, i).getContents().trim(),values);
					values=new LinkedList<String>();
					
				}
			}
			
			TestConfiguration.setTestSetupData(config);
			
			log.info( sheet.getRows() +" read from "+ "setup" +" sheet of "+testConfigPath );

		} catch (Exception e) {
			
			StartExceptionHandler.handle(e, null, 
					this.getClass().getSimpleName(),Utility.getMethodName());
		}
		
		return TestConfiguration.getSetupData();
	}
	
	public static void main(String[] args){
		
		ExcelReader e = new ExcelReader();
		
		HashMap<String,LinkedList<String>> testId = e.getTestSetupData();
		HashMap<String,LinkedList<String>> test = TestConfiguration.getSetupData();
	
		
		for(Entry<String, LinkedList<String>> s : testId.entrySet()){
			
			for(String x : s.getValue()){
			System.out.println("Not Test" + s.getKey() + " - " + x);
			}
		}
		
		for(Entry<String, LinkedList<String>> s : test.entrySet()){
			
			for(String x : s.getValue()){
			System.out.println("Test ::::: " + s.getKey() + " - " + x);
			}
		}
	}
}


