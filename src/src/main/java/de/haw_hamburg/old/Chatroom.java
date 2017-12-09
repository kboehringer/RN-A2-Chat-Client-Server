package src.main.java.de.haw_hamburg.old;

import java.util.ArrayList;

import src.main.java.de.haw_hamburg.messagingComponent.Message;

public class Chatroom {

	private String name;
	private User creator;
	private ArrayList<User> member;
	private ArrayList<Message> messageHistory;
	
	public Chatroom(String nameChatroom, String username) {
		this.name = nameChatroom;
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

	public String getName() {
		return name;
	}
}
