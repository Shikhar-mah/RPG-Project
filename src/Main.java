import characters.Player;
import battle.BattleManager;
import utils.InputHandler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Legends of the Terminal Realm!");
        String name = InputHandler.getString("Enter your hero's name: ");

        Player player = new Player(name);
        System.out.println("Welcome, " + player.getName() + "!");

        BattleManager.startBattle(player);
    }
}
