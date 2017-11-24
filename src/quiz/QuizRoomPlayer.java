package quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuizRoomPlayer extends Thread {
    private Socket server;
    private char playerMark;
    private QuizRoomPlayer opponent;
    private QuizRoom room;
    private BufferedReader input;
    private PrintWriter output;

    public QuizRoomPlayer getOpponent() {
        return opponent;
    }

    public void setOpponent(QuizRoomPlayer opponent) {
        this.opponent = opponent;
    }

    public QuizRoomPlayer(Socket server, char playerMark, QuizRoom room) {
        this.server = server;
        this.playerMark = playerMark;
        this.room = room;

        try {
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter(server.getOutputStream(), true);
            output.println("WELCOME " + playerMark);
            output.println("MESSAGE Waiting for an opponent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String fromClient;
        output.println("MESSAGE All players have connected. Starting game");

        while (true) {
            try {
                fromClient = input.readLine();
                if (fromClient.startsWith("ANSWER")) {
                    fromClient = fromClient.substring(7);
                    if (room.correctAnswer(fromClient)) {

                    }
                } else if (fromClient.startsWith("MESSAGE")) {
                    fromClient = fromClient.substring(8);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
