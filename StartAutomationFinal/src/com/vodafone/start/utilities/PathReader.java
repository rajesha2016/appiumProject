package com.vodafone.start.utilities;

import java.util.Enumeration;
import java.util.ResourceBundle;

public final class PathReader {

	public static String  readPath(String key) {

		String value = "Requested Key "+ key + " has no defined value.";
		ResourceBundle rb = ResourceBundle.getBundle("Path");
		Enumeration <String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {

			if(keys.nextElement().equalsIgnoreCase(key)){

				value = rb.getString(key);
				break;
			}

		}	

		return value;	
	}

}
