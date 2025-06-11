package com.utils;

import com.characters.Player;
import com.items.HealingPotion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {


    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "rpgGame";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";


    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";


    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void initializeDatabase() {

        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player_name VARCHAR(50) NOT NULL UNIQUE, " +
                "level INT NOT NULL DEFAULT 1, " +
                "current_exp INT NOT NULL DEFAULT 0, " +
                "hp INT GENERATED ALWAYS AS (100 + (level - 1) * 5) STORED, " +  // auto-scale HP
                "attack_power INT GENERATED ALWAYS AS (10 + (level - 1) * 5) STORED, " +  // auto-scale attack
                "potion_count INT NOT NULL, " +
                "highest_area_unlocked INT NOT NULL, " +
                "last_saved_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ");";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
//            String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
//                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
//                    "player_name VARCHAR(50) NOT NULL UNIQUE, " +
//                    "level INT NOT NULL DEFAULT 1, " +
//                    "current_exp INT NOT NULL DEFAULT 0, " +
//                    "hp INT GENERATED ALWAYS AS (100 + (level - 1) * 25) STORED, " +
//                    "attack_power INT GENERATED ALWAYS AS (10 + (level - 1) * 5) STORED, " +
//                    "potion_count INT NOT NULL, " +
//                    "highest_area_unlocked INT NOT NULL, " +
//                    "last_saved_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
//                    ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void savePlayer(Player player, int highestArea) {
        if (saveExists(player.getName())) {
            System.out.println("Error: A player with the name '" + player.getName() + "' already exists.");
            return;
        }

        String sql = "INSERT INTO player_data (player_name, level, current_exp, potion_count, highest_area_unlocked) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getLevel());
            pstmt.setInt(3, player.getCurrentExp());
            long potionCount = player.getInventory().stream()
                    .filter(item -> item instanceof HealingPotion)
                    .count();
            pstmt.setInt(4, (int) potionCount);
            pstmt.setInt(5, highestArea);
            pstmt.executeUpdate();
            System.out.println("Game saved for " + player.getName() + "!");
        } catch (SQLException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public static Player loadPlayer(String playerName) {
        String sql = "SELECT level, current_exp, potion_count, highest_area_unlocked FROM player_data WHERE player_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Player loadedPlayer = new Player(playerName);

                int level = rs.getInt("level");
                loadedPlayer.setLevel(level);
                loadedPlayer.setCurrentExp(rs.getInt("current_exp"));

                // hp and attack_power calculated from level
                loadedPlayer.setHp(100 + (level - 1) * 25);
                loadedPlayer.setAttackPower(10 + (level - 1) * 5);

                loadedPlayer.getInventory().clear();
                int potionCount = rs.getInt("potion_count");
                for (int i = 0; i < potionCount; i++) {
                    loadedPlayer.addItem(new HealingPotion());
                }

                System.out.println("Welcome back, " + playerName + "!");
                System.out.println("You have " + potionCount + " potions.");
                System.out.println("You have unlocked up to Area " + rs.getInt("highest_area_unlocked") + ".");

                return loadedPlayer;
            }

        } catch (SQLException e) {
            System.out.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static boolean saveExists(String playerName) {
        String sql = "SELECT 1 FROM player_data WHERE player_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Player> loadAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT player_name, level, current_exp FROM player_data";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("player_name");
                int level = rs.getInt("level");
                int exp = rs.getInt("current_exp");

                Player player = new Player(name);
                player.setLevel(level);
                player.setCurrentExp(exp);
                player.setHp(100 + (level - 1) * 25);
                player.setAttackPower(10 + (level - 1) * 5);

                players.add(player);
            }
        } catch (SQLException e) {
            System.out.println("Error loading saved players: " + e.getMessage());
        }
        return players;
    }

}
