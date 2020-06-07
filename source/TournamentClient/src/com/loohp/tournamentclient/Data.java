package com.loohp.tournamentclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Data {
	
	private static File StorageFolder = new File("clientdata");
	private static File file;
    private static JSONObject json;
    private static JSONParser parser = new JSONParser();
	
	@SuppressWarnings("unchecked")
	public synchronized static void loadDatabase() {		
		try {
			String fileName = "client.json";
			StorageFolder.mkdirs();
			file = new File(StorageFolder, fileName);
			if (!file.exists()) {
        	    PrintWriter pw = new PrintWriter(file, "UTF-8");
        	    pw.print("{");
        	    pw.print("}");
        	    pw.flush();
        	    pw.close();
        	}
        	System.out.println("Reading database..");
        	json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (Exception e) {
			System.out.println("Error while loading database! Perhaps the file has been edited or is corrupted?");
			e.printStackTrace();
			System.out.println("");
			System.out.println("Contact the author if you believe this is a bug..");
        }
		boolean init = false;
		if (json.containsKey("LastServer") == false) {
			json.put("LastServer", "localhost:1720");
			init = true;
		}
		
		if (init) {
			save();
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static boolean save() {
        try {
        	JSONObject toSave = json;
        
        	TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        	treeMap.putAll(toSave);
        	
        	Gson g = new GsonBuilder().setPrettyPrinting().create();
            String prettyJsonString = g.toJson(treeMap);
            
            PrintWriter clear = new PrintWriter(file);
            clear.print("");
            clear.close();
            
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write(prettyJsonString);
            writer.flush();
            writer.close();

            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
	
	public static String getLastServer() {
		return (String) json.get("LastServer");
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static void setLastServer(String host) {
		json.put("LastServer", host);
		save();
	}

}
