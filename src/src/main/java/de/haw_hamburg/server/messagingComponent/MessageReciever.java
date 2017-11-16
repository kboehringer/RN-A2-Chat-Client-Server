package src.main.java.de.haw_hamburg.server.messagingComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import src.main.java.de.haw_hamburg.server.ApplicationServer;

public class MessageReciever extends Thread{

	private ServerSocket serverSocket;
	
	public MessageReciever() throws IOException {
		serverSocket = new ServerSocket(ApplicationServer.port);
	}
	
	@Override
	public void run() {
		Socket client = null;
		while (!isInterrupted()) {
			try {
				client = serverSocket.accept();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			client = null;
		}
	}
}
