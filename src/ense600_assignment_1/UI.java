package ense600_assignment_1;

import java.util.Scanner;
import java.util.List;

/**
 *
 * @author Joshua and Zoraver
 */
public class UI {

    // Instance variable
    private Scanner scanner;

    // Constructor to initialize the UI instance with a Scanner for user input.
    public UI() {
        scanner = new Scanner(System.in);
    }

    // Display the question text and answer options.
    public void displayQuestion(Question question) {
        System.out.println(question.getQuestionText());

        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println(options.get(i));
        }
    }

    // Get user input with a provided prompt.
    public String getUserInput(String prompt) {
        displayMessage(prompt);
        return scanner.nextLine();
    }

    // Display a message to the user.
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // Display the player's username and score.
    public void displayScore(Player player) {
        displayMessage("Player: " + player.getUsername());
        displayMessage("Score: $" + player.getScore());
    }

    // Close the scanner used for user input.
    public void closeScanner() {
        scanner.close();
    }

    // Display the lifeline status to inform the player whether it's available or used.
    public void lifelineStatus(boolean lifelineUsed) {
        if (lifelineUsed) {
            displayMessage("Lifeline has already been used.");
        } else {
            displayMessage("You have a lifeline available. Enter 'L' to use it.");
        }
    }
}
