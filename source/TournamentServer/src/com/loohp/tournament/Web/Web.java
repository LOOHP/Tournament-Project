package com.loohp.tournament.Web;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Utils.IO;

public class Web {
	
	public static File WebFolder = new File("web");
 
    public static void load() {
    	IO.writeLn("Starting Tournament Chart Web Server..");
 
    	System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
    	System.setProperty("org.eclipse.jetty.LEVEL", "OFF");
    	
    	try {TimeUnit.MILLISECONDS.sleep(50);} catch (InterruptedException e) {}
    	
    	Thread t = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	WebManager.run();
		    }
		});  
		t.start();   	
    	
		try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {}
	    try {
	    	Server server = new Server();
	        ServerConnector connector = new ServerConnector(server);
	        connector.setPort(TournamentServer.getInstance().getWebPort());
	        server.addConnector(connector);
	        
	        IO.writeLn("Tournament Chart Web Server listening on port " + connector.getPort());
	
	        URL file = WebFolder.toURI().toURL();
	        if (file == null) {
	            throw new RuntimeException("Unable to find resource directory");
	        }
	
	        URI webRootUri = file.toURI().resolve("./").normalize();
	        System.err.println("WebRoot is " + webRootUri);
	
	        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context.setContextPath("/");
	        context.setBaseResource(Resource.newResource(webRootUri));
	        server.setHandler(context);
	
	        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
	        holderPwd.setInitParameter("dirAllowed","true");
	        context.addServlet(holderPwd,"/");
	
	        server.start();
	        server.join();	     
	    } catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			IO.writeLn(errors.toString());			
		}
    }
 
}