package src.main.java.de.haw_hamburg.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import src.main.java.de.haw_hamburg.Contract;

public class NormalServerConnection extends Thread implements Connection {
	private Socket socket;
	private BufferedReader inputReader;
	private PrintWriter output;
	private ClientGUIController clientGUIController;
	private String clientName = "";
	private final String seperator = "\t";
	private final char lastAnswerPrefix = '-';
	
	public NormalServerConnection(String hostAddress,
			ClientGUIController clientGUIController, String clientName) {
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
		this.clientName = clientName;
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
					if (c == endLine) {
						handleInput(inputStream);
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
			output.println(message);
		}
		System.out.println("output: " + message);
	}

	private void handleInput(StringBuilder inputStream) {
		if (beginnsWith(inputStream, "-SELECTNAME")) {
			sendName(clientName);
		}
		if (beginnsWith(inputStream, "ROOMS")) {
			listChatrooms(inputStream.substring("ROOMS".length()));
		}
		if (beginnsWith(inputStream, "-BYE BYE")) {
			this.interrupt();
		}
	}
	
	private boolean beginnsWith(StringBuilder inputStream, String command) {
		return (inputStream.length() >= command.length())
				&& (inputStream.subSequence(0, command.length()).equals(command));
	}
	
	public void getChatroomList() {
		send("GC");
	}

	private void listChatrooms(String chatrooms) {
		//If just "-"
		if (chatrooms.length() == 1) {
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
	}
	
	public void enterChatroom(String chatroomName) {
		send("JOIN: " + chatroomName);
	}
	
	public void sendName(String name) {
		send(name);
	}
	
	public void sendMessage(String text) {
		send("Message: " + text);
	}
	
	public void disconnect() {
		send("QUIT");
		this.interrupt();
	}
}
