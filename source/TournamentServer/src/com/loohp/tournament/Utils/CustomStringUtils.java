package com.loohp.tournament.Utils;

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
	
}
