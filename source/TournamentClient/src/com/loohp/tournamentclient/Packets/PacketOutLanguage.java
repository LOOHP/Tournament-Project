package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import com.loohp.tournamentclient.Utils.CustomHashMapUtils;
import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketOutLanguage extends PacketOut {
	
	public final int packetId = 0x04;
	
	public static int getPacketId() {
		return new PacketOutLanguage().packetId;
	}
	
	private PacketOutLanguage() {}
	
	private HashMap<String, String> language;
	
	public PacketOutLanguage(HashMap<String, String> language) {
		this.language = language;
	}
	
	@SuppressWarnings("unchecked")
	public PacketOutLanguage(DataInputStream input) throws IOException {
		language = (HashMap<String, String>) CustomHashMapUtils.deserialize(DataTypeIO.readString(input));
	}
	
	public HashMap<String, String> getLanguage() {
		return language;
	}

}
