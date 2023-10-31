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
        this.questionNumber = 0;
        int moneyChange = 1000;
        shuffleQuestions(questions);
        displayNextQuestion();
    }

    // Displays a question and handles the user's answer.
    public void displayNextQuestion() {
        if (questionNumber < questions.size()) {
            Question currentQuestion = questions.get(questionNumber);
            ui.displayQuestion(currentQuestion);
            ui.displayMessage("Question " + (questionNumber + 1) + ":");
            ui.lifelineStatus(lifelineUsed);
            // Now, the UI will wait for button click events to progress.
        } else {
            endGame();
        }
    }

    // This method can be called from the UI when a button is clicked
    public void handleAnswer(String answer) {
        Question currentQuestion = questions.get(questionNumber);

        if (answer.equalsIgnoreCase("L") && !lifelineUsed) {
            answer = useLifeline();
            lifelineUsed = true;
            // Update UI to reflect lifeline use. Maybe disable lifeline button.
        }

        if (answer.matches("[A-D]")) {
            if (currentQuestion.isCorrectAnswer(answer)) {
                ui.displayMessage("Correct!");
                player.updateScore(player.getScore() + 1000);
                ui.displayScore(player);
            } else {
                ui.displayMessage("Incorrect!");
                endGame();
                return;
            }
            questionNumber++;
            displayNextQuestion();
        } else {
            ui.displayMessage("Invalid input, please try again");
        }
    }

    // Ends the game and provides an option to play again.
    public void endGame() {
        ui.displayMessage("Game Over!");

        int response = ui.showConfirmDialog("Do you want to play again?");

        if (response == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            ui.displayMessage("Thank you for playing!");
            playerManager.updatePlayerScore(player.getUsername(), player.getScore());
            ui.close();  // Close the GUI without terminating the entire program
        }
    }
    // Restarts the game, resetting scores and lifeline status.
    private void restartGame() {
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

    // Use lifeline can be adapted for GUI, maybe to eliminate wrong choices or provide hints
    private String useLifeline() {
        if (this.questionNumber < questions.size() && !lifelineUsed) {
            Lifeline lifeline = new Lifeline();
            return lifeline.use(player, questions.get(this.questionNumber));
        } else {
            return "";
        }
    }

}
