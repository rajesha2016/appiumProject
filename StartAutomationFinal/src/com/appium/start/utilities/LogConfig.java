package com.appium.start.utilities;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * configuration class for logger
 * @author Manan
 *
 */
public class LogConfig {

	/**
	 * configures log4j for logging purposes
	 */
	public static void setLogger(){
		
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();
		
		 ConsoleAppender console = new ConsoleAppender();
	
		  String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		  console.setLayout(new PatternLayout(PATTERN)); 
		  console.setThreshold(Level.INFO);
		  console.activateOptions();
		  
		  Logger.getRootLogger().addAppender(console);

		  FileAppender fa = new FileAppender();
		  fa.setFile(PathReader.readPath("logs"));
		  fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		  fa.setThreshold(Level.INFO);
		  fa.setAppend(false);
		  fa.activateOptions();

		  Logger.getRootLogger().addAppender(fa);
	}
}
