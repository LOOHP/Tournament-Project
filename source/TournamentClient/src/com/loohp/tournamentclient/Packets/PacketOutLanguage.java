package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketOutLanguage extends PacketOut {
	
	public final int packetId = 0x04;
	
	public static int getPacketId() {
		return new PacketOutLanguage().packetId;
	}
	
	private PacketOutLanguage() {}
	
	private Map<String, String> language;
	
	public PacketOutLanguage(HashMap<String, String> language) {
		this.language = language;
	}
	
	public PacketOutLanguage(DataInputStream input) throws IOException {
		language = DataTypeIO.readStringMapping(input);
	}
	
	public Map<String, String> getLanguage() {
		return language;
	}

}
