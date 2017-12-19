package src.main.java.de.haw_hamburg.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import src.main.java.de.haw_hamburg.Contract;

public class ClientConnection extends Thread {
	private Socket socket;
	private PrintWriter output;
	private BufferedReader inputReader;
	private String name = "";
	private String chatroomName = "";
	private final int maxNameLength = 32; //Namelength for Clients and Chatrooms
	private final char seperator = '\t';
	private final char endLine = '\n';
	private final int commandLength = 2;
	private final int maxInputSize = 1024 + commandLength + 2;
	private final List<String> commands = Arrays.asList(new String[] {"NM", "GC", "EC", "MG", "DC"});
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		System.out.println("erstelle verbindung.");
		try {
			this.output = new PrintWriter(socket.getOutputStream(), true);
			this.inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), maxInputSize);
		} catch (IOException e) {
			Contract.logException(e);
		}
		start();
	}
	
	/**
	 * Runs the client, is called when creating this object.
	 * Waits for input from a client, input can at least be 1024 characters long.
	 */
	@Override
	public void run() {
		StringBuilder inputStream = new StringBuilder(maxInputSize);
		char c = '-';
		boolean throwAway = false;
		try {
			while (!isInterrupted()) {
				inputStream = new StringBuilder(maxInputSize);
				while (inputReader.ready()) {
					c = (char) inputReader.read();
					//End reached and dont throwAway chars and at least 2 chars long (positive case)
					if (c == endLine && !throwAway && inputStream.length() >= commandLength) {
						Contract.LogInfo("input: " + inputStream);
						handleInput(inputStream);
						//Reset variable
						c = '-';
						break;
					}
					//End reached and input thrown away or input to short
					if (c == endLine && (throwAway || inputStream.length() < commandLength)) {
						//Reset variables
						c = '-';
						throwAway = false;
						break;
					}
					//Max length reached
					if (inputStream.length() >= maxInputSize) {
						handleInput(inputStream);
						inputStream = new StringBuilder(maxInputSize);
						throwAway = true;
					}
					//Check for valid command
					if (inputStream.length() == commandLength) {
						String probableCommand = inputStream.toString();
						if (!commands.contains(probableCommand)) {
							throwAway = true;
						}
					}
					if (!throwAway) {
						inputStream.append(c);
					}
				}
			}
			inputReader.close();
		} catch (IOException e) {
			Contract.logException(e);
		}
		output.flush();
		output.close();
	}
	
	/**
	 * Sends a without control-shortcut to this client.
	 * @param message: String message.
	 */
	public void send(String message) {
		synchronized (output) {
			output.println(message + endLine);
		}
		Contract.LogInfo("output: " + message);
	}
	
	/**
	 * Decides what to do with the current input.
	 * @param input: String input.
	 */
	private void handleInput(StringBuilder input) {
		Contract.LogInfo("input: " + input);
		if (input.length() < commandLength) {
			returnError("Send message must be at least 2 Characters long!");
		}
		String command = input.substring(0, commandLength);
		switch (command) {
		case "NM":
			returnName(input.substring(commandLength));
			break;
		case "GC":
			returnChatrooms();
			break;
		case "EC":
			if (!name.isEmpty()) {
				enterChatroom(input.substring(commandLength));
			} else {
				returnError("The client has no name, so it cannot enter a chatroom!");
			}
			break;
		case "MG":
			if (!chatroomName.isEmpty()) {
				shareMessage(input.substring(commandLength));
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
		StringBuilder returnMessage = new StringBuilder("LC");
		synchronized (ApplicationServer.chatrooms) {
			Set<String> chatroomNames = ApplicationServer.chatrooms.keySet();
			for (String name : chatroomNames) {
				returnMessage.append(name);
				returnMessage.append(seperator);
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
				ApplicationServer.chatrooms.put(name, chatroom);
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
		output.flush();
		output.close();
		try {
			socket.close();
		} catch (IOException e) {
			Contract.logException(e);
		}
	}
}
