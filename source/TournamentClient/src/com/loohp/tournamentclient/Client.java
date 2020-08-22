package com.loohp.tournamentclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import com.loohp.tournamentclient.ComboBox.ListDropDownRound;
import com.loohp.tournamentclient.Functions.FinishReport;
import com.loohp.tournamentclient.Functions.GetReport;
import com.loohp.tournamentclient.Packets.PacketIn;
import com.loohp.tournamentclient.Packets.PacketOutConsole;
import com.loohp.tournamentclient.Packets.PacketOutLanguage;
import com.loohp.tournamentclient.Packets.PacketOutReportFinish;
import com.loohp.tournamentclient.Packets.PacketOutReportNormal;
import com.loohp.tournamentclient.Packets.PacketOutRoundNumber;
import com.loohp.tournamentclient.Utils.RoundUtils;
import com.loohp.tournamentclient.Utils.TextOutputUtils;

public class Client {
	
	public Socket socket;
	public String host;
	public int port;
	private DataInputStream in;		
	private DataOutputStream out;
	
	public boolean connect(String host, int port) {
		try {
			if (socket != null) {
				if (socket.isConnected()) {
					socket.close();
				}
			}
			this.host = host;
			this.port = port;
			socket = new Socket(host, port);
			TextOutputUtils.appendText("Connected to [" + host + ":" + port + "]", true);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
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
	
	public void send(PacketIn packet) {
		if (socket == null) {
			TextOutputUtils.appendText("[Error] Not connected to any tournament server!", true);
			return;
		} else if (!socket.isConnected()) {
			connect(host, port);
		}
		try {
			byte[] bytes = packet.getBytes();
			out.writeInt(bytes.length);
			out.write(bytes);
			out.flush();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			TextOutputUtils.appendText(errors.toString(), true);
		}
	}
	
	public void recieve() {
		try {
			int size = in.readInt();
			int packetId = in.readByte();
			
			if (PacketOutConsole.getPacketId() == packetId) {
				PacketOutConsole packet = new PacketOutConsole(in);
				TextOutputUtils.appendText(packet.getText());
			} else if (PacketOutLanguage.getPacketId() == packetId) {
				PacketOutLanguage packet = new PacketOutLanguage(in);
				if (TournamentClient.GUIrunning) {
					TournamentClient.getInstance().getLang().load(packet.getLanguage());
				}
			} else if (PacketOutReportFinish.getPacketId() == packetId) {
				PacketOutReportFinish packet = new PacketOutReportFinish(in);
				FinishReport.save(packet.getText());
			} else if (PacketOutReportNormal.getPacketId() == packetId) {
				PacketOutReportNormal packet = new PacketOutReportNormal(in);
				GetReport.getReport(packet.getText());
			} else if (PacketOutRoundNumber.getPacketId() == packetId) {
				PacketOutRoundNumber packet = new PacketOutRoundNumber(in);
				if (TournamentClient.GUIrunning) {
					int num = packet.getRound();
					TournamentClient.getInstance().getListDropDownBox().removeAllItems();
					int u = num - 1;
					for (int i = 0; i < num; i++) {
						ListDropDownRound round = new ListDropDownRound(i, RoundUtils.getRoundNameFromRoundNumber(u));
						TournamentClient.getInstance().getListDropDownBox().addItem(round);
						u--;
						TournamentClient.getInstance().getListDropDownBox().setSelectedIndex(0);
					}
				}
			} else {
				in.skipBytes(size - 1);
			}
			recieve();
		} catch (Exception e) {
			TextOutputUtils.appendText("\nDisconnected from [" + host + ":" + port + "]", true);
			TextOutputUtils.appendText("Reason: " + e.getLocalizedMessage(), true);
		}
	}

}
