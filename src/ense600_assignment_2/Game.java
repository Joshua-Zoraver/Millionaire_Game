package ense600_assignment_2;

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

    // Prize ladder
    private static final int[] PRIZE_LADDER = {
        100,
        200,
        300,
        500,
        1000, // first Safe haven
        2000,
        4000,
        8000,
        16000,
        32000, // second Safe haven
        64000,
        125000,
        250000,
        500000,
        1000000 // top prize
    };

    private int guaranteedWinnings = 0;

    // Constructor to initialize the Game instance.
    public Game(Player player, PlayerManager playerManager, List<Question> questions, UI ui) {
        this.player = player;
        this.ui = ui;
        this.questions = questions;
        this.playerManager = playerManager;
        this.questionNumber = 0;
        this.lifelineUsed = false;
        this.guaranteedWinnings = 0;
    }

    //Starts the game and progresses through the questions.
    public void startGame() {
        this.questionNumber = 0;
        this.guaranteedWinnings = 0;
        shuffleQuestions(questions);
        displayNextQuestion();
    }

    // Displays a question and handles the user's answer.
    public void displayNextQuestion() {
        if (questionNumber < questions.size()) {
            Question currentQuestion = questions.get(questionNumber);
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
        Question currentQuestion = questions.get(questionNumber);

        // Handle lifeline
        if (answer.equalsIgnoreCase("L") && !lifelineUsed) {
            answer = useLifeline();
            lifelineUsed = true;
        }

        if (answer.matches("[A-D]")) {
            if (currentQuestion.isCorrectAnswer(answer)) {
                ui.displayMessage("Correct!");

                // Move up prize ladder for correct answer
                int prize = getCurrentPrize();
                player.setScore(prize);

                // Update score if on a Safe haven
                if (isSafeHaven(questionNumber)) {
                    guaranteedWinnings = prize;
                }

                ui.displayScore(player);

                // If this was the last question then end the game
                boolean lastQuestion = questionNumber >= questions.size() - 1 || questionNumber >= PRIZE_LADDER.length - 1;

                if (lastQuestion) {
                    ui.displayMessage("Congratulations! You've won $" + player.getScore() + "!");
                    endGame();
                    return;
                }

            } else {
                // Incorrect answer, drop to the last guaranteed amount
                ui.displayMessage("Incorrect!");

                player.setScore(guaranteedWinnings);
                if (guaranteedWinnings > 0) {
                    ui.displayMessage("You leave with $" + guaranteedWinnings + ".");
                } else {
                    ui.displayMessage("You leave with nothing. Better luck next time.");
                }

                ui.displayScore(player);
                endGame();
                return;
            }
            questionNumber++;
            displayNextQuestion();
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
            playerManager.savePlayerScore(player);
            ui.close();  // Close the GUI without terminating the entire program
        }
    }

    // Restarts the game, resetting scores and lifeline status.
    private void restartGame() {
        player.setScore(0);
        lifelineUsed = false;
        guaranteedWinnings = 0;

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

    // Uses lifeline if it has not been used once already
    private String useLifeline() {
        if (this.questionNumber < questions.size() && !lifelineUsed) {
            Lifeline lifeline = new Lifeline();
            return lifeline.use(player, questions.get(this.questionNumber), ui, this);
        } else {
            return "";
        }
    }

    // Get the prize for the current question
    private int getCurrentPrize() {
        int index = Math.min(questionNumber, PRIZE_LADDER.length - 1);
        return PRIZE_LADDER[index];
    }

    // Check if current question is a Safe haven
    private boolean isSafeHaven(int questionIndex) {
        return questionIndex == 4 || questionIndex == 9;
    }

}
