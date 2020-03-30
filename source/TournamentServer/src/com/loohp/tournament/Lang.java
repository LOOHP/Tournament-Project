package com.loohp.tournament;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map.Entry;

import org.yaml.snakeyaml.Yaml;

import com.loohp.tournament.Utils.IO;

public class Lang {
	
	private static File DataFolder = Tournament.DataFolder;
	private static HashMap<String, Object> lang = new HashMap<String, Object>();
	public static HashMap<String, String> clientLang = new HashMap<String, String>();
	
	@SuppressWarnings("unchecked")
	public static void load() {
		String fileName = "language.yml";
		DataFolder.mkdirs();
        File file = new File(DataFolder, fileName);
        if (!file.exists()) {
        	IO.writeLn("Creating language.yml");
        	try (InputStream in = Tournament.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
                IO.writeLn("Created language.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		try {		
			Yaml yaml = new Yaml();    
			InputStream inputStream = new FileInputStream(file);
			lang = yaml.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
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
		
		clientLang.put("Rounds.Final", getLang("Common.Rounds.Final"));
		clientLang.put("Rounds.SemiFinal", getLang("Common.Rounds.SemiFinal"));
		clientLang.put("Rounds.QuaterFinal", getLang("Common.Rounds.QuaterFinal"));
		clientLang.put("Rounds.Others", getLang("Common.Rounds.Others"));
		
		clientLang.put("Title", getLang("Title"));
	}
	
	@SuppressWarnings("unchecked")
	public static String getLang(String key) {
		String[] tree = key.split("\\.");
		HashMap<String, Object> map = lang;
		for (int i = 0; i < tree.length - 1; i++) {
			map = (HashMap<String, Object>) map.get(tree[i]);
		}
		return (String) map.get(tree[tree.length - 1]);
	}

}
