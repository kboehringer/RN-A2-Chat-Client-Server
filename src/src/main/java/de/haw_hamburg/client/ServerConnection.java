package src.main.java.de.haw_hamburg.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import src.main.java.de.haw_hamburg.Contract;

public class ServerConnection extends Thread {
	private Socket socket;
	private BufferedReader inputReader;
	private PrintWriter output;
	private ClientGUIController clientGUIController;
	private final String seperator = "\t";
	private final char endLine = '\n';
	private final int commandLength = 2;
	private final int maxInputSize = 1024 + commandLength;

	public ServerConnection(String hostAddress, ClientGUIController clientGUIController) {
		try {
			this.socket = new Socket(hostAddress, ApplicationClient.port);
			this.inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			Contract.logException(e);
		} catch (IOException e) {
			Contract.logException(e);
		}
		this.clientGUIController = clientGUIController;
		start();
	}
	
	/**
	 * Starts the Thread, will be called when creating this object.
	 */
	@Override
	public void run() {
		StringBuilder inputStream;
		char c = '-';
		try {
			while (!isInterrupted()) {
				inputStream = new StringBuilder();
				while (inputReader.ready()) {
					c = (char) inputReader.read();
					//If end of line and long enough
					if (c == endLine && inputStream.length() >= commandLength) {
						handleInput(inputStream);
						break;
					} else if (c == endLine && inputStream.length() < commandLength) {
						break;
					} else {
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
		Contract.LogInfo("Output: " + message);
		synchronized (output) {
			output.println(message + endLine);
		}
		System.out.println("output: " + message);
	}

	private void handleInput(StringBuilder input) {
		Contract.LogInfo("Input: " + input);
		System.out.println("input: " + input);
		if (input.length() < 2) {
			Contract.logException(new Exception("Send message must be at least 2 Characters long!"));
		}
		int inputIndex = 2;
		String command = input.substring(0, inputIndex);
		switch (command) {
		case "NM":
			clientGUIController.setName(input.substring(inputIndex));
			break;
		case "LC":
			listChatrooms(input.substring(inputIndex));
			break;
		case "EC":
			String[] chatrooms = input.substring(inputIndex).split(seperator);
			clientGUIController.setChatroomList(chatrooms);
			break;
		case "MG":
			clientGUIController.setMessage(input.substring(inputIndex));
			break;
		case "ER":
			Contract.LogInfo("Error: " + input.substring(inputIndex));
			break;
		default:
			Contract.LogInfo("The command " + command + " is not valid!");
			break;
		}
	}
	
	public void getChatroomList() {
		send("GC");
	}

	private void listChatrooms(String chatrooms) {
		ArrayList<String> tmpList = new ArrayList<>();
		for (String room : chatrooms.split(seperator)) {
			if (room != null && !room.isEmpty()) {
				tmpList.add(room);
			}
		}
		String[] rooms = new String[0];
		rooms = tmpList.toArray(rooms);
		clientGUIController.setChatroomList(rooms);
	}
	
	public void enterChatroom(String chatroomName) {
		send("EC" + chatroomName);
	}
	
	public void sendMessage(String text) {
		String message = "MG" + text;
		if (message.length() > maxInputSize) {
			message = message.substring(0, maxInputSize);
		}
		send(message);
	}
	
	public void sendName(String name) {
		send("NM" + name);
	}
}
