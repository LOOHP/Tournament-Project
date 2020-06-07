package com.loohp.tournamentclient.Functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.loohp.tournamentclient.Lang;
import com.loohp.tournamentclient.Utils.TextOutputUtils;

public class FinishReport {
	
	public static void save(String in) {
		
		String str = "The tournament has finished! Do you want to save a report?";
		if (Lang.lang.containsKey("FinishReport.Text")) {
			str = Lang.lang.get("FinishReport.Text");
		}
		int dialogResult = JOptionPane.showConfirmDialog(null, str);
		if(dialogResult == JOptionPane.YES_OPTION){
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			String fileName = new SimpleDateFormat("yyyy'-'MM'-'dd'_'HH'-'mm'-'ss'_'zzz'_Complete.txt'").format(new Date());
			fileChooser.setSelectedFile(new File(fileName));
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File file = fileChooser.getSelectedFile();
			    if (!file.exists()) {
			    	try {
						file.createNewFile();
					} catch (IOException e) {
						TextOutputUtils.appendText("Failed to save report!", true);
					}
			    }
			    
			    String report = in.substring(in.indexOf("function:finishreport=") + 22);
			    try {
					PrintWriter writer = new PrintWriter(file);
					writer.write(report);
					writer.flush();
					writer.close();
				} catch (FileNotFoundException e) {
					TextOutputUtils.appendText("Failed to save report!", true);
				}
			    
			}
		}
	}

}
