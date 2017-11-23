package src.main.java.de.haw_hamburg.serverComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import de.haw.chatsystem.model.ChatRaum;
import de.haw.chatsystem.model.Server.ConnectedThread;
import src.main.java.de.haw_hamburg.Contract;
import src.main.java.de.haw_hamburg.chatComponent.Chatroom;

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
	
	public class ConnectedThread implements Runnable {
        private Thread t;
        private Chatroom actualChatroom;
        private BufferedReader is;
        private PrintWriter os;
        private Socket s;

        public ConnectedThread(Socket s) {
            this.s = s;
            try {
                is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                os = new PrintWriter(s.getOutputStream());

            } catch (IOException e) {
                System.out.println("IO error in server thread");
            }
        }

        public void run() {
            String line;
            try {
                while (!t.isInterrupted()) {
                    line = is.readLine();
                    if (line == null) {
                        cm.stream().filter(e -> e.getClients().contains(this)).findFirst()
                                .orElse(new Chatroom("")).remove(this);
                        t.interrupt();
                    } else {
                        commandParser(line, this);

                        System.out.println(line);
                    }

                }
                System.out.println("connection closed");
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
                if (s != null) {
                    s.close();
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }

        }

        void start() {
            if (t == null) {
                t = new Thread(this, "");
                t.start();
            }
        }

        void cancel() {
            t.interrupt();
        }

        void setActualChatroom(ChatRaum<ConnectedThread> actualChatroom) {
            this.actualChatroom = actualChatroom;
        }

        ChatRaum<ConnectedThread> getActualChatroom() {
            return actualChatroom;
        }
    }
}
