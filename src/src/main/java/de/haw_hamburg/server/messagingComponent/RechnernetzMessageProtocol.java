package src.main.java.de.haw_hamburg.server.messagingComponent;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import src.main.java.de.haw_hamburg.server.ApplicationServer;

public class RechnernetzMessageProtocol {

	private Socket socket;
	private MessageSender messageSender;
	private MessageReceiver messageReceiver;
	private boolean connected = false;
	private final String connectionEstablishing = "CONNECT+";
	

	public RechnernetzMessageProtocol(String address, String userName) throws UnknownHostException, IOException {
		socket = new Socket(address, ApplicationServer.port);
		messageSender = new MessageSender(new PrintWriter(socket.getOutputStream(), false));
		messageReceiver = new MessageReceiver(socket.getInputStream());
		sendMessage(connectionEstablishing + userName);
	}

	public void sendMessage(String message) throws UnknownHostException, IOException {
		if (socket.isConnected()) {
			messageSender.addMessage(message);
		}
	}

}
