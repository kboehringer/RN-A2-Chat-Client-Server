package serverComponent;

import java.io.PrintWriter;
import java.util.*;

public class Room {

    String name;
    Server server;
    List<ClientCommunication> clients = Collections.synchronizedList(new ArrayList<>());
    Set<PrintWriter> outputStreams = Collections.synchronizedSet(new HashSet<>());

    public Room(String name, Server server) {
        this.name = name;
        this.server = server;
    }

    public List<ClientCommunication> getClients() {
        return clients;
    }

    public void addClient(ClientCommunication client) {
        clients.add(client);
    }

    public void sendMessage(ClientCommunication client, String message) {
        for (PrintWriter out : outputStreams) {

            out.println(client.getName() + ": " + message);
            out.flush();
        }
    }

    public void sendClientGoneMessage(ClientCommunication client) {
        for (PrintWriter out : outputStreams) {
            if (!out.equals(client.getOut())) {
                out.println(client.getName() + " hat den Raum verlassen");
                out.flush();
            }
        }
    }

    public void sendClientJoinedNotification(ClientCommunication client) {
        for (PrintWriter out : outputStreams) {
            if (!out.equals(client.getOut())) {
                out.println(client.getName() + " hat den Raum betreten");
                out.flush();
            }
        }

    }

    public String clientsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if (clients.isEmpty()) {
            return "-";
        }
        List<ClientCommunication> clientsWithoutLast = new ArrayList<>(clients);
        clientsWithoutLast.remove(clients.size() - 1);
        for (ClientCommunication c : clientsWithoutLast) {
            sb.append(c.getName() + "\n");
        }
        sb.append("-" + clients.get(clients.size() - 1).getName() + "\n");
        return sb.toString();
    }

    public Set<PrintWriter> getOutputStreams() {
        return outputStreams;
    }

    public String getName() {
        return name;
    }
}
