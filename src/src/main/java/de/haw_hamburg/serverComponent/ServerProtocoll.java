package src.main.java.de.haw_hamburg.serverComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import src.main.java.de.haw_hamburg.Contract;
import src.main.java.de.haw_hamburg.chatComponent.Chatroom;

public class ServerProtocoll {
	private SSLServerSocket sslServerSocket;
	private List<Chatroom> chatroomList;
	private Map<ClientConnection, String> logged;
	private AcceptThread accept;

	public ServerProtocoll() {
        chatroomList = new ArrayList<>();
        logged = new HashMap<>();
        accept = new AcceptThread();
    }

	public void startServer() {
		accept.start();
	}

	public void stopServer() {
		accept.Interrupt();
	}

	private void sendMessageChatRoom(String line, ClientConnection from) {
		Chatroom currentChatroom = from.getCurrentChatroom();
		
	}

	private Chatroom findChatroom(String name) {
		for (Chatroom chatroom : chatroomList) {
			if (chatroom.getName().equals(name)) {
				return chatroom;
			}
		}
		return null;
	}

	private void commandParser(String cmd, ClientConnection from) {
		synchronized (this) {

		}
	}

	public class AcceptThread implements Runnable {
        private Thread t;

        public void run() {
            SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            try {
                sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(ApplicationServer.port);
            } catch (IOException e) {
                Contract.logException(e);
            }

            while (!t.isInterrupted()) {
                try {
                    SSLSocket client = (SSLSocket) sslServerSocket.accept();
                    System.out.println("connection Established");
                    ClientConnection st = new ClientConnection(client);
                    st.start();

                } catch (Exception e) {
                	Contract.logException(e);
                }
            }
            try {
                sslServerSocket.close();
                for (Chatroom chatroom : chatroomList) {
                    for (ClientConnection connectedThread : chatroom.getMember()) {
                        connectedThread.cancel();
                    }
                }
            } catch (IOException e) {
                Contract.logException(e);
            }
        }

        void start() {
            if (t == null) {
                t = new Thread(this, "");
                t.start();
            }
        }

        void Interrupt() {
            t.interrupt();
        }
    }
	
	public class ClientConnection implements Runnable {
        private Thread thread;
        private Chatroom actualChatroom;
        private BufferedReader inputConnetcion;
        private PrintWriter outputConnection;
        private Socket socket;

        public ClientConnection(SSLSocket socket) {
            this.socket = socket;
            try {
                inputConnetcion = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputConnection = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                Contract.logException(e);
            }
        }

        public void run() {
            String line;
            try {
                while (!thread.isInterrupted()) {
                    line = inputConnetcion.readLine();
                    commandParser(line, this);

                }
                if (outputConnection != null) {
                    outputConnection.close();
                }
                if (inputConnetcion != null) {
                    inputConnetcion.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException | NullPointerException e) {
                Contract.logException(e);
            }

        }

        void start() {
            if (thread == null) {
                thread = new Thread(this, "");
                thread.start();
            }
        }

        public void cancel() {
            thread.interrupt();
        }

        public void setActualChatroom(Chatroom actualChatroom) {
            this.actualChatroom = actualChatroom;
        }

        public Chatroom getCurrentChatroom() {
            return actualChatroom;
        }
    }
}
