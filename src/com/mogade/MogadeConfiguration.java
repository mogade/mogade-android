package com.mogade;

public class MogadeConfiguration {
	private static String url = "http://api2.mogade.com/api/";
	private static String version = "gamma";
	
	public static void setUrl(String value) {
		Guard.NotNullOrEmpty(value, "A valid Api URL is required.");
		
		url = value;
	}
	
	public static String getUrl() {
		return url;
	}
	
	public static void setVersion(String value) {
		Guard.NotNullOrEmpty(value, "A valid version is required.");
		
		version = value;
	}
	
	public static String getVersion() {
		return version;
	}
}
