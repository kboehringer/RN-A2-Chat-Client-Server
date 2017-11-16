package src.main.java.de.haw_hamburg.server;

public class ApplicationServer {
	
	public final static int port = 80;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void checkNull(Object object) {
		if (object == null) {
			throw new NullPointerException("Object is null!");
		}
	}
}
