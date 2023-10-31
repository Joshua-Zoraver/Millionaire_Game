package ense600_assignment_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Joshua and Zoraver
 */
public class PlayerManager {

    // // Instance variables
    private Map<String, Player> players;
    private static final String FILE_PATH = "./resources/players.txt";

    // Constructor to initialize the PlayerManager instance.
    public PlayerManager() {
        players = loadPlayers();
    }

    // Get a player by their username. If the player does not exist, create a new player with a score of 0.
    public Player getPlayer(String username) {
        if (players.containsKey(username)) {
            return players.get(username);
        } else {
            Player newPlayer = new Player(username, 0);
            players.put(username, newPlayer);
            savePlayers();
            return newPlayer;
        }
    }

    // Load player data from the file and populate the 'players' map.
    private Map<String, Player> loadPlayers() {
        Map<String, Player> loadedPlayers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    loadedPlayers.put(username, new Player(username, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedPlayers;
    }

    // Save player data from the 'players' map to the file.
    private void savePlayers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Player player : players.values()) {
                writer.write(player.getUsername() + "," + player.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update the score of a player with a specified username.
    public void updatePlayerScore(String username, int scoreChange) {
        if (players.containsKey(username)) {
            Player player = players.get(username);
            player.updateScore(scoreChange);
            savePlayers();
        }
    }
}
