package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A ConnectionListener listens for arriving connections. Every Instance of this
 * is an own Thread.
 * @author Karsten
 */
public class ConnectionListener extends Thread {
	private ServerSocket serverSocket;
	private ArrayList<ClientConnection> clientConnections;

	/**
	 * Creates a new ConnectionListener-Thread. The Thread starts automaticly.
	 */
	public ConnectionListener() {
		try {
			serverSocket = new ServerSocket(ApplicationServer.port);
		} catch (IOException e) {
			Contract.logException(e);
		}
		clientConnections = new ArrayList<>();
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
				clientConnections.add(clientConnection);
			} catch (IOException e) {
				Contract.logException(e);
			}
		}
		for (ClientConnection clientConnection : clientConnections) {
			clientConnection.interrupt();
		}
	}
}
