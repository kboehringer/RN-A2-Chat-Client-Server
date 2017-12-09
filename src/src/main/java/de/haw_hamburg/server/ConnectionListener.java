package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionListener extends Thread {
	private ServerSocket serverSocket;
	private ArrayList<ClientConnection> clientConnections;

	public ConnectionListener() {
		try {
			serverSocket = new ServerSocket(ApplicationServer.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientConnections = new ArrayList<>();
		start();
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				ClientConnection clientConnection = new ClientConnection(socket);
				clientConnections.add(clientConnection);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (ClientConnection clientConnection : clientConnections) {
			clientConnection.interrupt();
		}
	}
}
