package com.loohp.tournament.Web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.Tournament;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Round.Round;

public class WebManager {
	
	public static File WebFolder = Web.WebFolder;
	public static File TemplateFolder = new File(Web.WebFolder.getPath() + "/template");
	
	public static void run() {
		
		WebFolder.mkdirs();
		TemplateFolder.mkdir();
		
		String fileName = "jquery-1.11.3.min.js";
        File file = new File(WebFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        fileName = "jquery.bracket.min.js";
        file = new File(WebFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        fileName = "jquery.bracket.min.css";
        file = new File(WebFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        fileName = "inactive.html";
        file = new File(TemplateFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        fileName = "bracket.html";
        file = new File(TemplateFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        fileName = "index.html";
        String resource = "inactive.html";
        file = new File(WebFolder, fileName);
        if (!file.exists()) {
        	try (InputStream in = new FileInputStream(new File(TemplateFolder, resource))) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		while (true) {
			if (!Tournament.activeCompetition.isPresent()) {
				try {TimeUnit.MILLISECONDS.sleep(10);} catch (InterruptedException e) {}
				continue;
			}
			Competition comp = Tournament.activeCompetition.get();
			List<String> groupString = new ArrayList<String>();
			for (Group group : comp.getRounds().get(0).getGroups()) {
				String string = "[";
				if (group.getHome().getName().equals("_BYE_")) {
					string = string + "null";
				} else {
					string = string + "\"" + group.getHome().getName() + "\"";
				}
				string = string + ",";
				if (group.getAway().getName().equals("_BYE_")) {
					string = string + "null";
				} else {
					string = string + "\"" + group.getAway().getName() + "\"";
				}
				string = string + "]";
				groupString.add(string);
			}
			String teams = String.join(",", groupString);
			
			List<String> eachRound = new ArrayList<String>();
			for (Round round : comp.getRounds()) {
				List<String> eachGroup = new ArrayList<String>();
				for (Group group : round.getGroups()) {
					String string = "";
					if (!group.getWinner().isPresent()) {
						string = "[null, null]";
					} else if (group.getWinner().get().equals(group.getHome())) {
						string = "[1, 0]";
					} else if (group.getWinner().get().equals(group.getAway())) {
						string = "[0, 1]";
					}
					eachGroup.add(string);
				}
				String roundString = "[" + String.join(",", eachGroup) + "]";
				eachRound.add(roundString);
			}
			String results = String.join(",", eachRound);
			
			fileName = "index.html";
	        resource = "bracket.html";
	        File resourceFile = new File(TemplateFolder, resource);
	        file = new File(WebFolder, fileName);
        	try {
        		BufferedReader br = new BufferedReader(new FileReader(resourceFile));
        		String line;
        		PrintWriter writer = new PrintWriter(file);
        	    while ((line = br.readLine()) != null) {
        	    	String newline = line.replace("%teams%", teams).replace("%results%", results);
        	    	writer.write(newline + "\n");
        	    }
        	    writer.flush();
        	    writer.close();
        	    br.close();
        		
            } catch (IOException e) {
                e.printStackTrace();
            }
        	try {TimeUnit.MILLISECONDS.sleep(10);} catch (InterruptedException e) {}
		}
	}

}
