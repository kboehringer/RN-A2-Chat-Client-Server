package src.main.java.de.haw_hamburg.server.chatComponent;

import java.util.ArrayList;

import src.main.java.de.haw_hamburg.server.messagingComponent.Message;

public class Chatroom {

	private User creator;
	private ArrayList<User> member;
	private ArrayList<Message> messageHistory;
	
	public Chatroom(String username) {
		this.creator = new User(username);
		member = new ArrayList<>();
		messageHistory = new ArrayList<>();
	}
	
	public void addUser(String username) {
		member.add(new User(username));
	}
	
	public void createMessage(String text, String userId) {
		//TODO send message
	}
	
	public void addMessage(String messageValues) {
		messageHistory.add(new Message(messageValues));
	}
}
