package com.hapramp.utils;

public class StringUtils {
		public static String stringify(String s){
				return s.replaceAll("\"", "\\\\\"");
		}
}
