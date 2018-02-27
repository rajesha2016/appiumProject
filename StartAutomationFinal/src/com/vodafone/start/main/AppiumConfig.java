package com.vodafone.start.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

public class AppiumConfig {

	
	public void startAppiumServer() {
		
		try {
			
			String pidInfo="cmd /c \"C:/automation/programs/AppiumForWindows-1.3.4.1/Appium/node.exe\" \"C:/automation/programs/AppiumForWindows-1.3.4.1/Appium/node_modules/appium/bin/appium.js\" "
					+ "--address 127.0.0.1 --chromedriver-port 9516 --bootstrap-port 4725 --selendroid-port 8082 --no-reset --local-timezone";
			
			
//			Process p = Runtime.getRuntime().exec("\"C:/automation/programs/AppiumForWindows-1.3.4.1/appium-1.4.0/appium-1.4.0/bin/node.exe\" "
//					+ "\"C:/automation/programs/AppiumForWindows-1.3.4.1/appium-1.4.0/appium-1.4.0/bin/appium.js\" --no-reset --local-timezone");
//		
//			System.out.println("server started - " + p);
//			
//			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//			while ((line = input.readLine()) != null) {
//			    pidInfo+=line; 
//			}
//
//			input.close();
//
//			System.out.println("process : " + pidInfo);
			
			DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			Executor executor = new DefaultExecutor();
			executor.setExitValue(1);
			executor.execute(new CommandLine(pidInfo), resultHandler);
			System.out.println("server started");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void stopAppiumServer() {
		
		try {
			
			Runtime.getRuntime().exec("cmd /c echo off & FOR /F \"usebackq tokens=5\" %a in (`netstat -nao ^| findstr /R /C:\"4723\"`) do"
					+ " (FOR /F \"usebackq\" %b in (`TASKLIST /FI \"PID eq %a\" ^| findstr /I node.exe`) do taskkill /F /PID %a)");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
