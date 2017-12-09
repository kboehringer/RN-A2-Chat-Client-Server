package src.main.java.de.haw_hamburg.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnection extends Thread {
	private Socket socket;
	private Scanner input;
	private PrintWriter output;
	private String name = "";
	private String chatroomName = "";
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		try {
			this.input = new Scanner(socket.getInputStream());
			this.output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}
	
	@Override
	public void run() {
		String inputMessage = "";
		while (!isInterrupted()) {
			if (input.hasNextLine()) {
				handleInput(inputMessage);
			}
		}
	}
	
	public void send(String message) {
		synchronized (output) {
			output.println(message);
		}
	}
	
	private void handleInput(String input) {
		if (input.length() < 2) {
			returnError("Send message must be at least 2 Characters long!");
		}
		int inputIndex = 2;
		String command = input.substring(0, inputIndex);
		switch (command) {
		case "CC":
			Chatroom chatroom = new Chatroom(input.substring(inputIndex));
			synchronized (ApplicationServer.chatrooms) {
				ApplicationServer.chatrooms.add(chatroom);
			}
			chatroom.enter(this);
			chatroomName = chatroom.getName();
			break;
		case "GC":
			returnChatrooms();
			break;
		case "EC":
			enterChatroom(input.substring(inputIndex));
			break;
		default:
			returnError("The command " + command + " is not valid!");
			break;
		}
	}
	
	private void returnChatrooms() {
		String controlChar = "*C";
		StringBuilder returnMessage = new StringBuilder("LC");
		synchronized (ApplicationServer.chatrooms) {
			for (Chatroom chatroom : ApplicationServer.chatrooms) {
				returnMessage.append(chatroom.getName());
				returnMessage.append(controlChar);
			}
		}
		send(returnMessage.toString());
	}
	
	private void enterChatroom(String name) {
		//TODO
	}
	
	private void returnError(String errorText) {
		send("ER" + errorText);
	}
}
