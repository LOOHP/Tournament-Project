package com.loohp.tournament;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import com.loohp.tournament.Utils.IO;

public class Lang {

	private File DataFolder;
	private HashMap<String, Object> lang;
	private HashMap<String, String> clientLang;
	
	@SuppressWarnings("unchecked")
	public Lang(File dataFolder) {
		DataFolder = dataFolder;
		lang = new HashMap<>();
		clientLang = new HashMap<>();
		
		String fileName = "language.yml";
		DataFolder.mkdirs();
        File file = new File(DataFolder, fileName);
        if (!file.exists()) {
        	IO.writeLn("Creating language.yml");
        	try (InputStream in = TournamentServer.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
                IO.writeLn("Created language.yml");
            } catch (IOException e) {

    			StringWriter errors = new StringWriter();
    			e.printStackTrace(new PrintWriter(errors));
    			IO.writeLn(errors.toString());
    			
            }
        }
		try {		
			Yaml yaml = new Yaml();    
			InputStream inputStream = new FileInputStream(file);
			lang = yaml.load(inputStream);
			
			clientLang.clear();
			HashMap<String, Object> map = (HashMap<String, Object>) lang.get("Client");
			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				HashMap<String, String> in = (HashMap<String, String>) entry.getValue();
				if (in.containsKey("Text")) {
					if (!in.get("Text").equals("")) {
						clientLang.put(key + ".Text", in.get("Text"));
					}
				}
				if (in.containsKey("ToolTip")) {
					if (!in.get("ToolTip").equals("")) {
						clientLang.put(key + ".ToolTip", in.get("ToolTip"));
					}
				}
			}
			
			clientLang.put("Rounds.Final", get("Common.Rounds.Final"));
			clientLang.put("Rounds.SemiFinal", get("Common.Rounds.SemiFinal"));
			clientLang.put("Rounds.QuaterFinal", get("Common.Rounds.QuaterFinal"));
			clientLang.put("Rounds.Others", get("Common.Rounds.Others"));
			
			clientLang.put("Title", get("Title"));
			
		} catch (Exception e) {
			IO.writeLn("Error while parsing language.yml: You might want to check the syntax on the lines below");

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());
			
			IO.writeLn("");
			IO.writeLn("You can always regenerate a new one by removing the file from its location");
			System.exit(4);
		}
	}
	
	public HashMap<String, String> getClientLang() {
		return clientLang;
	}
	
	@SuppressWarnings("unchecked")
	public String get(String key) {
		try {
			String[] tree = key.split("\\.");
			HashMap<String, Object> map = lang;
			for (int i = 0; i < tree.length - 1; i++) {
				map = (HashMap<String, Object>) map.get(tree[i]);
			}
			return (String) map.get(tree[tree.length - 1]);
		} catch (Exception e) {
			return "null";
		}
	}

}
