package src.main.java.de.haw_hamburg.serverComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import de.haw.chatsystem.model.ChatRaum;
import de.haw.chatsystem.model.Server.ConnectedThread;
import src.main.java.de.haw_hamburg.Contract;

public class ConnectionListener extends Thread {
	private ServerSocket serverSocket;

	public ConnectionListener() {
		
		start();
	}
	
	@Override
	public void run() {
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
        try {
            serverSocket = (ServerSocket) socketFactory.createServerSocket(ApplicationServer.port);
        } catch (IOException e) {
        	Contract.logException(e);
        }

        while (!isInterrupted()) {
            try {
                Socket client = (Socket) serverSocket.accept();
                ConnectedThread st = new ConnectedThread(client);
                st.start();

            } catch (Exception e) {
            	Contract.logException(e);
            }
        }
        try {
            serverSocket.close();
            for (ChatRaum<ConnectedThread> chatRaum : cm) {
                for (ConnectedThread connectedThread : chatRaum.getClients()) {
                    connectedThread.cancel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
