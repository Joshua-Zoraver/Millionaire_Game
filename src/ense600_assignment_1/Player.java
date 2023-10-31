package ense600_assignment_1;

/**
 *
 * @author Joshua and Zoraver
 */
public class Player {

    // Instance variables
    private String username;
    private int score;

    // Constructor to create a Player instance with a specified username and initial score.
    public Player(String username, int score) {
        this.username = username;
        this.score = score;
    }

    // Get the username of the player.
    public String getUsername() {
        return username;
    }

    // Set the username of the player.
    public void setUsername(String username) {
        this.username = username;
    }

    // Get the current score of the player.
    public int getScore() {
        return this.score;
    }

    // Set the score of the player to a new value.
    public void setScore(int score) {
        this.score = score;
    }

    // Update the player's score by adding the specified scoreChange.
    public void updateScore(int scoreChange) {
        this.score += scoreChange;
    }
}
