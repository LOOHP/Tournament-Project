package com.loohp.tournamentclient.Packets;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketOutRoundNumber extends PacketOut {
	
	public final int packetId = 0x03;
	
	public static int getPacketId() {
		return new PacketOutRoundNumber().packetId;
	}
	
	private PacketOutRoundNumber() {}
	
	private int round;
	
	public PacketOutRoundNumber(int round) {
		this.round = round;
	}
	
	public PacketOutRoundNumber(DataInputStream input) throws IOException {
		round = input.readInt();
	}
	
	public int getRound() {
		return round;
	}

}
