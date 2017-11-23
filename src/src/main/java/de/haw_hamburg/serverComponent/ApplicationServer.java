package src.main.java.de.haw_hamburg.serverComponent;

import src.main.java.de.haw_hamburg.Contract;

public class ApplicationServer {
	public final static int port = 80;

	public static void main(String[] args) {
		Contract.createLogger();
	}
}
