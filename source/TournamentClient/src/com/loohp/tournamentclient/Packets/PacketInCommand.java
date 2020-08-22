package com.loohp.tournamentclient.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.loohp.tournamentclient.Utils.DataTypeIO;

public class PacketInCommand extends PacketIn {
	
	public final int packetId = 0x00;
	
	public int getPacketId() {
		return packetId;
	}
	
	private String text;
	
	public PacketInCommand(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(packetId);
		DataTypeIO.writeString(output, text, StandardCharsets.UTF_8);
		
		return buffer.toByteArray();
	}

}
