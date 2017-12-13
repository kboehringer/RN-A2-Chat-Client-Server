package src.main.java.de.haw_hamburg.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationServer {
	public final static int port = 55533;
//	public static List<Chatroom> chatrooms = Collections.synchronizedList(new ArrayList<Chatroom>());
	public static Map<String, Chatroom> chatrooms = Collections.synchronizedMap(new HashMap<String, Chatroom>());

	public static void main(String[] args) {
		if (args != null && args.length > 0 && args[0].equals("log")) {
			Contract.createLogger(true);
		}
		try {
			ConnectionListener connectionListener = new ConnectionListener();
		} catch (Exception e) {
			Contract.logException(e);
		}
		
	}
}
