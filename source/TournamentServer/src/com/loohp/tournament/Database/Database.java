package com.loohp.tournament.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;

public class Database {
	
	private static File StorageFolder = new File("data");
	private static File file;
    private static JSONObject json;
    private static JSONParser parser = new JSONParser();
	
	@SuppressWarnings("unchecked")
	public static void loadDatabase() {		
		try {
			String fileName = "data.json";
			StorageFolder.mkdirs();
			file = new File(StorageFolder, fileName);
			if (!file.exists()) {
        		IO.writeLn("Initial Start Detected, Creating data.json");
        	    PrintWriter pw = new PrintWriter(file, "UTF-8");
        	    pw.print("{");
        	    pw.print("}");
        	    pw.flush();
        	    pw.close();
        	}
        	IO.writeLn("Reading database..");
        	json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		} catch (Exception e) {
			IO.writeLn("Error while loading database! Perhaps the file has been edited or is corrupted?");
			
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());
			
            IO.writeLn("");
            IO.writeLn("Contact the author if you believe this is a bug..");
            System.exit(5);
        }
		if (json.containsKey("Players") == false) {
			json.put("Players", new JSONArray());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean save() {
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

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());
			
        	return false;
        }
    }
	
	public static Optional<Competition> loadCompetition() {
		if (!json.containsKey("Competition")) {
			return Optional.empty();
		}
		JSONObject compObj = (JSONObject) json.get("Competition");
		int activeRound = (int) (long) compObj.get("ActiveRound");
		int roundNumber = (int) (long) compObj.get("TotalRounds");

		List<Group> groups = new ArrayList<Group>();
		for (int i = 0; i < Math.pow(2, roundNumber) - 1; i++) {
			groups.add(new Group(null, null, Optional.empty(), Optional.empty(), Optional.empty(), null));
		}

		JSONArray groupArray = (JSONArray) compObj.get("Groups");
		for (int i = 0; i < groupArray.size(); i++) {
			JSONObject obj = (JSONObject) groupArray.get(i);
			Player home = null;
			if (obj.get("Home").equals("_BYE_")) {
				home = new Player(UUID.randomUUID(), "_BYE_", "N/A", false);
			} else if (!obj.get("Home").equals("")) {
				home = PlayerUtils.getPlayer(UUID.fromString((String) obj.get("Home")));
			}
			Player away = null;
			if (obj.get("Away").equals("_BYE_")) {
				away = new Player(UUID.randomUUID(), "_BYE_", "N/A", false);
			} else if (!obj.get("Away").equals("")) {
				away = PlayerUtils.getPlayer(UUID.fromString((String) obj.get("Away")));
			}
			Optional<Player> winner = Optional.empty();
			if (!obj.get("Winner").equals("")) {
				winner = Optional.of(PlayerUtils.getPlayer(UUID.fromString((String) obj.get("Winner"))));
			}
			Optional<Group> nextGroup = Optional.empty();
			Optional<Integer> nextSide = Optional.empty();
			if (!obj.get("NextRoundGroup").equals("")) {
				nextGroup = Optional.of(groups.get((int) (long) obj.get("NextRoundGroup")));
				nextSide = Optional.of((int) (long) obj.get("NextRoundSide"));
			}
			Group group = groups.get(i);
			group.setHome(home);
			group.setAway(away);
			group.setWinner(winner);
			group.setNextRoundGroup(nextGroup);
			group.setNextRoundSide(nextSide);
		}

		List<Round> rounds = new ArrayList<Round>();
		int u = 0;
		for (int i = roundNumber - 1; i >= 0; i--) {
			Round round = new Round(null, i);
			List<Group> groupList = new ArrayList<Group>();
			for (int j = 0; j < Math.pow(2, i); j++) {
				groups.get(u).setRound(round);
				groupList.add(groups.get(u));
				u++;
			}
			round.setGroups(groupList);
			rounds.add(round);
		}

		Competition comp = new Competition(rounds, groups, activeRound);
		return Optional.of(comp);
	}
	
	@SuppressWarnings("unchecked")
	public static void saveCompetition(Optional<Competition> comp) {
		if (!comp.isPresent()) {
			json.remove("Competition");
			save();
			return;
		}
		
		json.put("Competition", new JSONObject());
		JSONObject compObj = (JSONObject) json.get("Competition");
		compObj.put("ActiveRound", comp.get().getActiveRound());
		compObj.put("TotalRounds", comp.get().getRounds().size());
		JSONArray groupArray = new JSONArray();
		
		List<Group> groups = comp.get().getGroups();
		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			JSONObject obj = new JSONObject();
			if (group.getHome() != null) {
				if (group.getHome().getName().equals("_BYE_")) {
					obj.put("Home", "_BYE_");
				} else {
					obj.put("Home", group.getHome().getId());
				}
			} else {
				obj.put("Home", "");
			}
			if (group.getAway() != null) {
				if (group.getAway().getName().equals("_BYE_")) {
					obj.put("Away", "_BYE_");
				} else {
					obj.put("Away", group.getAway().getId());
				}
			} else {
				obj.put("Away", "");
			}
			if (group.getWinner().isPresent()) {
				if (group.getWinner().get().getName().equals("_BYE_")) {
					obj.put("Winner", "_BYE_");
				} else {
					obj.put("Winner", group.getWinner().get().getId());
				}
			} else {
				obj.put("Winner", "");
			}
			if (group.getNextRoundGroup().isPresent()) {
				obj.put("NextRoundGroup", groups.indexOf(group.getNextRoundGroup().get()));
				obj.put("NextRoundSide", group.getNextRoundSide().get());
			} else {
				obj.put("NextRoundGroup", "");
				obj.put("NextRoundSide", "");
			}
			groupArray.add(obj);
		}
		
		compObj.put("Groups", groupArray);
		save();
	}
	
	public static List<Player> getPlayers() {
		List<Player> list = new ArrayList<Player>();
		JSONArray playersArray = (JSONArray) json.get("Players");
		for (int i = 0; i < playersArray.size(); i++) {
			JSONObject data = (JSONObject) playersArray.get(i);
			String name = (String) data.get("Name");
			String school = (String) data.get("School");
			boolean seeded = (boolean) data.get("Seeded");
			UUID id = UUID.fromString((String) data.get("UUID"));
			Player player = new Player(id, name, school, seeded);
			list.add(player);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static void savePlayer(List<Player> players) {
		JSONArray playersArray = new JSONArray();
		for (Player player : players) {
			String name = player.getName();
			String school = player.getSchool();
			boolean seeded = player.getSeeded();
			String id = player.getId().toString();
			JSONObject obj = new JSONObject();
			obj.put("Name", name);
			obj.put("School", school);
			obj.put("Seeded", seeded);
			obj.put("UUID", id);
			playersArray.add(obj);
		}
		json.put("Players", playersArray);
		save();
	}

}
