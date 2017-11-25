package quiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class QuizRoomPlayer extends Thread {
    private Socket server;
    private char playerMark;
    private QuizRoomPlayer opponent;
    private QuizRoom room;
    private BufferedReader input;
    private PrintWriter output;
    private int currentQuestion;
    private boolean continueGame;
    private List<String> questions;
    private List<String> answers;

    public char getPlayerMark() {
        return playerMark;
    }

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
        currentQuestion = 0;
        continueGame = true;

        try {
            input = new BufferedReader(new InputStreamReader(server.getInputStream()));
            output = new PrintWriter(server.getOutputStream(), true);
            output.println("WELCOME " + playerMark);
            output.println("MESSAGE Waiting for an opponent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean nextCategory() {
        return room.getCurrentRoundPlayer().getPlayerMark() == playerMark;
    }

    private void clientChooseCategory(String fromClient) {
        /*
        The first connected player gets to choose category.
        The second player waits until a category has been chosen.
         */
        if (nextCategory()) {
            output.println("CHOOSECATEGORY");
            try {
                fromClient = input.readLine();
                if (fromClient.startsWith("CATEGORY")) {
                    fromClient = fromClient.substring(9);
                    room.chooseCategory(fromClient, this);
                    startNewRound();
                    // Notify the second client/server a category has been chosen
                    opponent.output.println("STARTROUND");
                }
            } catch (IOException e) {

            }
        } else {
            output.println("WAIT");
            try {
                fromClient = input.readLine();
                if (fromClient.startsWith("STARTROUND")) {
                    startNewRound();
                }
            } catch (IOException e) {

            }
        }
    }

    private void startNewRound() {
        questions = room.getQuestions();
        answers = room.getAnswers();
        output.println("QUESTION " + questions.get(currentQuestion));
        currentQuestion++;
    }

    private boolean correctAnswer(String answer) {
        for (int i = 0; i < answers.size(); i++) {
            if (answer.equalsIgnoreCase(answers.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        String fromClient = "";
        output.println("MESSAGE All players have connected. Starting the game");
        clientChooseCategory(fromClient);

        while (continueGame) {
            try {
                fromClient = input.readLine();
                if (fromClient.startsWith("ANSWER")) {
                    fromClient = fromClient.substring(7);

                    // Check if the answer is correct and returns result to client
                    if (correctAnswer(fromClient)) {
                        output.println("RESULT TRUE");
                    } else {
                        output.println("RESULT FALSE");
                    }

                    // Check if there are more questions
                    if (room.nextQuestion(currentQuestion)) {
                        output.println("QUESTION " + questions.get(currentQuestion));
                        currentQuestion++;
                    } else {
                        // If there are no more questions, check if there are more rounds
                        if (room.nextRound()) {
                            currentQuestion = 0;
                            // If there are more rounds, check which player gets to choose a category
                            clientChooseCategory(fromClient);
                        } else {
                            // End game if there are no more rounds
                            output.println("ENDGAME");
                        }
                    }
                } // Important! This command is only sent from the second player server and not the client
                  else if (fromClient.startsWith("STARTROUND")) {
                    startNewRound();
                } else if (fromClient.startsWith("CATEGORY")) {
                    fromClient = fromClient.substring(9);

                } else if (fromClient.startsWith("MESSAGE")) {
                    fromClient = fromClient.substring(8);
                    System.out.println(fromClient);
                }
            } catch (IOException e) {
                System.out.println("Player disconnected");
                continueGame = false;
            }
        }
    }
}
