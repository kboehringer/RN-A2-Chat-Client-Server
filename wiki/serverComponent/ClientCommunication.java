package serverComponent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientCommunication extends Thread {

    private Room room;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean quit = false;

    public ClientCommunication(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            registerUser();
            chooseLobbyCommands();
            sendMessage();
            Server.removeClient(this);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() throws IOException {
        //Send hello message to ClientCommunication
        out.println("-SELECTNAME");
        out.flush();
        while (!quit) {
            String name = in.readLine();
            if (name == null) {
                return;
            }
            if (name.toUpperCase().equals("QUIT")) {
                quit = true;
            }
            if (name.equals("") || name.length() > 20 || name.startsWith(" ") || name.endsWith(" ")) {
                out.println("INVALID");
                out.println("-SELECTNAME");
                out.flush();
            } else {
                synchronized (Server.getNames()) {
                    if (!Server.getNames().contains(name)) {
                        Server.getNames().add(name);
                        this.setName(name);
                        System.out.println(name);
                        out.println("-ACCEPTED");
                        out.flush();
                        break;
                    } else {
                        out.println("INVALID");
                        out.println("-SELECTNAME");
                        out.flush();
                    }
                }
            }

        }

    }

    private void chooseLobbyCommands() throws IOException {
        while (!quit) {
            String input = in.readLine();

            if (input == null) {
                return;
            }

            input = input.toUpperCase();
            Pattern userPattern = Pattern.compile("USERS: (.*)");
            Matcher userMatcher = userPattern.matcher(input);

            Pattern joinRoomPattern = Pattern.compile("JOIN: (.*)");
            Matcher joinRoomMatcher = joinRoomPattern.matcher(input);

            if (input.equals("ROOMS")) {
                out.print(roomsToString());
                out.flush();
            } else if (userMatcher.find()) {
                Room r = findRoomByName(userMatcher.group(1));
                if (r != null) {
                    out.println(r.clientsToString());
                    out.flush();
                } else {
                    out.println("-INVALID_ROOMNAME");
                    out.flush();
                }
            } else if (input.equals("HELP")) {
                out.println("ROOMS");
                out.println("USERS: <ROOMNAME>");
                out.println("-JOIN: <ROOMNAME>");
                out.flush();
            } else if (joinRoomMatcher.find()) {
                Room r = findRoomByName(joinRoomMatcher.group(1));
                this.room = r;
                r.addClient(this);
                out.println("-JOIN_SUCCESSFUL");
                out.flush();
                room.outputStreams.add(out);
                room.sendClientJoinedNotification(this);
                break;
            } else if (input.equals("QUIT")) {
                quit = true;
            } else {
                out.println("-UNKNOWN_COMMAND");
                out.flush();
            }
        }
    }

    private void sendMessage() throws IOException {
        while (!quit) {
            String input = in.readLine();

            if (input == null) {
                return;
            }

            Pattern messagePattern = Pattern.compile("MESSAGE: (.*)");
            Matcher messageMatcher = messagePattern.matcher(input);
            if (messageMatcher.find()) {
                Server.sendMessageToRoom(this, room, messageMatcher.group(1));
            } else if (input.toUpperCase().equals("QUIT")) {
                quit = true;
            } else {
                out.println("-INVALID_MESSAGE_FORMAT");
                out.flush();
            }


        }
        out.println("-BYE BYE");
        out.flush();
    }

    public String roomsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        if (Server.getRooms().isEmpty()) {
            return "-";
        }
        List<Room> roomsWithoutLast = new ArrayList<>(Server.getRooms());
        roomsWithoutLast.remove(roomsWithoutLast.size() - 1);
        for (Room r : roomsWithoutLast) {
            sb.append(r.getName() + "\n");
        }
        sb.append("-" + Server.getRooms().get(roomsWithoutLast.size()).getName() + "\n");
        return sb.toString();
    }

    public Room findRoomByName(String name) {
        for (Room r : Server.getRooms()) {
            if (r.getName().toUpperCase().equals(name)) {
                return r;
            }
        }
        return null;
    }

    public Room getRoom() {
        return room;
    }

    public PrintWriter getOut() {
        return out;
    }
}
