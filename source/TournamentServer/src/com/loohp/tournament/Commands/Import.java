package com.loohp.tournament.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.CustomStringUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.OrdinalUtils;

public class Import implements CommandExecutor {
	
	public void importFromTerminal(boolean replace) {
		if (replace) {
			TournamentServer.getInstance().getPlayerList().clear();
		}
		
		List<Player> addedPlayers = new ArrayList<Player>();
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Begin"));
		
		for (int i = 1; i > 0; i++) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Name").replace("%s", OrdinalUtils.getOrdinal(TournamentServer.getInstance().getPlayerList().size() + addedPlayers.size() + 1)));
			IO.write("> ");
			String name = String.join(" ", IO.readLn());
			if (name.equalsIgnoreCase("undo")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Undo"));
				continue;
			} else if (name.equalsIgnoreCase("done")) {
				break;
			} else if (name.equalsIgnoreCase("cancel")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.School").replace("%s", name));
			IO.write("> ");
			String school = String.join(" ", IO.readLn());
			if (school.equalsIgnoreCase("undo")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Undo"));
				continue;
			} else if (school.equalsIgnoreCase("cancel")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			String inp = "";
			do {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Seeded").replace("%s", name));
				IO.write("> ");
				inp = String.join(" ", IO.readLn());
			} while (!inp.equalsIgnoreCase("true") && !inp.equalsIgnoreCase("false") && !inp.equalsIgnoreCase("undo") && !inp.equalsIgnoreCase("cancel"));
			if (inp.equalsIgnoreCase("undo")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Undo"));
				continue;
			} else if (inp.equalsIgnoreCase("cancel")) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Cancel"));
				return;
			}
			
			boolean seeded = Boolean.valueOf(inp);
			
			Player player = new Player(UUID.randomUUID(), name, school, seeded);
			addedPlayers.add(player);
		}
		
		IO.writeLn("========================================");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Finishing"));
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Imported").replace("%s", addedPlayers.size() + ""));
		TournamentServer.getInstance().getPlayerList().addAll(addedPlayers);
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Terminal.Total").replace("%s", TournamentServer.getInstance().getPlayerList().size() + ""));
	}
	
	public static void initPlayersFile() {
		String fileName = "players.yml";
		TournamentServer.getInstance().getDataFolder().mkdirs();
        File file = new File(TournamentServer.getInstance().getDataFolder(), fileName);
        if (!file.exists()) {
        	IO.writeLn("Initial Start Detected, Creating players.yml");
        	try (InputStream in = TournamentServer.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(in, file.toPath());
                IO.writeLn("Created players.yml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }           
	}
	
	public void importFromFile(boolean replace) {
		long start = System.currentTimeMillis();
		if (replace) {
			TournamentServer.getInstance().getPlayerList().clear();
		}
		
		List<Player> addedPlayers = new ArrayList<Player>();
		try {
			
			Yaml yaml = new Yaml();
			String fileName = "players.yml";			
	        File file = new File(TournamentServer.getInstance().getDataFolder(), fileName);	    
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
			
			TournamentServer.getInstance().getPlayerList().addAll(addedPlayers);
			long end = System.currentTimeMillis();
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.File.Complete").replace("%s", "(" + (end - start) + "ms)"));
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.File.Imported").replace("%s", addedPlayers.size() + ""));
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.File.Total").replace("%s", TournamentServer.getInstance().getPlayerList().size() + ""));
			
		} catch (Exception e) {
			IO.writeLn("Error while parsing players.yml: You might want to check the syntax on the lines below");

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());
			
			IO.writeLn("");
			IO.writeLn("You can always regenerate a new one by removing the file from its location");
		}
	}
	
	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("import")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Usage"));
			} else {
				if (args.length < 2) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Usage"));
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
				
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Import.Usage"));
			}
		}
	}
	
}
