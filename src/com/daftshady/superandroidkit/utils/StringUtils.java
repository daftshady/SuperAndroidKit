package com.daftshady.superandroidkit.utils;

import java.util.Locale;

/**
 * Provides `String` utility methods.
 * @author parkilsu
 *
 */
public class StringUtils {
	public static String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
        	camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
	}
	
	public static String toLowerCamelCase(String s) {
		String camelCaseString = toCamelCase(s);
		return camelCaseString.substring(0, 1).toLowerCase(Locale.ENGLISH) + 
				camelCaseString.substring(1);
	}

	private static String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() +
	               s.substring(1).toLowerCase();
	}
}