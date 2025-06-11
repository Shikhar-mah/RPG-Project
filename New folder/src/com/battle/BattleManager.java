package com.battle;

import com.areas.AreaManager;
import com.characters.Enemy;
import com.characters.Player;
import com.items.Item;
import com.items.Usable;

import java.util.Scanner;

public class BattleManager {
    public static void startBattle(Player player) {

        Scanner scanner = new Scanner(System.in);
        AreaManager area = new AreaManager();
        boolean keepPlaying = true;

        while (keepPlaying && player.isAlive()) {
            Enemy enemy = null;

            System.out.println("\n=== Choose an Area to Enter ===");
            System.out.println("1. Area 1");
            System.out.println("2. Area 2");
            System.out.println("3. Area 3");
            System.out.println("4. Area 4");
            System.out.println("5. Area 5");
            System.out.print("Enter your choice (1-5): ");

            int areaChoice;
            if (scanner.hasNextInt()) {
                areaChoice = scanner.nextInt();
            } else {
                System.out.println("Invalid input! Please enter a number between 1 and 5.");
                scanner.next(); // clear invalid input
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
                default -> {
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                    continue;
                }
            }

            System.out.println("A wild " + enemy.getName() + " appears!");
            boolean escaped = false;

            while (player.isAlive() && enemy.isAlive()) {
                System.out.println("\nChoose an action:");
                System.out.println("1. Attack");
                System.out.println("2. Use Potion");
                System.out.println("3. Run");
                System.out.println("4. Show Hp");
                System.out.print("Enter choice: ");

                int choice;
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Invalid input! Please enter 1, 2, or 3.");
                    scanner.next(); // clear invalid input
                    continue;
                }

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
                        System.out.println("You ran away from the battle!");
                        escaped = true;
                        break;
                    case 4:
                        System.out.println("Players hp: " + player.getHp());
                        System.out.println("Enemy hp: " + enemy.getHp());
                        continue;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

                if (escaped || !enemy.isAlive()) {
                    break;
                }

                if (enemy.isAlive()) {
                    enemy.attack(player);
                }
            }

            if (!player.isAlive()) {
                System.out.println("You have been defeated...");
                keepPlaying = false;
            } else if (!enemy.isAlive()) {
                System.out.println("You defeated the " + enemy.getName() + "!");
            } else if (escaped) {
                System.out.println("You safely escaped. Choose another area!");
            }
        }

        System.out.println("\nGame Over. Thanks for playing!");
    }
}