import com.characters.Player;
import com.battle.BattleManager;
import com.utils.DatabaseManager;
import com.utils.InputHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        System.out.println("Welcome to Legends of the Terminal Realm!");

        Player player = null;
        while (player == null) {
            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            int choice = InputHandler.getInt("Choose an option: ");

            if (choice == 1) {
                String name = InputHandler.getString("Enter your hero's name: ");
                if (DatabaseManager.saveExists(name)) {
                    System.out.println("Error: A player with that name already exists.");
                } else {
                    player = new Player(name);
                    System.out.println("Starting a new adventure!");
                }
            } else if (choice == 2) {
                List<Player> savedPlayers = DatabaseManager.loadAllPlayers();
                if (savedPlayers.isEmpty()) {
                    System.out.println("No saved games found.");
                } else {
                    for (Player p : savedPlayers) {
                        System.out.printf("Name: %s | Level: %d | EXP: %d/%d%n",
                                p.getName(),
                                p.getLevel(),
                                p.getCurrentExp(),
                                p.getExpRequiredForNextLevel());
                    }
                    String name = InputHandler.getString("Enter your character's name to load: ");
                    player = DatabaseManager.loadPlayer(name);
                    if (player == null) {
                        System.out.println("Invalid name entered.");
                    }
                }
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        BattleManager.startBattle(player);
    }
}
