package serverComponent;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class Server {

    private static final int PORT = 9002;


    private static Set<String> names = Collections.synchronizedSet(new HashSet<String>(Arrays.asList("TIMO")));
    private static List<Room> rooms = Collections.synchronizedList(new ArrayList<>());

    public Server() {
        createRoom("chatroom1");
        createRoom("chatroom2");
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started: " + serverSocket.toString());
        Server server = new Server();
        try {
            while (true) {
                new ClientCommunication(serverSocket.accept()).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    public void createRoom(String roomName) {
        Room room = new Room(roomName, this);
        rooms.add(room);
    }


    public static void sendMessageToRoom(ClientCommunication client, Room room, String message) {
        room.sendMessage(client, message);
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    public static Set getNames() {
        return names;
    }


    public static void removeClient(ClientCommunication client) {
        if (client.getName() != null) {
            names.remove(client.getName());
        }
        if (client.getOut() != null && client.getRoom() != null) {
            client.getRoom().sendClientGoneMessage(client);
            client.getRoom().getOutputStreams().remove(client.getOut());
        }
        if (client != null && client.getRoom() != null) {
            client.getRoom().getClients().remove(client);
        }

    }
}
