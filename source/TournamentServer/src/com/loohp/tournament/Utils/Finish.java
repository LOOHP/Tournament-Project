package com.loohp.tournament.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.ClientFunctions.FunctionSendFinalReport;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Web.WebManager;
import com.loohp.tournament.Yaml.YamlOrder;

public class Finish {
	
	public static void finish() {
		try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {}
		IO.writeLn("===========================================================================================");
		IO.writeLn("===========================================================================================");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.CompetitionComplete"));
		IO.writeLn("===========================================================================================");
		List<Player> top4 = new ArrayList<Player>();
		Player first = TournamentServer.getInstance().getActiveCompetition().getGroups().get(TournamentServer.getInstance().getActiveCompetition().getGroups().size() - 1).getWinner().get();
		top4.add(first);
		Player runner = null;
		if (TournamentServer.getInstance().getActiveCompetition().getGroups().get(TournamentServer.getInstance().getActiveCompetition().getGroups().size() - 1).getHome().equals(first)) {
			runner = TournamentServer.getInstance().getActiveCompetition().getGroups().get(TournamentServer.getInstance().getActiveCompetition().getGroups().size() - 1).getAway();
		} else {
			runner = TournamentServer.getInstance().getActiveCompetition().getGroups().get(TournamentServer.getInstance().getActiveCompetition().getGroups().size() - 1).getHome();
		}
		top4.add(runner);
		Player runner2 = null;
		Player runner3 = null;
		if (TournamentServer.getInstance().getActiveCompetition().getRounds().size() > 1) {
			Round semi = TournamentServer.getInstance().getActiveCompetition().getRounds().get(TournamentServer.getInstance().getActiveCompetition().getRounds().size() - 2);
			for (Player player : RoundUtils.getPlayersInRound(semi)) {
				if (!top4.contains(player)) {
					runner2 = player;
					top4.add(player);
					break;
				}
			}
			for (Player player : RoundUtils.getPlayersInRound(semi)) {
				if (!top4.contains(player)) {
					runner3 = player;
					top4.add(player);
					break;
				}
			}
		}
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.Winner").replace("%s", first.getName()));
		IO.writeLn("");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.RunnerUp").replace("%s", runner.getName()));
		IO.writeLn("");
		if (runner2 != null) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.RunnerUp2").replace("%s", runner2.getName() + (runner3 != null ? ", " : "") + (runner3 != null ? runner3.getName() : "")));
		}
		
		IO.writeLn("");
		IO.writeLn("");
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.PrintingReport"));
		ReportGenerator.finish(Optional.empty(), first, runner, runner2, runner3);
		FunctionSendFinalReport.sendReports(first, runner, runner2, runner3);
		
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.SettingTop4Seeded"));
		for (Player player : TournamentServer.getInstance().getPlayerList()) {
			player.setSeeded(false);
		}
		for (Player player : top4) {
			player.setSeeded(true);
		}
		
		if (TournamentServer.getInstance().isLoadWeb()) {
			WebManager.updateHtml(TournamentServer.getInstance().getActiveCompetition());
		}
		TournamentServer.getInstance().setActiveCompetition(null);
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		for (int i = 0; i < TournamentServer.getInstance().getPlayerList().size(); i++) {
			Player player = TournamentServer.getInstance().getPlayerList().get(i);
			LinkedHashMap<String, Object> submap = new LinkedHashMap<String, Object>();
			submap.put("Name", player.getName());
			submap.put("School", player.getSchool());
			submap.put("Seeded", player.getSeeded());
			map.put(String.valueOf(i + 1), submap);
		}
		
		DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer customRepresenter = new Representer();
        YamlOrder customProperty = new YamlOrder();
        customRepresenter.setPropertyUtils(customProperty);
		Yaml yaml = new Yaml(customRepresenter, options);
		
		String fileName = "players.yml";
		File file = new File(TournamentServer.getInstance().getDataFolder(), fileName);	
		
		File backup = new File(TournamentServer.getInstance().getDataFolder().getPath() + "/backups", new SimpleDateFormat("'players_backup_'yyyy'-'MM'-'dd'_'HH'-'mm'-'ss'_'zzz'.yml'").format(new Date()));
		new File(TournamentServer.getInstance().getDataFolder().getPath() + "/backups").mkdir();
        try (InputStream in = new FileInputStream(file)) {
            Files.copy(in, backup.toPath());
        } catch (IOException e) {
        	IO.writeLn("Failed to backup players.yml");
        }
		
		try {
			PrintWriter pw = new PrintWriter(file, "UTF-8");
			try (BufferedReader br = new BufferedReader(new InputStreamReader(TournamentServer.class.getClassLoader().getResourceAsStream("players_header.yml"), StandardCharsets.UTF_8))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			    	pw.write(line + "\n");
			    }
			}
			yaml.dump(map, pw);
			pw.flush();
			pw.close();
			
			
		} catch (Exception e) {
			IO.writeLn("Error while writing new player.yml for next season!");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());
		}      
		
		IO.writeLn(TournamentServer.getInstance().getLang().get("Functions.Finish.Reset"));
	}
}
