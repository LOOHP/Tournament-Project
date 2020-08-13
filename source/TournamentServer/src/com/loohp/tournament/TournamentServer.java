package com.loohp.tournament;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.Commands.CommandsManager;
import com.loohp.tournament.Commands.Exit;
import com.loohp.tournament.Commands.Find;
import com.loohp.tournament.Commands.Import;
import com.loohp.tournament.Commands.ListPlayer;
import com.loohp.tournament.Commands.Promotion;
import com.loohp.tournament.Commands.Report;
import com.loohp.tournament.Commands.Restart;
import com.loohp.tournament.Commands.Start;
import com.loohp.tournament.Commands.Unpromote;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Database.Database;
import com.loohp.tournament.GUI.GUI;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Server.Server;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.NetworkUtils;
import com.loohp.tournament.Web.Web;

public class TournamentServer {
	
	public static TournamentServer instance;
	
	public static void main(String args[]) {
		new TournamentServer(args);
	}
	
	public static TournamentServer getInstance() {
		return instance;
	}
	
	//==============================
	
	public BufferedReader in;
	private File DataFolder = new File("configs");

	private List<Player> playerList = new CopyOnWriteArrayList<Player>();
	
	private Optional<Competition> activeCompetition = Optional.empty();
	
	public PrintStream stdout = null;
	
	private boolean GUIrunning = true;
	private boolean loadServer = true;
	private boolean loadWeb = true;
	private int serverPort = 1720;
	private int webPort = 8080;
	
	private Lang lang;
	private Server server;

	private CommandsManager commandsManager;
	
