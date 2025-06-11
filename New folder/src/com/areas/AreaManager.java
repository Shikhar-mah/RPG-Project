package com.areas;

import com.characters.Enemy;
import com.characters.Player;
import java.util.Random;
import java.util.Scanner;

public class AreaManager {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();


    private Enemy getRandomEnemy(Enemy e1, Enemy e2) {
        int roll = rand.nextInt(10); // 0 to 9

        // 0 to 6 → 70% chance
        if (roll < 7) {
            return e1;
        } else {
            return e2;
        }
    }

    public Enemy area1(Player player) {
        // Area 1 enemies
        Enemy enyArea1_1 = new Enemy("Slime", 10, 5);
        Enemy enyArea1_2 = new Enemy("Giant Rat", 15, 10);

        Enemy selectedEnemy = getRandomEnemy(enyArea1_1, enyArea1_2);
        System.out.println("A wild " + selectedEnemy.getName() + " appears!");

//            battle(player, selectedEnemy);

        return selectedEnemy;
    }

    public Enemy area2(Player player) {
        // Area 2 enemies
        Enemy enyArea2_1 = new Enemy("Zombie", 20, 15);
        Enemy enyArea2_2 = new Enemy("Dire Wolf", 25, 20);

        Enemy selectedEnemy = getRandomEnemy(enyArea2_1, enyArea2_2);
        System.out.println("Area 2 - Enemy encountered: " + selectedEnemy.getName());

//            battle(player, selectedEnemy);

        return selectedEnemy;
    }

    public Enemy area3(Player player) {
        // Area 3 enemies
        Enemy enyArea3_1 = new Enemy("Ogre", 30, 25);
        Enemy enyArea3_2 = new Enemy("Vampire", 35, 30);

        Enemy selectedEnemy = getRandomEnemy(enyArea3_1, enyArea3_2);
        System.out.println("Area 3 - Enemy encountered: " + selectedEnemy.getName());

//            battle(player, selectedEnemy);

        return selectedEnemy;
    }

    public Enemy area4(Player player) {
        // Area 4 enemies
        Enemy enyArea4_1 = new Enemy("Wyvern", 40, 35);
        Enemy enyArea4_2 = new Enemy("Demon Knight", 45, 40);

        Enemy selectedEnemy = getRandomEnemy(enyArea4_1, enyArea4_2);
        System.out.println("Area 4 - Enemy encountered: " + selectedEnemy.getName());

//            battle(player, selectedEnemy);

        return selectedEnemy;
    }

    public Enemy area5(Player player) {
        // Area 5 enemies
        Enemy enyArea5_1 = new Enemy("Lich", 50, 45);
        Enemy enyArea5_2 = new Enemy("Ancient Dragon", 55, 50);

        Enemy selectedEnemy = getRandomEnemy(enyArea5_1, enyArea5_2);
        System.out.println("Area 5 - Enemy encountered: " + selectedEnemy.getName());

//            battle(player, selectedEnemy);
        return selectedEnemy;

    }
}
