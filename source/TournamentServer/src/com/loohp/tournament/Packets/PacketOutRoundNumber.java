package com.loohp.tournament.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOutRoundNumber extends PacketOut {
	
	public final int packetId = 0x03;
	
	public int getPacketId() {
		return packetId;
	}
	
	private int round;
	
	public PacketOutRoundNumber(int round) {
		this.round = round;
	}
	
	public int getRound() {
		return round;
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(packetId);
		output.writeInt(round);
		
		return buffer.toByteArray();
	}

}
