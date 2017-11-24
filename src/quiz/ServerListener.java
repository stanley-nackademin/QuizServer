package quiz;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerListener {

    public static void main(String[] args) {
        try {
            ServerSocket listener = new ServerSocket(4444);
            System.out.println("Server started, listening for connections...");

            while (true) {
                QuizRoom room = new QuizRoom();
                QuizRoomPlayer playerX = new QuizRoomPlayer(listener.accept(), 'X', room);
                System.out.println("First client connected");
                QuizRoomPlayer playerO = new QuizRoomPlayer(listener.accept(), 'O', room);
                System.out.println("Second client connected");
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                room.setCurrentRoundPlayer(playerX);
                playerX.start();
                playerO.start();
            }
        } catch (IOException e) {
            System.out.println("Could not create server with the given port.\n" +
                    "The port could be in use.");
        }
    }
}
