package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contract {
	private static final Logger log = Logger.getLogger( ApplicationServer.class.getName());
	private static boolean logging = false;

	public static void createLogger(boolean setLogging) {
		logging = setLogging;
		try {
			Handler handler = new FileHandler( LocalDateTime.now().toString().replaceAll(":", "-") + " log.xml" );
			log.addHandler(handler);
		} catch (SecurityException | IOException e) {
			StringBuilder stack = new StringBuilder();
			for (StackTraceElement trace : e.getStackTrace()) {
				stack.append(trace.toString());
			}
			log.log(Level.WARNING, e.getMessage() + stack);
		}
	}

	public static void checkNull(Object object) {
		if (object == null) {
			throw new NullPointerException("Object is null!");
		}
	}
	
	public static void LogInfo(String info) {
		if (logging) {
			log.log(Level.INFO, info);
		}
	}
	
	public static void logException(Exception e) {
		if (logging) {
			StringBuilder stack = new StringBuilder();
			for (StackTraceElement trace : e.getStackTrace()) {
				stack.append(trace.toString());
			}
			log.log(Level.WARNING, e.getMessage() + "\n" + stack);
		}
	}
}
