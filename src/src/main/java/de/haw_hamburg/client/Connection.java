package src.main.java.de.haw_hamburg.client;

public interface Connection {
	public void send(String message);
	
	public void getChatroomList();
	
	public void enterChatroom(String chatroomName);
	
	public void sendMessage(String text);
	
	public void sendName(String name);
	
	public void disconnect();
}
