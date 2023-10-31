package ense600_assignment_1;

import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author Joshua and Zoraver
 */
public class Game {

    // Instance variables
    private final Player player;
    private final UI ui;
    private final PlayerManager playerManager;
    private final List<Question> questions;
    private boolean lifelineUsed;
    private int questionNumber = 0;

    // Constructor to initialize the Game instance.
    public Game(Player player, PlayerManager playerManager, List<Question> questions, UI ui) {
        this.player = player;
        this.ui = ui;
        this.questions = questions;
        this.playerManager = playerManager;
        this.questionNumber = 0;
        this.lifelineUsed = false;
    }

    //Starts the game and progresses through the questions.
    public void startGame() {
        System.out.println("Starting game");
        this.questionNumber = 0;
        shuffleQuestions(questions);
        System.out.println("questions shuffled");
        displayNextQuestion();
    }

    // Displays a question and handles the user's answer.
    public void displayNextQuestion() {
        System.out.println("preparing to display question " + (questionNumber + 1));
        if (questionNumber < questions.size()) {
            Question currentQuestion = questions.get(questionNumber);
            System.out.println("displaying question: " + currentQuestion.getQuestionText());
            ui.displayQuestion(currentQuestion, this);
            ui.displayMessage("Question " + (questionNumber + 1) + ":");
            try {
                ui.lifelineStatus(lifelineUsed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No more questions");
            try {
                endGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // This method can be called from the UI when a button is clicked
    public void handleAnswer(String answer) {
        System.out.println("handling answer " + answer);
        Question currentQuestion = questions.get(questionNumber);
        if (answer.equalsIgnoreCase("L") && !lifelineUsed) {
            System.out.println("using lifeline");
            answer = useLifeline();
            lifelineUsed = true;
        }
        if (answer.matches("[A-D]")) {
            if (currentQuestion.isCorrectAnswer(answer)) {
                System.out.println("answer is correct");
                ui.displayMessage("Correct!");
                player.updateScore(player.getScore() + 1000);
                ui.displayScore(player);
            } else {
                System.out.println("answer is incorrect");
                ui.displayMessage("Incorrect!");
                endGame();
                return;
            }
            questionNumber++;
            displayNextQuestion();
        } else {
            System.out.println("Invalid input detected.");
            ui.displayMessage("Invalid input, please try again");
        }
    }

    // Ends the game and provides an option to play again.
    public void endGame() {
        System.out.println("endGame method called.");
        ui.displayMessage("Game Over!");

        int response = ui.showConfirmDialog("Do you want to play again?");

        if (response == JOptionPane.YES_OPTION) {
            restartGame();
            System.out.println("playing again.");
        } else {
            ui.displayMessage("Thank you for playing!");
            System.out.println("not play again.");
            playerManager.updatePlayerScore(player.getUsername(), player.getScore());
            ui.close();  // Close the GUI without terminating the entire program
        }
    }

    // Restarts the game, resetting scores and lifeline status.
    private void restartGame() {
        System.out.println("restarting");
        player.setScore(0);
        lifelineUsed = false;

        for (Question question : questions) {

            question.resetQuestions();
        }

        startGame();
    }

    // Shuffles a list of questions to randomize the order.
    public static void shuffleQuestions(List<Question> questions) {
        Random rand = new Random();
        for (int i = questions.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Question temp = questions.get(i);
            questions.set(i, questions.get(j));
            questions.set(j, temp);
        }
    }

    private String useLifeline() {
        if (this.questionNumber < questions.size() && !lifelineUsed) {
            Lifeline lifeline = new Lifeline();
            return lifeline.use(player, questions.get(this.questionNumber), ui, this);
        } else {
            return "";
        }
    }

}
