package com.battle;

import com.areas.AreaManager;
import com.characters.Enemy;
import com.characters.Player;
import com.items.Item;
import com.items.Usable;
import com.utils.DatabaseManager;
import com.utils.InputHandler;
import java.util.Scanner;

public class BattleManager {
    public static void startBattle(Player player) {

        Scanner scanner = new Scanner(System.in);
        AreaManager area = new AreaManager(); // use correct class name
        DatabaseManager dbManager = new DatabaseManager();
        boolean keepPlaying = true;
        int highestAreaCompleted = 0;


        while (keepPlaying && player.isAlive()) {

            Enemy enemy = null;

            System.out.println("=== Choose an Area to Enter ===");
            System.out.println("1. Area 1");
            System.out.println("2. Area 2");
            System.out.println("3. Area 3");
            System.out.println("4. Area 4");
            System.out.println("5. Area 5");
            System.out.println("6. Save and Quit");
            System.out.print("Enter your choice (1-6): ");

            int areaChoice = scanner.nextInt();

            if (areaChoice == 6) {
                DatabaseManager.savePlayer(player, highestAreaCompleted + 1);
                keepPlaying = false;
                continue;
            }

                switch (areaChoice) {
                case 1 -> {
                    System.out.println("You are entering Area 1...");
                    enemy = area.area1(player);
                }
                case 2 -> {
                    System.out.println("You are entering Area 2...");
                    enemy = area.area2(player);
                }
                case 3 -> {
                    System.out.println("You are entering Area 3...");
                    enemy = area.area3(player);
                }
                case 4 -> {
                    System.out.println("You are entering Area 4...");
                    enemy = area.area4(player);
                }
                case 5 -> {
                    System.out.println("You are entering Area 5...");
                    enemy = area.area5(player);
                }
                default -> System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }

//        Enemy enemy = new Enemy("Syntax Troll", 50, 10);
            int initialHp = enemy.getHp();
            System.out.println("A wild " + enemy.getName() + " appears!");
            while (player.isAlive() && enemy.isAlive()) {

                System.out.println("\nChoose an action:");
                System.out.println("1. Attack");
                System.out.println("2. Use Potion");
                System.out.println("3. Run");
                System.out.println("4. Show Hp");

                System.out.println("Enter choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        player.attack(enemy);
                        break;
                    case 2:
                        boolean used = false;
                        for (Item item : player.getInventory()) {
                            if (item instanceof Usable) {
                                ((Usable) item).use(player);
                                player.getInventory().remove(item);
                                used = true;
                                break;
                            }
                        }
                        if (!used) {
                            System.out.println("No usable potions found.");
                        }
                        break;
                    case 3:
                        System.out.println("You fled the com.battle!");
                        return;
                    case 4:
                        System.out.println("Player Level: " + player.getLevel());
                        System.out.println("Player HP: " + player.getHp());
                        System.out.println("Enemy HP: " + enemy.getHp());
                        System.out.println("Current EXP: " + player.getCurrentExp() + " / " + player.getExpRequiredForNextLevel());
                        continue;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

                if (enemy.isAlive()) {
                    enemy.attack(player);
                }
            }

            if (player.isAlive() && !enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getName() + "!");
                enemy.giveExp(player, initialHp); // create an initial hp for enemy.
                // Update progress if this is a new highest area
                if (areaChoice > highestAreaCompleted) {
                    highestAreaCompleted = areaChoice;
                }

            }

            if (!player.isAlive()) {
                System.out.println("You have been defeated...");
            } else if (!enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getName() + "!");
            }
        }
    }
}
