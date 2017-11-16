package src.main.java.de.haw_hamburg.server.chatComponent;

public class User {

	private String name;
	private String id;
	
	public User(String name) {
		this.name = name;
		this.id = "" + System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}
}
