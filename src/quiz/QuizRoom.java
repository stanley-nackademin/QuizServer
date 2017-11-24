package quiz;

public class QuizRoom {
    private int totalRounds;
    private int questionsPerRound;
    private String[] questions = {"Fråga 1", "Fråga 2", "Fråga 3"};
    private String[] answers = {"1", "2", "3"};
    private QuizRoomPlayer currentRoundPlayer;

    public QuizRoomPlayer getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

    public void setCurrentRoundPlayer(QuizRoomPlayer currentRoundPlayer) {
        this.currentRoundPlayer = currentRoundPlayer;
    }

    public QuizRoom() {
        totalRounds = 2;
        questionsPerRound = 2;
    }

    public boolean correctAnswer(String answer) {
        boolean result = false;
        for (String s : answers) {
            if (answer.equalsIgnoreCase(s))
                result = true;
        }
        return result;
    }
}
