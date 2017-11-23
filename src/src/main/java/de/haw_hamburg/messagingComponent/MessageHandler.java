package src.main.java.de.haw_hamburg.messagingComponent;

import java.util.List;

public interface MessageHandler {

	public void handleIncommingMessage(List<String> messages);
}
