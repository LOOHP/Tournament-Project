package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketOutReportNormal extends PacketOut {
	
	public final int packetId = 0x01;
	
	public static int getPacketId() {
		return new PacketOutReportNormal().packetId;
	}
	
	private PacketOutReportNormal() {}
	
	private String text;
	
	public PacketOutReportNormal(String text) {
		this.text = text;
	}	
	
	public PacketOutReportNormal(DataInputStream input) throws IOException {
		text = DataTypeIO.readString(input, StandardCharsets.UTF_8);
	}
	
	public String getText() {
		return text;
	}

}
