package com.loohp.tournament.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.loohp.tournament.Utils.DataTypeIO;

public class PacketOutLanguage extends PacketOut {
	
	public static int packetId = 0x04;
	
	public int getPacketId() {
		return packetId;
	}
	
	private HashMap<String, String> language;
	
	public PacketOutLanguage(HashMap<String, String> language) {
		this.language = language;
	}
	
	public HashMap<String, String> getLanguage() {
		return language;
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(packetId);
		DataTypeIO.writeStringMapping(output, language, StandardCharsets.UTF_8);
		
		return buffer.toByteArray();
	}

}
