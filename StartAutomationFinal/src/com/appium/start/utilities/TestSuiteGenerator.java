package com.appium.start.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import org.uncommons.reportng.JUnitXMLReporter;

public class TestSuiteGenerator {


	private static Logger log = Logger.getLogger(TestSuiteGenerator.class);
	private static String newLine="\r\n";
	private static List<String> testName = new LinkedList<String>();



	//	@Test
	//	public static void createXml(){
	//		String sourceFolder=".\\src\\com\\vodafone\\start\\exp";
	//
	//		String outputFilename="TestSuite";
	//		writeIntroIntoXml(sourceFolder,outputFilename);
	//
	//
	//		writeTestsIntoXml(sourceFolder,outputFilename);
	//
	//		System.out.println("Test Suite created successfully");
	//	}


	private static void writeIntroIntoXml(String sourceFolder,String outputFilename) {

		try {

			String schema = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			String suiteStart ="<suite name=\""+outputFilename+"\" parallel=\"tests\" thread-count=\"1\">";


			File file = new File("./../input/"+outputFilename+".xml");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(schema);
			bw.write(newLine);
			bw.write(newLine);
			bw.write(suiteStart);
			bw.write(newLine);

			bw.flush();

			bw.close();

			System.out.println(" Intro Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//	private static void writeTestsIntoXml(String sourceFolder,String outputFilename) {
	//
	//		try {
	//
	//
	//			String classes="\t\t<classes>";
	//
	//			String classesEnd="\t\t</classes>";
	//			String testEnd="\t</test>";
	//			String suiteEnd="\t</suite>";
	//
	//			File file = new File("./../input/"+outputFilename+".xml");
	//
	//
	//			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
	//			BufferedWriter bw = new BufferedWriter(fw);
	//
	//
	//
	//			List<String> fileList=generateFileList(new File(sourceFolder));
	//			for(int k=0;k<fileList.size();k++){
	//
	//				String testStart="\t<test name=\""+testName.get(k)+"\">";
	//				String className="\t\t\t<class name=\""+ repairTestCasePath(fileList.get(k).toString())+"\"></class>";
	//
	//
	//				bw.write(newLine);
	//				bw.write(testStart);
	//				bw.write(newLine);
	//				bw.write(classes);
	//				bw.write(newLine);
	//				bw.write(className);
	//				bw.write(newLine);
	//				bw.write(classesEnd);
	//				bw.write(newLine);
	//				bw.write(testEnd);
	//				bw.write(newLine);
	//
	//
	//				bw.flush();
	//			}
	//
	//			bw.write(newLine);
	//			bw.write(suiteEnd);
	//			bw.flush();
	//
	//			bw.close();
	//
	//			System.out.println("Test writing Done");
	//
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
	//



	private static String repairTestCasePath(String testCaseName){

		String[] remove=testCaseName.split(".java");
		String[] temp=null;

		if(remove[0].trim().length()>0){

			temp= remove[0].split("src\\\\");

			if(temp[1].trim().length()>0){

				temp[1]=temp[1].replace("\\", ".");
			}
			else{

				System.out.println("some error occurred while trying to split from src");
			}
		}
		else{

			System.out.println("Some error occured while trying to remove .java");
		}


		return temp[1];
	}

	public void createTestSuite(int suiteNo, LinkedList<LinkedList<String>> params,String testSourceDir){

		try{
			HTMLReporter htmlReporter = new HTMLReporter();
			JUnitXMLReporter junitReporter = new JUnitXMLReporter();
			Map<String,String> parameters = new HashMap<String,String>();

			
			String testParameters = params.get(2).get(suiteNo) + "," + //device name
									params.get(4).get(suiteNo) + "," + // implicit login
									params.get(5).get(suiteNo) + "," + // msisdn
									params.get(6).get(suiteNo) + "," + // test data sheet
									params.get(7).get(suiteNo) + "," + // cta no
									params.get(8).get(suiteNo); //cta text
									
			parameters.put(Keywords.DEVICE_NAME, testParameters);
			
			

			XmlSuite suite = new XmlSuite();
			suite.setName(params.get(0).get(suiteNo));

			List<String> fileList= generateFileList(new File(testSourceDir),params.get(3).get(suiteNo));
			int i=0;

			log.info("source dir : "+ testSourceDir + " \r\n fileList size: " + fileList.size());
			if(fileList.size() == 0){

				throw new AssertionError(ErrorMessages.TEST_SCRIPT_NOT_FOUND);
			}

			for(String s : fileList){

				List<XmlClass> _class =  new ArrayList<XmlClass>();
				XmlTest test = new XmlTest(suite);

				String[] s2 = s.split("com");
				String[] s3= s2[1].split("\\.");

				s3[0] = "com" + s3[0];
				s3[0] = s3[0].replaceAll("\\\\", ".");

				_class.add(new XmlClass(s3[0]));

				test.setName(testName.get(i));
				test.setClasses(_class);
				test.setParameters(parameters);

				i++;

			}

			List<XmlSuite> suites = new ArrayList<XmlSuite>();
			suites.add(suite);

			TestNG testNG = new TestNG();
			testNG.setXmlSuites(suites);
			testNG.setOutputDirectory(PathReader.readPath("reportSrc"));
			testNG.addListener(htmlReporter);
			testNG.addListener(junitReporter);
			testNG.run();

		}catch(Exception e){

			log.error("Exception occurred while creating test suite."
					+ " Error Message is - "+e.getMessage());

			throw new AssertionError();
		}
	}



	/**
	 * Traverse a directory and get all files,
	 * and add the file into fileList  
	 * @param node file or directory
	 */
	private static List<String> generateFileList(File node, String scripts){

		ExcelReader excel = new ExcelReader();
		List<String> fileList=new LinkedList<String>();
		List<String> suite = new ArrayList<String>();

		try {
			File[] files = node.listFiles();
			log.info("Found "+ node.getAbsolutePath() +" for test scripts");
			log.info("Total scripts found in the path are - "+ files.length);
			HashMap<String,String> testId = excel.getTestId("Smoke");
			log.info("Received -" + testId.size() + " tests");

			if(scripts.contains(",")){

				suite = Arrays.asList(scripts.split(","));

			}
			else{
				
				suite.add(scripts);
			}

			log.info("suite has "+ suite.size() + "scripts");

			if(suite.size()>0){

				for(String test : suite){

					for(Entry<String,String> s : testId.entrySet()){
						for (File file : files) {
							if (file.isDirectory()) {

								log.info("directory:" + file.getCanonicalPath());

								generateFileList(file,scripts);
							} else {

								log.info("Splitting file name for file - " + file.getName());
								String fileName = file.getName();
								String name[] = fileName.split("\\.");

								log.info("Filename from Excel - " + s.getKey()+ 
										" & FileName from Source - "+ name[0]);

								if(test.equalsIgnoreCase(s.getValue())){
									
									if(s.getKey().equalsIgnoreCase(name[0])){


										fileList.add(file.getCanonicalPath());

										testName.add(s.getValue());
										break;
									}

								}
							}
						}
					}
				}	
			}
			
			log.info("Size of file list -" + fileList.size());


		} catch (Exception e) {

			log.error("Error occured while generating fileList. "
					+ "Error Message is - "+ e.toString());
		}
		return fileList;
	}
	
}
