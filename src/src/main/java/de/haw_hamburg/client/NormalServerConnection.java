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
	private final int PORT = 9002;
	private Socket socket;
	private BufferedReader inputReader;
	private PrintWriter output;
	private ClientGUIController clientGUIController;
	private String clientName = "";
	private final String seperator = "\t";
	private final char lastAnswerPrefix = '-';
	private boolean getChatrooms = false;
	private boolean getUsers = false;
	
	public NormalServerConnection(String hostAddress,
			ClientGUIController clientGUIController, String clientName) {
		try {
			this.socket = new Socket(hostAddress, PORT);
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
		ArrayList<String> lines = new ArrayList<>();
		String line = "";
		try {
			while (!isInterrupted()) {
				lines = new ArrayList<>();
				while (inputReader.ready()) {
					line = inputReader.readLine();
					lines.add(line);
					if (line.length() < 0 && line.charAt(0) == lastAnswerPrefix) {
						handleInput(lines);
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

	private void handleInput(ArrayList<String> lines) {
		if (lines != null && lines.size() > 0) {
			String line = lines.get(0);
			if (beginnsWith(lines.get(-1), "-SELECTNAME")) {
				if (beginnsWith(line, "INVALID")) {
					clientGUIController.setMessage("Invalid Username!");
				} else {
					sendName(clientName);
				}
			} else if (beginnsWith(line, "-JOIN_SUCCESSFUL")) {
				clientGUIController.setMessage("Joined Room");
			} else if (beginnsWith(line, "-BYE BYE")) {
				this.interrupt();
			} else if (getChatrooms) {
				listChatrooms(lines);
			} else if (getUsers) {
				//TODO
			}
		}
	}
	
	private boolean beginnsWith(String inputStream, String command) {
		return (inputStream.length() >= command.length())
				&& (inputStream.subSequence(0, command.length()).equals(command));
	}
	
	public void getChatroomList() {
		send("ROOMS");
		getChatrooms = true;
	}

	private void listChatrooms(ArrayList<String> chatrooms) {
		clientGUIController.setChatroomList(listStringsToArray(chatrooms));
		getChatrooms = false;
	}
	
	private String[] listStringsToArray(ArrayList<String> strings) {
		String[] rooms = new String[0];
		if (strings.get(0).length() == 1 && strings.get(0).charAt(0) == lastAnswerPrefix) {
			clientGUIController.setMessage("No Chatrooms avaiable!");
		} else {
			rooms = strings.toArray(rooms);
			rooms[-1] = rooms[-1].substring(1);
		}
		return rooms;
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
