package src.main.java.de.haw_hamburg.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A chatroom has a name and manages a list of clients.
 * @author Karsten
 */
public class Chatroom {
	private List<ClientConnection> clientList;
	private String name;

	/**
	 * Create a new chatroom with the given name.
	 * @param name: Name of the chatroom.
	 */
	public Chatroom(String name) {
		clientList = Collections.synchronizedList(new ArrayList<>());
		this.name = name;
	}
	
	/**
	 * Lets a client enter the chatroom.
	 * @param clientConnection: Entering client.
	 */
	public void enter(ClientConnection clientConnection) {
		synchronized (clientList) {
			clientList.add(clientConnection);
		}
	}
	
	/**
	 * Lets a client leave the chatroom.
	 * @param clientConnection: Leaving client.
	 */
	public void leave(ClientConnection clientConnection) {
		synchronized (clientList) {
			clientList.remove(clientConnection);
			if (clientList.isEmpty()) {
				ApplicationServer.chatrooms.remove(name);
			}
		}
	}

	/**
	 * Returns the name of the chatroom.
	 * @return String name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a list of clients of this chatroom.
	 * @return List of clients.
	 */
	public List<ClientConnection> getClientList() {
		return clientList;
	}
}
