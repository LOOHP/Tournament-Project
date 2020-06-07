package com.loohp.tournament.Utils;

import java.util.ArrayList;
import java.util.List;

public class CustomStringUtils {
	
	public static boolean arrayContains(String compare, String[] args, boolean IgnoreCase) {
		for (String string : args) {
			if (IgnoreCase == false) {
				if (string.equals(compare)) {
					return true;
				}
			} else {
				if (string.equalsIgnoreCase(compare)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean arrayContains(String compare, String[] args) {
		return arrayContains(compare, args, true);
	}
	
	public static String[] splitStringToArgs(String str) {
		List<String> tokens = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();

		boolean insideQuote = false;

		for (char c : str.toCharArray()) {
		    if (c == '"') {
		        insideQuote = !insideQuote;
		    } else if (c == ' ' && !insideQuote) {
		    	if (sb.length() > 0) {
					tokens.add(sb.toString());
				}
		        sb.delete(0, sb.length());
		    } else {
		        sb.append(c);
		    }
		}
		tokens.add(sb.toString());
		
		return tokens.toArray(new String[tokens.size()]);
	}
	
}
