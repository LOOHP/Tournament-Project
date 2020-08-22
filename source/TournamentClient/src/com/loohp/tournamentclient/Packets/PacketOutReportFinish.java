package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;

import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketOutReportFinish extends PacketOut {
	
	public final int packetId = 0x02;
	
	public static int getPacketId() {
		return new PacketOutReportFinish().packetId;
	}
	
	private PacketOutReportFinish() {}
	
	private String text;
	
	public PacketOutReportFinish(String text) {
		this.text = text;
	}
	
	public PacketOutReportFinish(DataInputStream input) throws IOException {
		text = DataTypeIO.readString(input);
	}
	
	public String getText() {
		return text;
	}

}
