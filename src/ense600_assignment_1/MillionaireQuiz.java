package ense600_assignment_1;

import java.util.List;
import javax.swing.JOptionPane;

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

        String playerName = null;
        while (playerName == null || playerName.trim().isEmpty()) {
            playerName = JOptionPane.showInputDialog(null, "Please enter your name:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                player = playerManager.getPlayer(playerName);
            } else {
                ui.displayMessage("Invalid input. Please enter a valid name.");
            }
        }

        ui.displayMessage("Hello, " + player.getUsername() + "! Let's play!");

        Game game = new Game(player, playerManager, questions, ui);
        game.startGame();

        ui.close();
    }

}
