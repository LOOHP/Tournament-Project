package com.loohp.tournamentclient.Functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;

public class GetReport {
	
	public static void getReport(String in) {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		String fileName = new SimpleDateFormat("yyyy'-'MM'-'dd'_'HH'-'mm'-'ss'_'zzz'.txt'").format(new Date());
		fileChooser.setSelectedFile(new File(fileName));
		int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File file = fileChooser.getSelectedFile();
		    if (!file.exists()) {
		    	try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    
		    String report = in.substring(in.indexOf("function:getreport=") + 19);
		    try {
				PrintWriter writer = new PrintWriter(file);
				writer.write(report);
				writer.flush();
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		    
		}
	}

}
