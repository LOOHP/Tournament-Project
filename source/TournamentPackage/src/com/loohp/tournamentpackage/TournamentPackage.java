package com.loohp.tournamentpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class TournamentPackage {
	
	public static void main(String args[]) {
		
		boolean hasConsole = System.console() != null ? true : false;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		File outFolder = new File("builds");
		
		//String rooturl = "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/";
		
		List<Resource> list = new ArrayList<Resource>();
		list.add(new Resource("TournamentServer.jar", "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/builds/TournamentServer.jar"));
		list.add(new Resource("TournamentClient.jar", "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/builds/TournamentClient.jar"));
		list.add(new Resource("TournamentClient.apk", "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/builds/TournamentClient.apk"));
		list.add(new Resource("LICENSES.txt", "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/LICENSES.txt"));
		list.add(new Resource("README.md", "https://raw.githubusercontent.com/LOOHP/TournamentProject/master/README.md"));
		
		for (Resource resource : list) {
			try {
	        	if (!outFolder.exists()) {
	        		outFolder.mkdir();
	        	}
	        	
	        	File output = new File(outFolder, resource.getName());
	        	
	        	if (hasConsole) {
	        		System.out.println("Building " + resource.getName() + "\nFrom " + resource.getUrl() + "\nTo " + output.getAbsolutePath() + "\n...");
	        	}
	        	
	        	if (!downloadFile(output, new URL(resource.getUrl()))) {
	        		InputStream stream = TournamentPackage.class.getClassLoader().getResourceAsStream(resource.getName());
		        	OutputStream resStreamOut = new FileOutputStream(output);	
		            int readBytes;
		            byte[] buffer = new byte[4096];
		            while ((readBytes = stream.read(buffer)) > 0) {
		                resStreamOut.write(buffer, 0, readBytes);
		            }
		            stream.close();
		            resStreamOut.close();
		            
		            if (hasConsole) {
		        		System.out.println("Unable to connect to github, used internal " + resource.getName() + " instead!" + "\n");
		        	}
	        	} else {
	        		if (hasConsole) {
		        		System.out.println("Finished building " + resource.getName() + "\n");
		        	}
	        	}	   
	        	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		
		String str = "Files had been built and placed into the \"builds\" folder!";
		if (hasConsole) {
			System.out.println(str);
			
			System.out.println("Press [enter] to quit");
			try {
				in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			JOptionPane.showMessageDialog(null, str);
		}
		
		System.exit(0);
	}
	
	public static boolean downloadFile(File output, URL download) {
	    try {
	        ReadableByteChannel rbc = Channels.newChannel(download.openStream());

	        FileOutputStream fos = new FileOutputStream(output);

	        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

	        fos.close();

	        return true;
	    }
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
