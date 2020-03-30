package com.loohp.tournament.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.CustomStringUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.OrdinalUtils;

public class Import {
	
	public static File DataFolder = Tournament.DataFolder;
	
	public static void onImport(String[] args) {
		
		if (args.length < 2) {
			IO.writeLn(Lang.getLang("Commands.Import.Usage"));
			return;
		}
		
		boolean replace = false;
		if (CustomStringUtils.arrayContains("-r", args) || CustomStringUtils.arrayContains("--replace", args)) {
			replace = true;
		}
		
		if (args[1].equalsIgnoreCase("file")) {
			importFromFile(replace);
			return;
		} else if (args[1].equalsIgnoreCase("terminal")) {
			importFromTerminal(replace);
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.Import.Usage"));
	}
	
	public static void importFromFile(boolean replace) {
		long start = System.currentTimeMillis();
		if (replace) {
			Tournament.playerList.clear();
		}
		
		List<Player> addedPlayers = new ArrayList<Player>();
		try {
			
			Yaml yaml = new Yaml();
			String fileName = "players.yml";			
	        File file = new File(DataFolder, fileName);	    
			InputStream inputStream = new FileInputStream(file);
			HashMap<String, HashMap<String, Object>> obj = yaml.load(inputStream);
			
			for (Entry<String, HashMap<String, Object>> entry : obj.entrySet()) {
				HashMap<String, Object> data = entry.getValue();
				String name = (String) data.get("Name");
				String school = (String) data.get("School");
				boolean seeded = (boolean) data.get("Seeded");
				Player player = new Player(UUID.randomUUID(), name, school, seeded);
				addedPlayers.add(player);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Tournament.playerList.addAll(addedPlayers);
		long end = System.currentTimeMillis();
		IO.writeLn(Lang.getLang("Commands.Import.File.Complete").replace("%s", "(" + (end - start) + "ms)"));
		IO.writeLn(Lang.getLang("Commands.Import.File.Imported").replace("%s", addedPlayers.size() + ""));
		IO.writeLn(Lang.getLang("Commands.Import.File.Total").replace("%s", Tournament.playerList.size() + ""));
	}
	
	public static void importFromTerminal(boolean replace) {
		if (replace) {
			Tournament.playerList.clear();
		}
		
		List<Player> addedPlayers = new ArrayList<Player>();
		IO.writeLn(Lang.getLang("Commands.Import.Terminal.Begin"));
		
		for (int i = 1; i > 0; i++) {
			IO.writeLn(Lang.getLang("Commands.Import.Terminal.Name").replace("%s", OrdinalUtils.getOrdinal(Tournament.playerList.size() + addedPlayers.size() + 1)));
			IO.write(">");
			String name = String.join(" ", IO.readLn());
			if (name.equalsIgnoreCase("undo")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Undo"));
				continue;
			} else if (name.equalsIgnoreCase("done")) {
				break;
			} else if (name.equalsIgnoreCase("cancel")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			IO.writeLn(Lang.getLang("Commands.Import.Terminal.School").replace("%s", name));
			IO.write(">");
			String school = String.join(" ", IO.readLn());
			if (school.equalsIgnoreCase("undo")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Undo"));
				continue;
			} else if (school.equalsIgnoreCase("cancel")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			String inp = "";
			do {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Seeded").replace("%s", name));
				IO.write(">");
				inp = String.join(" ", IO.readLn());
			} while (!inp.equalsIgnoreCase("true") && !inp.equalsIgnoreCase("false") && !inp.equalsIgnoreCase("undo") && !inp.equalsIgnoreCase("cancel"));
			if (inp.equalsIgnoreCase("undo")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Undo"));
				continue;
			} else if (inp.equalsIgnoreCase("cancel")) {
				IO.writeLn(Lang.getLang("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			boolean seeded = Boolean.valueOf(inp);
			
			Player player = new Player(UUID.randomUUID(), name, school, seeded);
			addedPlayers.add(player);
		}
		
		IO.writeLn("========================================");
		IO.writeLn(Lang.getLang("Commands.Import.Terminal.Finishing"));
		IO.writeLn(Lang.getLang("Commands.Import.Terminal.Imported").replace("%s", addedPlayers.size() + ""));
		Tournament.playerList.addAll(addedPlayers);
		IO.writeLn(Lang.getLang("Commands.Import.Terminal.Total").replace("%s", Tournament.playerList.size() + ""));
	}
	
}
