package src.main.java.de.haw_hamburg.clientComponent;

import src.main.java.de.haw_hamburg.Contract;

public class ApplicationClient {
	public final static int port = 80;

	public static void main(String[] args) {
		Contract.createLogger();
	}

}
