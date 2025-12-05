package ense600_assignment_2;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private Map<String, Player> players;

    // JDBC connection to Derby DB
    private Connection conn;


    // Attempt to connect to Derby DB
    public PlayerManager() {
        // Connect to / create database
        this.conn = establishDatabaseConnection();

        // Throw exception if it fails
        if (this.conn == null) {
            throw new RuntimeException("Failed to establish database connection.");
        }

        try {
            // Ensures Players table exists
            createPlayersTableIfNotExists(this.conn);

            // Load from database into the Map
            this.players = loadPlayersFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Establishes a connection to an embedded Derby DB
    // If it doesnt exist it will be created
    private Connection establishDatabaseConnection() {
        // Database connection URL
        String url = "jdbc:derby:PlayersDB;create=true";

        try {
            // Explicitly load the Derby driver class
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Connection failed.");
            e.printStackTrace();
            return null;
        }
    }

    // Retrieves or creates a player object and resets the score for returning players
    public Player getPlayer(String username) {
        if (players.containsKey(username)) {
            // If Player is a returning player, reset the score to 0
            Player player = players.get(username);
            player.setScore(0);
            updatePlayerScoreInDatabase(player); // Update the score in the database
            return player;
        } else {
            // Insert new player into database with score 0
            insertNewPlayerIntoDatabase(username);
            Player newPlayer = new Player(username, 0);
            players.put(username, newPlayer);
            return newPlayer;
        }
    }

    // Loads all players and their scores from the database into a map.
    private Map<String, Player> loadPlayersFromDatabase() {
        Map<String, Player> loadedPlayers = new HashMap<>();
        String query = "SELECT username, score FROM Players";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                loadedPlayers.put(username, new Player(username, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loadedPlayers;
    }

    // Inserts a new player with a score of 0 into the database.
    private void insertNewPlayerIntoDatabase(String username) {
        String insertSql = "INSERT INTO Players (username, score) VALUES (?, 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates the score of the player in the local map and database.
    public void updatePlayerScore(String username, int scoreChange) {
        if (players.containsKey(username)) {
            Player player = players.get(username);
            updatePlayerScoreInDatabase(player);
        }
    }

    // Executes the update operation to change the player's score in the database.
    private void updatePlayerScoreInDatabase(Player player) {
        String updateSql = "UPDATE Players SET score = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setInt(1, player.getScore()); // Here the score is taken from the player object
            pstmt.setString(2, player.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Checks for the existence of the Players table and creates it if it does not exist.
    private void createPlayersTableIfNotExists(Connection conn) throws SQLException {
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "PLAYERS", null);
        if (!tables.next()) {
            // Table does not exist, so create it
            try (Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE Players ("
                        + "username VARCHAR(255),"
                        + "score INT"
                        + ")";
                stmt.executeUpdate(sql);
            }
        }
    }
}
