package ense600_assignment_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joshua and Zoraver
 */
public class QuestionLoader {

    // Load questions from a file and return them as a list of Question objects.
    public List<Question> loadQuestionsFromFile(String filename) {
        List<Question> questions = new ArrayList<>();
        List<String> currentOptions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String questionText = "";
            String correctAnswer = "";

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Correct Answer: ")) {
                    correctAnswer = line.substring("Correct Answer: ".length()).trim();
                    questions.add(new Question(questionText, new ArrayList<>(currentOptions), correctAnswer));
                    questionText = "";
                    currentOptions.clear();
                    correctAnswer = "";
                } else if (line.matches("[A-D]\\).+")) {
                    currentOptions.add(line);
                } else {
                    questionText = line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
}
