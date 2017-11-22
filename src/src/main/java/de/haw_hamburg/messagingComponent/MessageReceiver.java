package src.main.java.de.haw_hamburg.messagingComponent;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;

public class MessageReceiver extends Thread{
	private final LinkedList<String> incommingMessageList;
	private InputStream inputStream;
	private Scanner inputScanner;
	private boolean threadStop = false;
	
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
				//TODO inform observer
			}
		}
		return message;
	}
	
	@Override
	public void run() {
		while (threadStop) {
			try {
				if (inputStream.available() > 0 && inputScanner.hasNextLine()) {
//					synchronized (incommingMessageList) {
//						incommingMessageList.push(inputScanner.nextLine());
//					}
					System.out.println(inputScanner.nextLine());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void stopThread() {
		threadStop = true;
	}
}
