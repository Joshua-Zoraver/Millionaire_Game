package ense600_assignment_1;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private Map<String, Player> players;
    private Connection conn;

    public PlayerManager() {
        // Here, establish a database connection
        try (Connection conn = establishDatabaseConnection()) {
            createPlayersTableIfNotExists(conn);
            this.conn = establishDatabaseConnection();
            this.players = loadPlayersFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions or log them
        }
    }

    private Connection establishDatabaseConnection() {
        // Database connection details
        String url = "jdbc:derby://localhost:1527/Users";
        String user = "ENSE600";
        String password = "AUT";

        try {
            // Explicitly load the Derby driver class
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            return DriverManager.getConnection(url, user, password);
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

    public Player getPlayer(String username) {
        if (players.containsKey(username)) {
            return players.get(username);
        } else {
            // Insert new player into database
            insertNewPlayerIntoDatabase(username);
            Player newPlayer = new Player(username, 0);
            players.put(username, newPlayer);
            return newPlayer;
        }
    }

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

    private void insertNewPlayerIntoDatabase(String username) {
        String insertSql = "INSERT INTO Players (username, score) VALUES (?, 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerScore(String username, int scoreChange) {
        if (players.containsKey(username)) {
            Player player = players.get(username);
            updatePlayerScoreInDatabase(player);
        }
    }

    private void updatePlayerScoreInDatabase(Player player) {
        String updateSql = "UPDATE Players SET score = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setInt(1, player.getScore());
            pstmt.setString(2, player.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
