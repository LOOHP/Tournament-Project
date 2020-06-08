package com.loohp.tournamentpackage;

public class Resource {
	
	String fileName;
	String url;
	
	public Resource(String fileName, String url) {
		this.fileName = fileName;
		this.url = url;
	}
	
	public String getName() {
		return fileName;
	}
	
	public String getUrl() {
		return url;
	}

}
