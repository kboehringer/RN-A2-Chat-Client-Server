package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;

import src.main.java.de.haw_hamburg.Contract;

public class ClientConnection extends Thread {
	private Socket socket;
	private Scanner input;
	private PrintWriter output;
	private String name = "";
	private String chatroomName = "";
	private final int maxNameLength = 32; //Namelength for Clients and Chatrooms
	private final char seperator = '\t';
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		try {
			this.input = new Scanner(socket.getInputStream());
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			Contract.logException(e);
		}
		start();
	}
	
	/**
	 * Runs the client, is called when creating this object.
	 */
	@Override
	public void run() {
		while (!isInterrupted()) {
			if (input.hasNextLine()) {
				handleInput(input.next());
			}
		}
		input.close();
		output.flush();
		output.close();
	}
	
	/**
	 * Sends a without control-shortcut to this client.
	 * @param message: String message.
	 */
	public void send(String message) {
		synchronized (output) {
			output.println(message + seperator);
		}
	}
	
	/**
	 * Decides what to do with the current input.
	 * @param input: String input.
	 */
	private void handleInput(String input) {
		if (input.length() < 2) {
			returnError("Send message must be at least 2 Characters long!");
		}
		int inputIndex = 2;
		String command = input.substring(0, inputIndex);
		switch (command) {
		case "NM":
			returnName(input.substring(inputIndex));
			break;
		case "GC":
			returnChatrooms();
			break;
		case "EC":
			if (!name.isEmpty()) {
				enterChatroom(input.substring(inputIndex));
			} else {
				returnError("The client has no name, so it cannot enter a chatroom!");
			}
			break;
		case "MG":
			if (!chatroomName.isEmpty()) {
				shareMessage(input.substring(inputIndex));
			} else {
				returnError("The client ist not a member of a chatroom!");
			}
			break;
		case "DC":
			disconnect();
			break;
		default:
			returnError("The command " + command + " is not valid!");
			break;
		}
	}
	
	/**
	 * Sets the name of this client. Cuts the input at a length of 32 if it is longer.
	 * @param name: String name.
	 */
	private void returnName(String name) {
		if (name.length() > maxNameLength) {
			name = name.substring(0, maxNameLength);
		}
		this.name = name;
		send("NM" + name);
	}
	
	/**
	 * Sends a list of the available chatrooms to this client.
	 */
	private void returnChatrooms() {
		String controlChar = "\t";
		StringBuilder returnMessage = new StringBuilder("LC");
		synchronized (ApplicationServer.chatrooms) {
			Set<String> chatroomNames = ApplicationServer.chatrooms.keySet();
			for (String name : chatroomNames) {
				returnMessage.append(name);
				returnMessage.append(controlChar);
			}
		}
		send(returnMessage.toString());
	}
	
	/**
	 * Enter a chatroom with this name. If there is no chatroom with this name,
	 * a new chatroom will be created.
	 * @param name: String name
	 */
	private void enterChatroom(String name) {
		if (name.length() > maxNameLength) {
			name = name.substring(0, maxNameLength);
		}
		Chatroom chatroom;
		synchronized (ApplicationServer.chatrooms) {
			Set<String> chatroomNames = ApplicationServer.chatrooms.keySet();
			if (chatroomNames.contains(name)) {
				chatroom = ApplicationServer.chatrooms.get(name);
			} else {
				chatroom = new Chatroom(name);
			}
		}
		chatroom.enter(this);
		chatroomName = name;
		send("EC" + name);
	}
	
	/**
	 * Sends an errortext to this client.
	 * @param errorText String errortext.
	 */
	private void returnError(String errorText) {
		send("ER" + errorText);
	}
	
	/**
	 * Sends a incomming message to all other clients in a chatroom.
	 * @param substring String message.
	 */
	private void shareMessage(String substring) {
		Chatroom chatroom;
		synchronized (ApplicationServer.chatrooms) {
			chatroom = ApplicationServer.chatrooms.get(chatroomName);
		}
		synchronized (chatroom.getClientList()) {
			for (ClientConnection clientConnection : chatroom.getClientList()) {
				if (!clientConnection.getName().equals(name)) {
					clientConnection.send("MG" + substring);
				}
			}
		}
	}
	
	/**
	 * Removes this client from it's chatroom and closes the Connection.
	 */
	private void disconnect() {
		Chatroom chatroom;
		synchronized (ApplicationServer.chatrooms) {
			chatroom = ApplicationServer.chatrooms.get(chatroomName);
			chatroom.leave(this);
		}
		input.close();
		output.flush();
		output.close();
		try {
			socket.close();
		} catch (IOException e) {
			Contract.logException(e);
		}
	}
}
