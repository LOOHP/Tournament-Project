package com.loohp.tournament.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.GUI.ConsoleTextOutput;
import com.loohp.tournament.Server.Connection;

public class IO {
	
	public static Optional<String[]> input = Optional.empty();
	private static HashMap<Long, Boolean> done = new HashMap<Long, Boolean>();
	
	public static void run() {
		while (TournamentServer.getInstance().in != null) {
			try {
				String cmd = TournamentServer.getInstance().in.readLine();
				input = Optional.of(CustomStringUtils.splitStringToArgs(cmd));
				if (TournamentServer.getInstance().stdout.equals(System.out)) {
					ConsoleTextOutput.appendText(cmd + "\n");
					if (TournamentServer.getInstance().getServer() != null) {
						for (Connection sc : TournamentServer.getInstance().getServer().getClients()) {
							sc.send(cmd + "\n");
						}
					}
				}
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (IOException | InterruptedException e) {
				TournamentServer.getInstance().in = null;		
			}
		}
	}
	
	public static void writeLn(String string) {
		final long unix = System.nanoTime();
		done.put(unix, false);
		new Thread(new Runnable() {
		    @Override
		    public void run() {
				System.out.println(string);
				if (TournamentServer.getInstance().stdout.equals(System.out)) {
					ConsoleTextOutput.appendText(string + "\n");
					if (TournamentServer.getInstance().getServer() != null) {
						for (Connection sc : TournamentServer.getInstance().getServer().getClients()) {
							sc.send(string + "\n");
						}
					}
				}
				done.put(unix, true);
		    }
		}).start();
		while (done.get(unix) == false) {
			try {TimeUnit.NANOSECONDS.sleep(10);} catch (InterruptedException e) {}
		}
		done.remove(unix);
	}
	
	public static void writeF(String format, Object... args) {
		final long unix = System.nanoTime();
		done.put(unix, false);
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	System.out.printf(format, args);
				if (TournamentServer.getInstance().stdout.equals(System.out)) {
					ConsoleTextOutput.appendText(String.format(format, args));
					for (Connection sc : TournamentServer.getInstance().getServer().getClients()) {
						String formatted = String.format(format, args);
						sc.send(formatted);
					}
				}
				done.put(unix, true);
		    }
		}).start();
		while (done.get(unix) == false) {
			try {TimeUnit.NANOSECONDS.sleep(10);} catch (InterruptedException e) {}
		}
		done.remove(unix);
	}
	
	public static void write(String string) {
		final long unix = System.nanoTime();
		done.put(unix, false);
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	System.out.print(string);
				if (TournamentServer.getInstance().stdout.equals(System.out)) {
					ConsoleTextOutput.appendText(string);
					for (Connection sc : TournamentServer.getInstance().getServer().getClients()) {
						sc.send(string);
					}
				}
				done.put(unix, true);
		    }
		}).start();
		while (done.get(unix) == false) {
			try {TimeUnit.NANOSECONDS.sleep(10);} catch (InterruptedException e) {}
		}
		done.remove(unix);
	}
	
	public static String[] readLn() {
		String[] inp = null;
		do {
			try {
				if (input.isPresent()) {
					inp = input.get().clone();
					TimeUnit.MILLISECONDS.sleep(50);
					input = Optional.empty();
				}
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				IO.writeLn(errors.toString());			
			}
		} while (inp == null);
		return inp;
	}
}
