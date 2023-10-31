package ense600_assignment_1;

import java.util.List;
import java.util.Random;

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

        for (Question question : questions) {
            questionNumber++;
            ui.displayMessage("Question " + questionNumber + ":");
            ui.displayQuestion(question);

            ui.displayMessage("Select your answer (A, B, C, D) or press X to quit");
            ui.lifelineStatus(lifelineUsed);

            while (true) {
                String answer = ui.getUserInput("");
                answer = answer.toUpperCase();

                if (answer.equalsIgnoreCase("X")) {
                    ui.displayScore(player);
                    endGame();
                    return;
                }

                if (answer.equalsIgnoreCase("L") && !lifelineUsed) {
                    answer = useLifeline();
                    lifelineUsed = true;
                }

                if (answer.matches("[A-D]")) {

                    if (question.isCorrectAnswer(answer)) {
                        ui.displayMessage("Correct!");
                        player.updateScore(moneyChange);
                        if (player.getScore() >= 1000000) {
                            player.setScore(1000000);
                            ui.displayScore(player);
                            System.out.println("Congratulations! You're a millionaire!");
                            endGame();
                        } else {
                            ui.displayScore(player);
                            moneyChange += moneyChange;
                        }
                    } else {
                        ui.displayMessage("Incorrect!");
                        ui.displayScore(player);
                        endGame();
                        return;
                    }

                    break;
                } else {
                    ui.displayMessage("Invalid input, please try again");
                }
            }
        }

        ui.displayMessage("Game Over!");

        endGame();

    }

    // Displays a question and handles the user's answer.
    public void askQuestion(Question question) {
        ui.displayMessage(question.getQuestionText());

        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            ui.displayMessage((char) ('A' + i) + ") " + options.get(i));
        }

        String playerAnswer = ui.getUserInput("Enter your answer: (A, B, C, D)").toUpperCase();

        if (question.isCorrectAnswer(playerAnswer)) {
            ui.displayMessage("Correct!");
            player.updateScore(1000);
        } else {
            ui.displayMessage("Incorrect!");
            endGame();
        }
    }

    // Ends the game and provides an option to play again.
    public void endGame() {
        ui.displayMessage("Game Over!");

        String playAgain = ui.getUserInput("Do you want to play again? (Type \"yes\" to continue, or anything else to end)").toLowerCase();

        if (playAgain.equals("yes")) {
            restartGame();
        } else {
            ui.displayMessage("Thank you for playing!");
            PlayerManager playerManager = new PlayerManager();
            playerManager.updatePlayerScore(player.getUsername(), player.getScore());
            System.exit(0);
        }

        playerManager.updatePlayerScore(player.getUsername(), player.getScore());
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

    // Uses a lifeline for the current question.
    private String useLifeline() {
        if (this.questionNumber > 0 && !lifelineUsed) {
            Lifeline lifeline = new Lifeline();
            return lifeline.use(player, questions.get(this.questionNumber - 1));
        } else {
            return "";
        }
    }

}
