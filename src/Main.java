import com.characters.Player;
import com.battle.BattleManager;
import com.utils.DatabaseManager;
import com.utils.InputHandler;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        System.out.println("Welcome to Legends of the Terminal Realm!");
        String name = InputHandler.getString("Enter your hero's name: ");

        Player player;

        // Check if a save file exists for this player
        if (DatabaseManager.saveExists(name)) {
            System.out.println("Save data found for " + name + ".");
            int choice = InputHandler.getInt("1. New Game\n2. Load Game\nChoose an option: ");
            if (choice == 2) {
                player = DatabaseManager.loadPlayer(name);
                if (player == null) { // Fallback in case loading fails
                    System.out.println("Could not load game. Starting a new one.");
                    player = new Player(name);
                }
            } else {
                player = new Player(name);
                System.out.println("Starting a new adventure!");
            }
        } else {
            // No save exists, start a new game automatically
            player = new Player(name);
            System.out.println("Welcome, " + player.getName() + "!");
        }

        BattleManager.startBattle(player);
    }
}
