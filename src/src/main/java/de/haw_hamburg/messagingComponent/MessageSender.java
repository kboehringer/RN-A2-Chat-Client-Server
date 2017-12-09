package src.main.java.de.haw_hamburg.messagingComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import src.main.java.de.haw_hamburg.server.ApplicationServer;

public class MessageSender extends Thread {
	private final LinkedList<String> messageList;
	private PrintWriter messageOut;

	public MessageSender(PrintWriter messageOut) throws IOException {
		this.messageList = new LinkedList<>();
		start();
	}
	
	public void addMessage(String message) {
		synchronized (messageList) {
			messageList.push(message);
		}
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			synchronized (messageList) {
				if (!messageList.isEmpty()) {
					messageOut.println(messageList.pop());
					messageOut.flush();
				}
			}
		}
		messageOut.flush();
	}
}
