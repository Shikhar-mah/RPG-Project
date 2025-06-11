package com.battle;

import com.areas.AreaManager;
import com.characters.Enemy;
import com.characters.Player;
import com.exceptions.InvalidInputException;
import com.items.HealingPotion;
import com.items.Item;
import com.items.Usable;
import com.utils.DatabaseManager;
import com.utils.InputHandler;

import java.util.HashMap;
import java.util.Map;
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

            System.out.println("\n=== Choose an Area to Enter ===");
            System.out.println("1. Area 1");
            System.out.println("2. Area 2");
            System.out.println("3. Area 3");
            System.out.println("4. Area 4");
            System.out.println("5. Area 5");
            System.out.println("6. Save and Quit");
            System.out.print("Enter your choice (1-6): ");

            int areaChoice;// = scanner.nextInt();

            try {
//                System.out.print("Enter your choice (1-6): ");

                if (!scanner.hasNextInt()) throw new InvalidInputException("\nPlease enter a valid number (1-6).");
                areaChoice = scanner.nextInt();
                if (areaChoice < 1 || areaChoice > 6)
                    throw new InvalidInputException("\nInvalid input! Choose between 1 and 6.");
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            if (areaChoice == 6) {
//                DatabaseManager.savePlayer(player, highestAreaCompleted + 1);
                DatabaseManager.savePlayerUpdate(player, highestAreaCompleted + 1);
                keepPlaying = false;
                continue;
            }
//
//
//
//            if (areaChoice == 6) {
//                DatabaseManager.savePlayer(player, highestAreaCompleted + 1);
//                keepPlaying = false;
//                continue;
//            }

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

//            System.out.println("A wild " + enemy.getName() + " appears!");

            while (player.isAlive() && enemy.isAlive()) {

                System.out.println("\nChoose an action:");
                System.out.println("1. Attack");
                System.out.println("2. Use Potion");
                System.out.println("3. Run");
                System.out.println("4. Show Details");

//                System.out.println("Enter choice: ");
                int choice;// = scanner.nextInt();


                try {
                    System.out.print("Enter choice: ");
                    if (!scanner.hasNextInt())
                        throw new InvalidInputException("Only numbers between 1 - 4 are allowed!");
                    choice = scanner.nextInt();
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine(); // Clear invalid input
                    continue;
                }

                boolean run_from_battle = false;

                switch (choice) {
                    case 1 -> player.attack(enemy);
                    case 2 -> {
                        boolean used = false;
                        for (Item item : player.getInventory()) {
                            if (item instanceof Usable) {
                                ((Usable) item).use(player);
                                player.getInventory().remove(item);
                                used = true;
                                continue;
                            }
                        }
                        if (!used) {
                            System.out.println("No usable potions found.");
                        }
                        continue;
                    }
                    case 3 -> {
                        System.out.println("You fled the battle!");
                        run_from_battle = true;
//                        DatabaseManager.savePlayer(player, highestAreaCompleted + 1);
                        break;
                    }
                    case 4 -> {
                        showDetails(player, enemy);
                        continue;
                    }
                    default -> {
                        System.out.println("Invalid choice. Try again.");
                        continue;
                    }
                }

                if (enemy.isAlive() && !run_from_battle) {
                    enemy.attack(player);
                }
                if(run_from_battle) {
                    break;
                }
            }



            if (player.isAlive() && !enemy.isAlive()) {
//                System.out.println("You defeated the " + enemy.getName() + "!");
                System.out.println("===========================");
                enemy.giveExp(player, initialHp); // create an initial hp for enemy.
                System.out.println("===========================");

                // 70% chance to get a healing potion
                if (Math.random() < 0.7) {
                    System.out.println("===========================");
                    System.out.println("You found a Healing Potion!");
                    System.out.println("===========================");
                    player.addItem(new HealingPotion());
                }

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

    private static void showDetails(Player player, Enemy enemy) {
        System.out.println("\n=== Player Details ===");
        Map<String, Object> playerDetails = new HashMap<>();
        playerDetails.put("Name", player.getName());
        playerDetails.put("Level", player.getLevel());
        playerDetails.put("HP", player.getHp());
        playerDetails.put("Attack Power", player.getAttackPower());
        playerDetails.put("Potions", player.getInventory().stream().filter(i -> i instanceof HealingPotion).count());

        playerDetails.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("Current EXP: " + player.getCurrentExp() + " / " + player.getExpRequiredForNextLevel());

        System.out.println("\n=== Enemy Details ===");
        Map<String, Object> enemyDetails = new HashMap<>();
        enemyDetails.put("Name", enemy.getName());
        enemyDetails.put("HP", enemy.getHp());
        enemyDetails.put("Attack Power", enemy.getAttackPower());

        enemyDetails.forEach(
                (k, v) -> System.out.println(k + ": " + v)
        );


    }


}
