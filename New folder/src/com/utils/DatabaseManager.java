package com.utils;

import com.characters.Player;
import com.items.HealingPotion;
import java.sql.*;

public class DatabaseManager {


    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "rpg";
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
                "hp INT NOT NULL, " +
                "attack_power INT NOT NULL, " +
                "potion_count INT NOT NULL, " +
                "highest_area_unlocked INT NOT NULL, " +
                "last_saved_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }

    public static void savePlayer(Player player, int highestArea) {
        String sql = "INSERT INTO player_data (player_name, hp, attack_power, potion_count, highest_area_unlocked) " +
                "VALUES(?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "hp = VALUES(hp), " +
                "attack_power = VALUES(attack_power), " +
                "potion_count = VALUES(potion_count), " +
                "highest_area_unlocked = VALUES(highest_area_unlocked);";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, player.getName());
            pstmt.setInt(2, player.getHp());
            pstmt.setInt(3, player.getAttackPower());
            long potionCount = player.getInventory().stream().filter(item -> item instanceof HealingPotion).count();
            pstmt.setInt(4, (int) potionCount);
            pstmt.setInt(5, highestArea);
            pstmt.executeUpdate();
            System.out.println("Game saved for " + player.getName() + "!");

        } catch (SQLException e) {
            System.out.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Player loadPlayer(String playerName) {
        String sql = "SELECT hp, attack_power, potion_count, highest_area_unlocked FROM player_data WHERE player_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Player loadedPlayer = new Player(playerName);
                loadedPlayer.setHp(rs.getInt("hp"));
                loadedPlayer.setAttackPower(rs.getInt("attack_power"));

                loadedPlayer.getInventory().clear();
                int potionCount = rs.getInt("potion_count");
                for (int i = 0; i < potionCount; i++) {
                    loadedPlayer.addItem(new HealingPotion());
                }

                System.out.println("Welcome back, " + playerName + "!");
                System.out.println("You have " + potionCount + " potions.");
                // This gives you the info about what they've done
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
}