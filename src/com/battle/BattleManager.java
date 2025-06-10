package com.battle;

import com.characters.Enemy;
import com.characters.Player;
import com.items.Item;
import com.items.Usable;
import com.utils.InputHandler;

public class BattleManager {
    public static void startBattle(Player player) {
        Enemy enemy = new Enemy("Syntax Troll", 50, 10);
        System.out.println("A wild " + enemy.getName() + " appears!");

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Attack");
            System.out.println("2. Use Potion");
            System.out.println("3. Run");

            int choice = InputHandler.getInt("Enter choice: ");

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
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            if (enemy.isAlive()) {
                enemy.attack(player);
            }
        }

        if (!player.isAlive()) {
            System.out.println("You have been defeated...");
        } else if (!enemy.isAlive()) {
            System.out.println("You defeated the " + enemy.getName() + "!");
        }
    }
}
