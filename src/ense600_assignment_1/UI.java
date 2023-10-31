package ense600_assignment_1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author Joshua and Zoraver
 */
public class UI {

    // Components for the GUI
    private JFrame frame;
    private JTextArea questionTextArea;
    private JButton[] optionButtons;
    private JButton lifelineButton;
    private JLabel scoreLabel;

    // Constructor to initialize the UI instance with Swing components.
    public UI() {
        frame = new JFrame("Millionaire Quiz");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        questionTextArea = new JTextArea();
        questionTextArea.setEditable(false);
        frame.add(questionTextArea, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1));
        optionButtons = new JButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionsPanel.add(optionButtons[i]);
        }
        frame.add(optionsPanel, BorderLayout.CENTER);

        scoreLabel = new JLabel();
        frame.add(scoreLabel, BorderLayout.SOUTH);

        lifelineButton = new JButton("Use Lifeline");
        frame.add(lifelineButton, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // Display the question text and answer options.
    public void displayQuestion(Question question, Game gameInstance) {
        questionTextArea.setText(question.getQuestionText());

        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            optionButtons[i].setText(options.get(i));

            // Remove existing listeners to prevent accumulating listeners
            for (ActionListener al : optionButtons[i].getActionListeners()) {
                optionButtons[i].removeActionListener(al);
            }

            int finalI = i;  // For use inside the lambda expression
            optionButtons[i].addActionListener(e -> {
                gameInstance.handleAnswer(options.get(finalI));
            });
        }

        lifelineButton.addActionListener(e -> {
            gameInstance.handleAnswer("L");
        });
    }

    // Display a message to the user using a JOptionPane.
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    // Display the player's username and score.
    public void displayScore(Player player) {
        scoreLabel.setText("Player: " + player.getUsername() + " | Score: $" + player.getScore());
    }

    // Display the lifeline status on the lifeline button.
    public void lifelineStatus(boolean lifelineUsed) {
        if (lifelineUsed) {
            lifelineButton.setEnabled(false);
        } else {
            lifelineButton.setEnabled(true);
        }
    }

    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this.frame, message, "Confirm", JOptionPane.YES_NO_OPTION);
    }

    public void close() {
        frame.dispose();
    }

}