	public TournamentServer(String args[]) {
		
		instance = this;
		
		long startunixtime = System.currentTimeMillis();		
		in = new BufferedReader(new InputStreamReader(System.in));
		stdout = System.out;
		
		if (args.length > 0) {	
			for (String flag : args) {
				if (flag.equals("--nogui")) {
					GUIrunning = false;
				} else if (flag.equals("--noserver")) {
					loadServer = false;
				} else if (flag.equals("--noweb")) {
					loadWeb = false;
				} else if (flag.startsWith("-s") || flag.startsWith("--serverport")) {
					String value = flag.substring(flag.indexOf("=") + 1);
					if (value.matches("[0-9]*")) {
						serverPort = Integer.parseInt(value);
					} else {
						System.out.println("Invalid server port: " + value);
						System.out.println("Press [enter] to quit");
						try {in.readLine();} catch (IOException e) {e.printStackTrace();}
						System.exit(2);
					}
				} else if (flag.startsWith("-w") || flag.startsWith("--webport")) {
					String value = flag.substring(flag.indexOf("=") + 1);
					if (value.matches("[0-9]*")) {
						webPort = Integer.parseInt(value);
					} else {
						System.out.println("Invalid web port: " + value);
						System.out.println("Press [enter] to quit");
						try {in.readLine();} catch (IOException e) {e.printStackTrace();}
						System.exit(2);
					}
				} else {
					System.out.println("Invalid start-up flags");
					System.out.println("Accepted flags:");
					System.out.println(" --nogui ");
					System.out.println(" --noserver");
					System.out.println(" --noweb");
					System.out.println(" --serverport -s [Example: -s=1720]");
					System.out.println(" --webport -w [Example: -w=8080]");
					System.out.println();
					System.out.println("Press [enter] to quit");
					try {in.readLine();} catch (IOException e) {e.printStackTrace();}
					System.exit(3);
				}
			}
			try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {}
		}
		
		if (GraphicsEnvironment.isHeadless()) {
			GUIrunning = false;
		}
		
		if (GUIrunning) {
			System.out.println("Launching Server GUI.. Add \"--nogui\" in launch arguments to disable");
			GUIrunning = true;
			Thread t1 = new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	GUI.main();
			    }
			});  
			t1.start();
			try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {}
		}
		
		Thread t2 = new Thread(new Runnable() {
		    @Override
		    public void run() {
				IO.run();
		    }
		});  
		t2.start();
    	
		IO.writeLn("Starting Tournament Server..");
		Import.initPlayersFile();
		
		IO.writeLn("Loading language..");
		lang = new Lang(DataFolder);
		
		IO.writeLn("Loading database..");
		Database.loadDatabase();
		playerList = Database.getPlayers();
		activeCompetition = Database.loadCompetition();
		IO.writeLn(lang.get("StartUp.LoadedPlayers").replace("%s", playerList.size() + ""));
		
		if (loadServer) {
			if (!NetworkUtils.available(serverPort)) {
				IO.writeLn("");
				IO.writeLn("[ERROR] *****FAILED TO BIND PORT " + serverPort + "*****");
				IO.writeLn("[ERROR] *****PORT ALREADY IN USE*****");
				IO.writeLn("[ERROR] *****PERHAPS ANOTHER INSTANCE OF THE SERVER IS ALREADY RUNNING?*****");
				IO.writeLn("");
				Exit.exit(1);
			}
			server = new Server(serverPort);
		}
		
		if (loadWeb) {
			if (!NetworkUtils.available(webPort)) {
				IO.writeLn("");
				IO.writeLn("[ERROR] *****FAILED TO BIND PORT" + webPort + "*****");
				IO.writeLn("[ERROR] *****PORT ALREADY IN USE*****");
				IO.writeLn("[ERROR] *****PERHAPS ANOTHER INSTANCE OF THE SERVER IS ALREADY RUNNING?*****");
				IO.writeLn("");
				Exit.exit(1);
			}
			Thread t4 = new Thread(new Runnable() {
			    @Override
			    public void run() {
			       	Web.load();
			    }
			});  
			t4.start();
			try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {}
		}
		
		commandsManager = new CommandsManager();
		commandsManager.registerCommands(new Exit());
		commandsManager.registerCommands(new Find());
		commandsManager.registerCommands(new Import());
		commandsManager.registerCommands(new ListPlayer());
		commandsManager.registerCommands(new Promotion());
		commandsManager.registerCommands(new Report());
		commandsManager.registerCommands(new Restart());
		commandsManager.registerCommands(new Start());
		commandsManager.registerCommands(new Unpromote());
		
		long doneunixtime = System.currentTimeMillis();		
		IO.writeLn("Done! (" + (doneunixtime - startunixtime) + "ms)");
		IO.writeLn(lang.get("StartUp.CurrentTime").replace("%s", new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss' 'zzz").format(new Date())));
		
		try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {}
		
		while (true) {
			try {
				System.setOut(stdout);
				
				try {TimeUnit.MILLISECONDS.sleep(50);} catch (InterruptedException e) {}
				IO.write("> ");
				
				String[] inp = IO.readLn();
				
				if (inp.length > 0) {				
					if (inp[0].equalsIgnoreCase("help")) {
						IO.writeLn("");
						IO.writeLn(lang.get("Commands.Help.Header"));
						IO.writeF("    %-15s    ", "import");
						IO.writeLn(lang.get("Commands.Import.Description"));
						IO.writeF("    %-15s    ", "list");
						IO.writeLn(lang.get("Commands.List.Description"));
						IO.writeF("    %-15s    ", "start");
						IO.writeLn(lang.get("Commands.Start.Description"));
						IO.writeF("    %-15s    ", "restart");
						IO.writeLn(lang.get("Commands.Restart.Description"));
						IO.writeF("    %-15s    ", "promote");
						IO.writeLn(lang.get("Commands.Promote.Description"));
						IO.writeF("    %-15s    ", "unpromote");
						IO.writeLn(lang.get("Commands.Unpromote.Description"));
						IO.writeF("    %-15s    ", "find");
						IO.writeLn(lang.get("Commands.Find.Description"));
						IO.writeF("    %-15s    ", "report");
						IO.writeLn(lang.get("Commands.Report.Description"));
						IO.writeF("    %-15s    ", "exit");
						IO.writeLn(lang.get("Commands.Exit.Description"));
						IO.writeF("    %-15s    ", "help");
						IO.writeLn(lang.get("Commands.Help.Description"));
						IO.writeLn("");						
					//} else if (!inp[0].equalsIgnoreCase("")) {
					//	IO.writeLn(lang.get("Common.UnknownCommand"));
					} else {
						commandsManager.fireExecutors(inp);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}				
	}
	
	public CommandsManager getCommandsManager() {
		return commandsManager;
	}
	
	public Server getServer() {
		return server;
	}

	public Lang getLang() {
		return lang;
	}

	public File getDataFolder() {
		return DataFolder;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public Competition getActiveCompetition() {
		return activeCompetition.orElse(null);
	}
	
	public void setActiveCompetition(Competition competition) {
		activeCompetition = Optional.ofNullable(competition);
	}
	
	public boolean hasActiveCompetition() {
		return activeCompetition.isPresent();
	}

	public boolean isGUIrunning() {
		return GUIrunning;
	}

	public boolean isLoadServer() {
		return loadServer;
	}

	public boolean isLoadWeb() {
		return loadWeb;
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getWebPort() {
		return webPort;
	}
}
