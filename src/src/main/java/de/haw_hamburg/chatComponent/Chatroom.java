package src.main.java.de.haw_hamburg.chatComponent;

import java.util.ArrayList;

import src.main.java.de.haw_hamburg.messagingComponent.Message;

public class Chatroom {

	private String name;
	private ArrayList<User> member;
	
	public Chatroom(String nameChatroom) {
		this.name = nameChatroom;
		member = new ArrayList<>();
	}
	
	public void addUser(User user) {
		member.add(user);
	}
	
	public String getName() {
		return name;
	}
}
