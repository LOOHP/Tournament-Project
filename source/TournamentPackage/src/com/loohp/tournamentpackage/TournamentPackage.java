package com.loohp.tournamentpackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
		
		File outFolder = new File("builds");
		
		String url = "https://raw.githubusercontent.com/LOOHP/Tournament-Project/master/builds/";
		
		List<String> list = new ArrayList<String>();
		list.add("TournamentServer.jar");
		list.add("TournamentClient.jar");
		list.add("TournamentClient.apk");
		
		for (String resourceName : list) {
			try {
	        	if (!outFolder.exists()) {
	        		outFolder.mkdir();
	        	}
	        	
	        	String link = url + resourceName;
	        	File output = new File(outFolder, resourceName);
	        	
	        	if (!downloadFile(output, new URL(link))) {
	        		InputStream stream = TournamentPackage.class.getClassLoader().getResourceAsStream(resourceName);
		        	OutputStream resStreamOut = new FileOutputStream(output);	
		            int readBytes;
		            byte[] buffer = new byte[4096];
		            while ((readBytes = stream.read(buffer)) > 0) {
		                resStreamOut.write(buffer, 0, readBytes);
		            }
		            stream.close();
		            resStreamOut.close();
	        	}
	        	
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		
		String str = "Files had been built and placed into the \"builds\" folder!";
		JOptionPane.showMessageDialog(null, str);
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
