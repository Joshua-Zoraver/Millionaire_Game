package ense600_assignment_1;

import java.util.List;

/**
 *
 * @author Joshua and Zoraver
 */
public class MillionaireQuiz {

    // Main method
    public static void main(String[] args) {
        String filename = "./resources/questions.txt";
        QuestionLoader questionLoader = new QuestionLoader();
        List<Question> questions = questionLoader.loadQuestionsFromFile(filename);

        UI ui = new UI();
        ui.displayMessage("Welcome to Who Wants to Be a Millionaire!");

        PlayerManager playerManager = new PlayerManager();
        Player player = null;

        while (player == null) {
            ui.displayMessage("Please enter your name: ");
            String playerName = ui.getUserInput("");
            player = playerManager.getPlayer(playerName);
            if (player == null) {
                ui.displayMessage("Invalid input. Please enter a valid name.");
            }
        }

        ui.displayMessage("Hello, " + player.getUsername() + "! Let's play!");

        Game game = new Game(player, playerManager, questions, ui);

        game.startGame();
        ui.closeScanner();
    }
}