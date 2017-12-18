package src.main.java.de.haw_hamburg.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import src.main.java.de.haw_hamburg.Contract;

public class ServerConnection extends Thread {
	private Socket socket;
	private Scanner input;
	private PrintWriter output;
	private ClientGUIController clientGUIController;
	private final char seperator = '\t';

	public ServerConnection(String hostAddress, ClientGUIController clientGUIController) {
		try {
			this.socket = new Socket(hostAddress, ApplicationClient.port);
			this.input = new Scanner(socket.getInputStream());
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
		Contract.LogInfo("Output: " + message);
		synchronized (output) {
			output.println(message + "\n");
		}
		System.out.println("output: " + message);
	}

	private void handleInput(String input) {
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

	private void listChatrooms(String chatrooms) {
		clientGUIController.setChatroomList(chatrooms.split("\t"));
	}
}
