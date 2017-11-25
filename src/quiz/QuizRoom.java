package quiz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QuizRoom {
    private int totalRounds;
    private int questionsPerRound;
    private int currentRound;
    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private QuizRoomPlayer currentRoundPlayer;

    public List<String> getQuestions() {
        return questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public QuizRoomPlayer getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

    public void setCurrentRoundPlayer(QuizRoomPlayer currentRoundPlayer) {
        this.currentRoundPlayer = currentRoundPlayer;
    }

    public QuizRoom() {
        currentRound = 0;
        readPropertyFile();
        mockDatabase();
    }

    /*public List<String> readQuestionsFromFile(String category) {

    }*/

    // Only for testing
    private void mockDatabase() {
        questions.add("Fråga 1");
        questions.add("Fråga 2");
        questions.add("Fråga 3");
        answers.add("ett");
        answers.add("två");
        answers.add("tre");
    }

    public void chooseCategory(String category, QuizRoomPlayer player) {
        // TODO
        currentRoundPlayer = player.getOpponent();
        currentRound++;
    }

    private void readPropertyFile() {
        String path = "settings.properties";
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(path));
                String rounds = p.getProperty("totalRounds", "2");
                String questions = p.getProperty("questionsPerRound", "2");
                totalRounds = Integer.parseInt(rounds);
                questionsPerRound = Integer.parseInt(questions);

                // If the value is zero or less for either totalRounds or questionsPerRound.
                // Set it to the default value, 2.
                if (totalRounds <= 0) {
                    totalRounds = 2;
                } else if (questionsPerRound <= 0) {
                    questionsPerRound = 0;
                } else if (totalRounds <= 0 && questionsPerRound <= 0) {
                    totalRounds = 2;
                    questionsPerRound = 2;
                }
            } catch (FileNotFoundException e) {
                System.out.println("The file could not be found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            totalRounds = 2;
            questionsPerRound = 2;
        }
    }

    public boolean nextQuestion(int currentQuestion) {
        return currentQuestion < questionsPerRound;
    }

    public boolean nextRound() {
        return currentRound < totalRounds;
    }
}
