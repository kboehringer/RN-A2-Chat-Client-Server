package src.main.java.de.haw_hamburg.server.messagingComponent;

import java.io.IOException;
import java.net.UnknownHostException;

public class RechnernetzMessageProtocol {
	
	private String address;
	
	public RechnernetzMessageProtocol(String address) {
		this.address = address;
	}
	
	public void sendMessage(String message) throws UnknownHostException, IOException {
		MessageSender messageSender = new MessageSender(address, message);
	}
	
	
}
