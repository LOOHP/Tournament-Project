package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;

import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketOutConsole extends PacketOut {
	
	public final int packetId = 0x00;
	
	public static int getPacketId() {
		return new PacketOutConsole().packetId;
	}
	
	private PacketOutConsole() {}
	
	private String text;
	
	public PacketOutConsole(String text) {
		this.text = text;
	}
	
	public PacketOutConsole(DataInputStream input) throws IOException {
		text = DataTypeIO.readString(input);
	}
	
	public String getText() {
		return text;
	}

}
