package ense600_assignment_1;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Joshua and Zoraver
 */
public class Lifeline {

    private final Random random;

    public Lifeline() {
        random = new Random();
    }

    public String use(Player player, Question question, UI ui, Game game) {
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

        ui.displayMessage("Using the Lifeline - Two incorrect options have been eliminated:");
        ui.displayQuestion(question, game);

        return "";
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
