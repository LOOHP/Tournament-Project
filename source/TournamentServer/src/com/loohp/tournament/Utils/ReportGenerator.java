package com.loohp.tournament.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;

public class ReportGenerator {
	
	public static File ReportFolder = new File("reports");
	private static PrintStream writer = null;
	
	public static void output(Optional<String> customFileName) {
		
		String fileName = new SimpleDateFormat("yyyy'-'MM'-'dd'_'HH'-'mm'-'ss'_'zzz'.txt'").format(new Date());
		if (customFileName.isPresent()) {
			fileName = customFileName.get();
			if (FilenameUtils.getExtension(fileName).equals("")) {
				fileName = fileName + ".txt";
			}
		}
        File dir = ReportFolder;
        dir.mkdirs();
        File file = new File(dir, fileName);
        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.CreateFile").replace("%s", file.toString()));
        try {
			file.createNewFile();
		} catch (IOException e1) {
			IO.writeLn("Failed to create report file! (IOException)");
		}
        
        try {
			writer = new PrintStream(new FileOutputStream(file, true)); 
		} catch (FileNotFoundException e) {
			IO.writeLn("Failed to start print writer! (FileNotFoundException)");
		}
        
        System.setOut(writer);
             
        if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));	
		} else {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.PlayerList"));	
			IO.writeLn("===========================================================================================");
			IO.writeF("|%47s|%33s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.School"), TournamentServer.getInstance().getLang().get("Common.Seeded"));
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
				String name = player.getName();
				String school = player.getSchool();
				String seeded = String.valueOf(player.getSeeded());
				IO.writeF("|%47s|%33s|%7s|", name, school, seeded);
				IO.writeLn("");
			}
			IO.writeLn("===========================================================================================");
		}

        IO.writeLn("");
        IO.writeLn("");
        IO.writeLn("");
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));	
		} else {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.UUIDList"));	
			IO.writeLn("===========================================================================================");
			IO.writeF("|%47s|%41s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.UUID"));
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
				String name = player.getName();
				String id = player.getId().toString();
				IO.writeF("|%47s|%41s|", name, id);
				IO.writeLn("");
			}
			IO.writeLn("===========================================================================================");
		}
        
		IO.writeLn("");
        IO.writeLn("");
        IO.writeLn("");
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));	
		} else {
			Competition comp = TournamentServer.getInstance().getActiveCompetition();
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.CurrentStage").replace("%s", comp.getRounds().get(comp.getActiveRound()).getName()));	
			IO.writeLn("");
	        IO.writeLn("");
	        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Fixtures"));
			for (int i = 0; i < comp.getGroups().size(); i++) {
				Group group = comp.getGroups().get(i);
				IO.writeLn("===========================================================================================");
				String header = TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Match").replace("%s", GroupUtils.getMatchNumber(group) + " - " + RoundUtils.getRoundNameFromRoundNumber(group.getRound().getRoundNumber()));
				IO.writeF("|%-89s|", header);
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				String home = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getHome() != null) {
					home = group.getHome().getName();
				} else {
					if (GroupUtils.getHomeSideLastGroup(group) != null) {
						home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
					}
				}
				String away = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getAway() != null) {
					away = group.getAway().getName();
				} else {
					if (GroupUtils.getAwaySideLastGroup(group) != null) {
						away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
					}
				}
				if (group.getWinner().isPresent()) {
					Player winner = group.getWinner().get();
					if (winner.equals(group.getAway())) {
						away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
					} else {
						home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
					}
				}
				IO.writeF("|%-44s|%44s|", home, away);
				IO.writeLn("");
				IO.writeLn("===========================================================================================");
				IO.writeLn("");
				IO.writeLn("");				
			}
			
			IO.writeLn("");
	        IO.writeLn("");
	        IO.writeLn("");
	        
	        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Rounds"));
	        for (int i = 0; i < comp.getRounds().size(); i++) {
	        	Round round = comp.getRounds().get(i);
	        	IO.writeLn("===========================================================================================");
				IO.writeF("|%-89s|", round.getName());
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
				IO.writeLn("");
				List<Group> groups = round.getGroups();
				for (Group group : groups) {
					IO.writeLn("-------------------------------------------------------------------------------------------");
					String home = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getHome() != null) {
						home = group.getHome().getName();
					} else {
						if (GroupUtils.getHomeSideLastGroup(group) != null) {
							home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
						}
					}
					String away = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getAway() != null) {
						away = group.getAway().getName();
					} else {
						if (GroupUtils.getAwaySideLastGroup(group) != null) {
							away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
						}
					}
					if (group.getWinner().isPresent()) {
						Player winner = group.getWinner().get();
						if (winner.equals(group.getAway())) {
							away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
						} else {
							home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
						}
					}
					IO.writeF("|%-44s|%44s|", home, away);
					IO.writeLn("");
				}
				IO.writeLn("===========================================================================================");
				IO.writeLn("");
		        IO.writeLn("");
	        }
		}
		
		IO.writeLn("");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.FinalResults"));
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Ongoing"));
		
		writer.flush();
		writer.close();
		
		System.setOut(TournamentServer.getInstance().stdout);
	}
	
	public static void finish(Optional<String> customFileName, Player first, Player runner, Player runner2, Player runner3) {
		
		String fileName = new SimpleDateFormat("yyyy'-'MM'-'dd'_'HH'-'mm'-'ss'_'zzz'_Complete.txt'").format(new Date());
		if (customFileName.isPresent()) {
			fileName = customFileName.get();
			if (FilenameUtils.getExtension(fileName).equals("")) {
				fileName = fileName + ".txt";
			}
		}
        File dir = ReportFolder;
        dir.mkdirs();
        File file = new File(dir, fileName);
        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.CreateFile").replace("%s", file.toString()));
        try {
			file.createNewFile();
		} catch (IOException e1) {
			IO.writeLn("Failed to create report file! (IOException)");
		}
        
        try {
			writer = new PrintStream(new FileOutputStream(file, true)); 
		} catch (FileNotFoundException e) {
			IO.writeLn("Failed to start logging session! (FileNotFoundException)");
		}
        
        System.setOut(writer);
             
        if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));	
		} else {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.PlayerList"));	
			IO.writeLn("===========================================================================================");
			IO.writeF("|%47s|%33s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.School"), TournamentServer.getInstance().getLang().get("Common.Seeded"));
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
				String name = player.getName();
				String school = player.getSchool();
				String seeded = String.valueOf(player.getSeeded());
				IO.writeF("|%47s|%33s|%7s|", name, school, seeded);
				IO.writeLn("");
			}
			IO.writeLn("===========================================================================================");
		}

        IO.writeLn("");
        IO.writeLn("");
        IO.writeLn("");
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));	
		} else {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.UUIDList"));	
			IO.writeLn("===========================================================================================");
			IO.writeF("|%47s|%33s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.School"), TournamentServer.getInstance().getLang().get("Common.Seeded"));
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
				String name = player.getName();
				String id = player.getId().toString();
				IO.writeF("|%47s|%41s|", name, id);
				IO.writeLn("");
			}
			IO.writeLn("===========================================================================================");
		}
        
		IO.writeLn("");
        IO.writeLn("");
        IO.writeLn("");
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));	
		} else {
			Competition comp = TournamentServer.getInstance().getActiveCompetition();
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.CurrentStage").replace("%s", comp.getRounds().get(comp.getActiveRound()).getName()));	
			IO.writeLn("");
	        IO.writeLn("");
	        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Fixtures"));
			for (int i = 0; i < comp.getGroups().size(); i++) {
				Group group = comp.getGroups().get(i);
				IO.writeLn("===========================================================================================");
				String header = TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Match").replace("%s", GroupUtils.getMatchNumber(group) + " - " + RoundUtils.getRoundNameFromRoundNumber(group.getRound().getRoundNumber()));
				IO.writeF("|%-89s|", header);
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				String home = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getHome() != null) {
					home = group.getHome().getName();
				} else {
					if (GroupUtils.getHomeSideLastGroup(group) != null) {
						home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
					}
				}
				String away = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getAway() != null) {
					away = group.getAway().getName();
				} else {
					if (GroupUtils.getAwaySideLastGroup(group) != null) {
						away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
					}
				}
				if (group.getWinner().isPresent()) {
					Player winner = group.getWinner().get();
					if (winner.equals(group.getAway())) {
						away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
					} else {
						home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
					}
				}
				IO.writeF("|%-44s|%44s|", home, away);
				IO.writeLn("");
				IO.writeLn("===========================================================================================");
				IO.writeLn("");
				IO.writeLn("");				
			}
			
			IO.writeLn("");
	        IO.writeLn("");
	        IO.writeLn("");
	        
	        IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Rounds"));
	        for (int i = 0; i < comp.getRounds().size(); i++) {
	        	Round round = comp.getRounds().get(i);
	        	IO.writeLn("===========================================================================================");
				IO.writeF("|%-89s|", round.getName());
				IO.writeLn("");
				IO.writeLn("-------------------------------------------------------------------------------------------");
				IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
				IO.writeLn("");
				List<Group> groups = round.getGroups();
				for (Group group : groups) {
					IO.writeLn("-------------------------------------------------------------------------------------------");
					String home = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getHome() != null) {
						home = group.getHome().getName();
					} else {
						if (GroupUtils.getHomeSideLastGroup(group) != null) {
							home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
						}
					}
					String away = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getAway() != null) {
						away = group.getAway().getName();
					} else {
						if (GroupUtils.getAwaySideLastGroup(group) != null) {
							away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
						}
					}
					if (group.getWinner().isPresent()) {
						Player winner = group.getWinner().get();
						if (winner.equals(group.getAway())) {
							away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
						} else {
							home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
						}
					}
					IO.writeF("|%-44s|%44s|", home, away);
					IO.writeLn("");
				}
				IO.writeLn("===========================================================================================");
				IO.writeLn("");
		        IO.writeLn("");
	        }
		}
		
		IO.writeLn("");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.FinalResults"));
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Winner").replace("%s", first.getName()));
		IO.writeLn("");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.RunnerUp").replace("%s", runner.getName()));
		IO.writeLn("");
		if (runner2 != null) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.RunnerUp2").replace("%s", runner2.getName() + (runner3 != null ? ", " : "") + (runner3 != null ? runner3.getName() : "")));
		}
		
		writer.flush();
		writer.close();
		
		System.setOut(TournamentServer.getInstance().stdout);
	}
}
