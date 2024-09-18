package com.ridango.game;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HighScoreService {

    private Connection dbConnection;

    public void connectToSQLiteDB() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:highscores.db");

            String createTableSQL = "CREATE TABLE IF NOT EXISTS highscores (\n"
                    + "id integer PRIMARY KEY,\n"
                    + "playerName text NOT NULL,\n"
                    + "score integer NOT NULL\n"
                    + ");";
            dbConnection.createStatement().execute(createTableSQL);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean highScoreExists(String playerName) {
        String selectSQL = "SELECT COUNT(*) FROM highscores WHERE playerName = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:highscores.db");
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, playerName);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void addHighScore(String playerName, int score) {
        if (highScoreExists(playerName)) {
            String updateSQL = "UPDATE highscores SET score = ? WHERE playerName = ?";
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:highscores.db");
                 PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                connection.setAutoCommit(false);
                preparedStatement.setInt(1, score);
                preparedStatement.setString(2, playerName);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            String insertSQL = "INSERT INTO highscores (playerName, score) VALUES (?, ?)";
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:highscores.db");
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                connection.setAutoCommit(false);
                preparedStatement.setString(1, playerName);
                preparedStatement.setInt(2, score);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<HighScore> getAllHighScores() {
        List<HighScore> highScores = new ArrayList<>();
        String selectSQL = "SELECT * FROM highscores";
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:highscores.db");
             var result = connection.createStatement().executeQuery(selectSQL)) {
            while (result.next()) {
                HighScore highScore = new HighScore();
                highScore.setId(result.getLong("id"));
                highScore.setPlayerName(result.getString("playerName"));
                highScore.setScore(result.getInt("score"));
                highScores.add(highScore);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return highScores;
    }

    public void closeConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}