package src.main.java.de.haw_hamburg.messagingComponent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import src.main.java.de.haw_hamburg.Contract;
import src.main.java.de.haw_hamburg.serverComponent.ApplicationServer;

public class RechnernetzMessageProtocol {

	private MessageSender messageSender;
	private MessageReceiver messageReceiver;
	private boolean connected = false;
	private final String connectionEstablishing = "CONNECT+";
	private ArrayList<MessageSender> messageSenders;
	private ArrayList<MessageReceiver> messageReceivers;
	private ArrayList<MessageHandler> incommingMessageHandler;
//	private ScheduledExecutorService receiversExecutor;

	public RechnernetzMessageProtocol() {
		messageSenders = new ArrayList<>();
		messageReceivers = new ArrayList<>();
		incommingMessageHandler = new ArrayList<>();
//		receiversExecutor = Executors.newScheduledThreadPool(10);
	}

	public void addConnection(String address, String userName, MessageHandler messageHandler) {
		try {
			Socket socket = new Socket(address, ApplicationServer.port);
			messageSenders.add(new MessageSender(new PrintWriter(socket.getOutputStream(), false)));
			messageReceivers.add(new MessageReceiver(socket.getInputStream()));
			incommingMessageHandler.add(messageHandler);
		} catch (IOException e) {
			Contract.logException(e);
		}
	}

	public void sendMessage(String message) throws UnknownHostException, IOException {
		for (MessageSender sender : messageSenders) {
			if (sender.isAlive()) {
				sender.addMessage(message);
			}
		}
	}

	public void receiveMessage(String message) {
		String command = message.substring(0, 3);
		switch (command) {
		case "":
			
			break;

		default:
			break;
		}
	}
}
