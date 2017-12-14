package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

import src.main.java.de.haw_hamburg.Contract;

/**
 * A ConnectionListener listens for arriving connections. Every Instance of this
 * is an own Thread.
 * @author Karsten
 */
public class ConnectionListener extends Thread {
	private ServerSocket serverSocket;

	/**
	 * Creates a new ConnectionListener-Thread. The Thread starts automaticly.
	 */
	public ConnectionListener() {
		try {
			serverSocket = new ServerSocket(ApplicationServer.port);
		} catch (IOException e) {
			Contract.logException(e);
		}
		start();
	}
	
	/**
	 * This Method is called when creating the Listener.
	 */
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				ClientConnection clientConnection = new ClientConnection(socket);
			} catch (IOException e) {
				Contract.logException(e);
			}
		}
		synchronized (ApplicationServer.chatrooms) {
			Set<String> chatrooms = ApplicationServer.chatrooms.keySet();
			for (String chatroom : chatrooms) {
				ApplicationServer.chatrooms.get(chatroom).disconnect();
			}
		}
	}
}
