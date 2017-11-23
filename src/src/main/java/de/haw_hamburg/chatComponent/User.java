package src.main.java.de.haw_hamburg.chatComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;

import src.main.java.de.haw_hamburg.Contract;
import src.main.java.de.haw_hamburg.clientComponent.ApplicationClient;

public class User {

	private String name;
	private SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
	private Socket socket;
	private BufferedReader inputStream;
    private PrintWriter outputStream;
	
	public User(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void connect(String address) {
        try {
            socket = (Socket) socketFactory.createSocket(address, ApplicationClient.port);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            Contract.logException(e);
        }
    }
	
	public void send(String message) {
        outputStream.println(message);
        outputStream.flush();
    }
	
	public String receive() {
        String response = "";
        try {
            response = inputStream.readLine();
        } catch (IOException e) {
        	Contract.logException(e);
        }
        return response;
    }
	
	public void disconnect() {
        try {
            socket.getInputStream().close();
            socket.getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            Contract.logException(e);
        }
    }
	
	public boolean isConnected() {
        return socket.isConnected();
    }
}
