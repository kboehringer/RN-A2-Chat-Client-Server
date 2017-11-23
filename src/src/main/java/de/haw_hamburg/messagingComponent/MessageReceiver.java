package src.main.java.de.haw_hamburg.messagingComponent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Callable;

import src.main.java.de.haw_hamburg.Contract;

public class MessageReceiver extends Thread {
	private final LinkedList<String> incommingMessageList;
	private InputStream inputStream;
	private Scanner inputScanner;
	
	public MessageReceiver(InputStream inputStream) {
		this.inputStream = inputStream;
		inputScanner = new Scanner(inputStream);
		incommingMessageList = new LinkedList<>();
		start();
	}
	
	public boolean hasMessage() {
		synchronized (incommingMessageList) {
			return !incommingMessageList.isEmpty();
		}
	}
	
	public String getNextMessage() {
		String message = "";
		if (hasMessage()) {
			synchronized (incommingMessageList) {
				message = incommingMessageList.pop();
			}
		}
		return message;
	}
	
	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				if (inputStream.available() > 0 && inputScanner.hasNextLine()) {
					synchronized (incommingMessageList) {
						incommingMessageList.push(inputScanner.nextLine());
					}
				}
			} catch (IOException e) {
				Contract.logException(e);
			}
		}
	}
}
