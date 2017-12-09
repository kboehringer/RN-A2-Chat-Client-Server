package src.main.java.de.haw_hamburg.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chatroom {
	private List<ClientConnection> clientList;
	private String name;

	public Chatroom(String name) {
		clientList = Collections.synchronizedList(new ArrayList<>());
		this.name = name;
	}
	
	public void enter(ClientConnection clientConnection) {
		synchronized (clientList) {
			clientList.add(clientConnection);
		}
	}
	
	public void leave(ClientConnection clientConnection) {
		synchronized (clientList) {
			clientList.remove(clientConnection);
		}
	}

	public String getName() {
		return name;
	}
}
