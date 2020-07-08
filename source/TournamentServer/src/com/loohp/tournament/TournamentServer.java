package com.loohp.tournament;

import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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
import com.loohp.tournament.Utils.RoundCheck;
import com.loohp.tournament.Web.Web;

public class TournamentServer {
	
	public static BufferedReader in;
	public static File DataFolder = new File("configs");
	
	public static List<Player> playerList = new CopyOnWriteArrayList<Player>();
	
	public static Optional<Competition> activeCompetition = Optional.empty();
	
	public static PrintStream stdout = null;
	
	public static boolean GUIrunning = true;
	public static boolean loadServer = true;
	public static boolean loadWeb = true;
	public static int serverPort = 1720;
	public static int webPort = 8080;
	
	public static void main(String args[]) {
		
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
		Lang.load();
		
		IO.writeLn("Loading database..");
		Database.loadDatabase();
		TournamentServer.playerList = Database.getPlayers();
		TournamentServer.activeCompetition = Database.loadCompetition();
		IO.writeLn(Lang.getLang("StartUp.LoadedPlayers").replace("%s", TournamentServer.playerList.size() + ""));
		
		if (loadServer) {
			if (!NetworkUtils.available(serverPort)) {
				IO.writeLn("");
				IO.writeLn("[ERROR] *****FAILED TO BIND PORT " + serverPort + "*****");
				IO.writeLn("[ERROR] *****PORT ALREADY IN USE*****");
				IO.writeLn("[ERROR] *****PERHAPS ANOTHER INSTANCE OF THE SERVER IS ALREADY RUNNING?*****");
				IO.writeLn("");
				Exit.exit(1);
			}
			Thread t3 = new Thread(new Runnable() {
			    @Override
			    public void run() {
			       	Server.start();
			    }
			});  
			t3.start();
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
		
		long doneunixtime = System.currentTimeMillis();		
		IO.writeLn("Done! (" + (doneunixtime - startunixtime) + "ms)");
		IO.writeLn(Lang.getLang("StartUp.CurrentTime").replace("%s", new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss' 'zzz").format(new Date())));
		
		try {TimeUnit.MILLISECONDS.sleep(500);} catch (InterruptedException e) {}
		
		while (true) {
			try {
				System.setOut(stdout);
				
				try {TimeUnit.MILLISECONDS.sleep(50);} catch (InterruptedException e) {}
				IO.write("> ");
				
				String[] inp = IO.readLn();
				
				if (inp.length > 0) {				
					if (inp[0].equalsIgnoreCase("exit") || inp[0].equalsIgnoreCase("stop")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Exit.Usage"));
						} else {
							Exit.exit();
						}
						
					} else if (inp[0].equalsIgnoreCase("import")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Import.Usage"));
						} else {
							Import.onImport(inp);
						}
						
					} else if (inp[0].equalsIgnoreCase("list")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.List.Usage"));
						} else {
							ListPlayer.listPlayers(inp);
						}
						
					} else if (inp[0].equalsIgnoreCase("start")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Start.Usage"));
						} else {
							Start.startCompetition();
							RoundCheck.check();
						}
						
					} else if (inp[0].equalsIgnoreCase("promote")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Promote.Usage"));
						} else {
							Promotion.promote(inp);
							RoundCheck.check();
						}
						
					} else if (inp[0].equalsIgnoreCase("restart")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Restart.Usage"));
						} else {
							Restart.restart(inp);
							RoundCheck.check();
						}
						
					} else if (inp[0].equalsIgnoreCase("unpromote") || inp[0].toLowerCase().equalsIgnoreCase("demote")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Unpromote.Usage"));
						} else {
							Unpromote.undo(inp);
							RoundCheck.check();
						}
						
					} else if (inp[0].equalsIgnoreCase("report")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Report.Usage"));
						} else {
							Report.generate(inp);
						}
						
					} else if (inp[0].equalsIgnoreCase("find")) {
						if (Arrays.asList(inp).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
							IO.writeLn(Lang.getLang("Commands.Find.Usage"));
						} else {
							Find.findPlayer(inp);
						}
						
					} else if (inp[0].equalsIgnoreCase("help")) {
						IO.writeLn("");
						IO.writeLn(Lang.getLang("Commands.Help.Header"));
						IO.writeF("    %-15s    ", "import");
						IO.writeLn(Lang.getLang("Commands.Import.Description"));
						IO.writeF("    %-15s    ", "list");
						IO.writeLn(Lang.getLang("Commands.List.Description"));
						IO.writeF("    %-15s    ", "start");
						IO.writeLn(Lang.getLang("Commands.Start.Description"));
						IO.writeF("    %-15s    ", "restart");
						IO.writeLn(Lang.getLang("Commands.Restart.Description"));
						IO.writeF("    %-15s    ", "promote");
						IO.writeLn(Lang.getLang("Commands.Promote.Description"));
						IO.writeF("    %-15s    ", "unpromote");
						IO.writeLn(Lang.getLang("Commands.Unpromote.Description"));
						IO.writeF("    %-15s    ", "find");
						IO.writeLn(Lang.getLang("Commands.Find.Description"));
						IO.writeF("    %-15s    ", "report");
						IO.writeLn(Lang.getLang("Commands.Report.Description"));
						IO.writeF("    %-15s    ", "exit");
						IO.writeLn(Lang.getLang("Commands.Exit.Description"));
						IO.writeF("    %-15s    ", "help");
						IO.writeLn(Lang.getLang("Commands.Help.Description"));
						IO.writeLn("");
						
					} else if (!inp[0].equalsIgnoreCase("")) {
						IO.writeLn(Lang.getLang("Common.UnknownCommand"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}				
	}
}
