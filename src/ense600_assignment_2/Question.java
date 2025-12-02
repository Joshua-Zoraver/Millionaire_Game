package ense600_assignment_2;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Joshua and Zoraver
 */
public class Question {

    // Instance variables
    private final String questionText;
    private List<String> currentOptions;
    private final List<String> originalOptions;
    private final String correctAnswer;

    // Constructor to create a Question instance with question text, answer options, and the correct answer.
    public Question(String questionText, List<String> options, String correctAnswer) {
        this.questionText = questionText;
        this.originalOptions = new ArrayList<>(options);
        this.currentOptions = new ArrayList<>(options);
        this.correctAnswer = correctAnswer;
    }

    // Get the text of the question.
    public String getQuestionText() {
        return questionText;
    }

    // Get the current answer options for the question.
    public List<String> getOptions() {
        return currentOptions;
    }

    // Check if the provided answer is correct.
    public boolean isCorrectAnswer(String answer) {
        return answer.equalsIgnoreCase(correctAnswer);
    }

    // Get the correct answer for the question.
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Reset the question's answer options to their original values.
    public void resetQuestions() {

        this.currentOptions = new ArrayList<>(originalOptions);
    }
}
