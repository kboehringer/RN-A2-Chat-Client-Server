package src.main.java.de.haw_hamburg.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.main.java.de.haw_hamburg.Contract;

public class ApplicationServer {
	public final static int port = 80;
	public static List<Chatroom> chatrooms = Collections.synchronizedList(new ArrayList<Chatroom>());

	public static void main(String[] args) {
		Contract.createLogger();
		ConnectionListener connectionListener = new ConnectionListener();
	}
}
