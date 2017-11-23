package src.main.java.de.haw_hamburg.chatComponent;

import java.util.ArrayList;

public class Chatroom {

	private String name;
	private ArrayList<Client> member;
	
	public Chatroom(String nameChatroom) {
		this.name = nameChatroom;
		member = new ArrayList<>();
	}
	
	public void addUser(Client user) {
		member.add(user);
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Client> getMember() {
		return member;
	}
}
