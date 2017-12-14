package src.main.java.de.haw_hamburg.client;

import src.main.java.de.haw_hamburg.Contract;

public class ApplicationClient {
	public final static int port = 55533;

	public static void main(String[] args) {
		Contract.createLogger(true);
		ClientGUIController clientGUIController = new ClientGUIController();
	}

}
