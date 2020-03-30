package com.loohp.tournamentclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.loohp.tournamentclient.ComboBox.ListDropDownRound;
import com.loohp.tournamentclient.Functions.FinishReport;
import com.loohp.tournamentclient.Functions.GetReport;
import com.loohp.tournamentclient.Utils.ArraysUtils;
import com.loohp.tournamentclient.Utils.RoundUtils;
import com.loohp.tournamentclient.Utils.TextOutputUtils;

public class Client {
	
	public static Socket socket;
	public static String host;
	public static int port;
	
	public static boolean connect(String host, int port) {
		try {
			if (socket != null) {
				if (socket.isConnected()) {
					socket.close();
				}
			}
			Client.host = host;
			Client.port = port;
			socket = new Socket(host, port);
			TextOutputUtils.appendText("Connected to [" + host + ":" + port + "]", true);
			Thread t1 = new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	recieve();
			    }
			});  
			t1.start();
		} catch (IOException e) {
			TextOutputUtils.appendText("[Error] Failed to connect to [" + host + ":" + port + "]", true);
			return false;
		}
		return true;
	}
	
	public static void send(String args) {
		if (socket == null) {
			TextOutputUtils.appendText("[Error] Not connected to any tournament server!", true);
		} else if (!socket.isConnected()) {
			connect(host, port);
		}
		try {
			args = "<Packet>" + args + "</Packet>";
			byte[] byteArray = args.getBytes(StandardCharsets.UTF_8);
			byte[][] splitArray = ArraysUtils.splitArrayBytes(byteArray, 32768);
			
			for (byte[] each : splitArray) {
				DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
				dOut.writeInt(each.length);
				dOut.write(each);
				dOut.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void recieve() {
		try {
			boolean done = false;
			String in = "";
			byte[] byteArray = new byte[0];
			while (!done) {
				DataInputStream dIn = new DataInputStream(socket.getInputStream());		
				byte[] each = new byte[dIn.readInt()];
				dIn.readFully(each);
				byteArray = ArraysUtils.joinArrayBytes(byteArray, each);
				in = new String(byteArray, StandardCharsets.UTF_8);
				if (in.startsWith("<Packet>") && in.endsWith("</Packet>")) {
					done = true;
				}
			}
			
			in = in.replaceAll("^<Packet>", "");
			in = in.replaceAll("<\\/Packet>$","");
			
			if (in.startsWith("function:")) {
				if (in.startsWith("function:getreport")) {
					GetReport.getReport(in);
				} else if (in.startsWith("function:finishreport")) {
					FinishReport.save(in);
				} else if (in.startsWith("function:getroundnum")) {
					int num = Integer.parseInt(in.substring(in.indexOf("function:getroundnum=") + 21));
					TournamentClient.listDropDownBox.removeAllItems();
					int u = num - 1;
					for (int i = 0; i < num; i++) {
						ListDropDownRound round = new ListDropDownRound(i, RoundUtils.getRoundNameFromRoundNumber(u));
						TournamentClient.listDropDownBox.addItem(round);
						u--;
						TournamentClient.listDropDownBox.setSelectedIndex(0);
					}
				} else if (in.startsWith("function:serverlang")) {
					Lang.load(in);
				}
			} else {
				TextOutputUtils.appendText(in);
			}
			recieve();
		} catch (Exception e) {
			TextOutputUtils.appendText("Disconnected from [" + host + ":" + port + "]", true);
			TextOutputUtils.appendText(e.getLocalizedMessage(), true);
		}
	}

}
