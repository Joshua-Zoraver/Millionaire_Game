package ense600_assignment_1;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 *
 * @author Joshua and Zoraver
 */
public class Lifeline {

    // Instance variable
    private final Random random;

    // Constructor to initialize the Lifeline instance.
    public Lifeline() {
        random = new Random();
    }

    // Uses the lifeline to eliminate two incorrect options from the current question.
    public String use(Player player, Question question) {
        List<String> options = question.getOptions();

        List<Integer> incorrectIndices = getIncorrectIndices(options, question.getCorrectAnswer());

        int eliminateCount = 2;

        while (eliminateCount > 0 && incorrectIndices.size() > 0) {
            int randomIndex = random.nextInt(incorrectIndices.size());
            int indexToRemove = incorrectIndices.get(randomIndex);

            options.set(indexToRemove, " ");
            incorrectIndices.remove(randomIndex);
            eliminateCount--;
        }

        UI ui = new UI();
        ui.displayMessage("Using the Lifeline - Two incorrect options have been eliminated:");
        ui.displayQuestion(question);

        String playerAnswer = ui.getUserInput("Enter your answer: (A, B, C, D)").toUpperCase();

        return playerAnswer;
    }

    // Retrieves the indices of incorrect options in the given list of options.
    private List<Integer> getIncorrectIndices(List<String> options, String correctAnswer) {
        List<Integer> incorrectIndices = new ArrayList<>();

        for (int i = 0; i < options.size(); i++) {
            if (!options.get(i).startsWith(correctAnswer)) {
                incorrectIndices.add(i);
            }
        }

        return incorrectIndices;
    }
}
