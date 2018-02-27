package com.appium.start.utilities;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.appium.start.main.MainTest;

import jxl.Workbook;
 

/**
 * @author Manan
 *
 */

public class FileManager
{
    List<String> fileList;

   

	private static Logger log = Logger.getLogger(FileManager.class);
	
    public FileManager(){
	fileList = new ArrayList<String>();
    }
 
    
 
  

    /**
     * Generate the file name
     * @param suitname
     * @return newFile name
     */
	public String generateFileName(String suitName){

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh-mm-ss");
		Date date= new Date();
		
		String now=sdf.format(date);
		String newName=suitName.concat("-").concat(now);


		return newName;
	}
	
	/**
	 * copies files from one directory to another directory
	 * @param srcDirPath path of folder from where to copy files
	 * @param destDirPath path of folder to which files are copied
	 */
	public void copyTestData(String srcDirPath, String destDirPath){
		
		try {
			

			File srcDir = new File(srcDirPath);
			File destDir = new File(destDirPath);
			
			FileUtils.copyDirectory(srcDir, destDir);	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * collects all files from given source directory and stores them in list
	 * @param dir source from where files needs to be collected
	 * @param fileList list of collected files
	 */
	public static void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					log.info("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					log.info("file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zips files present in the list to actual destination
	 * @param directoryToZip path of source dir to zip
	 * @param fileList list containing all zippable files
	 * @param ZipName name of the zipped directory
	 */
	public static void writeZipFile(File directoryToZip, List<File> fileList,String ZipName) {

		try {
			
			FileOutputStream fos = new FileOutputStream(PathReader.readPath("reportDest")+ ZipName+ ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * adds files to directory getting zipped
	 * @param directoryToZip source directory 
	 * @param file current file to zip
	 * @param zos zip output stream
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		FileInputStream fis = new FileInputStream(file);

		
		
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		log.info("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * deletes test reports from output folder
	 * @param reportPath path of test reports
	 * @return true if deleted successfully else false
	 */
	public boolean deleteTestReports(File reports){
		
		
		String[]entries = reports.list();
		for(String s: entries){
		    File currentFile = new File(reports.getPath(),s);
		    
		    if(currentFile.isDirectory()){
		    	
		    	deleteTestReports(currentFile);
		    }
		    
		    currentFile.delete();
		}
		
		return reports.delete();
	}
	
	
}