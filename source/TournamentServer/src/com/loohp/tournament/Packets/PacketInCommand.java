package com.loohp.tournament.Packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.loohp.tournament.Utils.DataTypeIO;

public class PacketInCommand extends PacketIn {
	
	public final int packetId = 0x00;
	
	public static int getPacketId() {
		return new PacketInCommand().packetId;
	}
	
	private PacketInCommand() {}
	
	private String text;
	
	public PacketInCommand(String text) {
		this.text = text;
	}
	
	public PacketInCommand(DataInputStream in) throws IOException {
		this.text = DataTypeIO.readString(in, StandardCharsets.UTF_8);
	}
	
	public String getText() {
		return text;
	}

}
