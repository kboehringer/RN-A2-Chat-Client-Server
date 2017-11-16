package src.main.java.de.haw_hamburg.server.messagingComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import src.main.java.de.haw_hamburg.server.ApplicationServer;

public class MessageSender extends Thread {
	private Socket socket;
	private byte[] message;

	public MessageSender(String address, String message) throws UnknownHostException, IOException {
		socket = new Socket(address, ApplicationServer.port);
		this.message = message.getBytes();
		start();
	}
	
	@Override
	public void run() {
		try {
			OutputStream out = socket.getOutputStream();
			out.write(message);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
