package com.characters;

import com.items.HealingPotion;

public class Player extends Character {

    private int level = 1;
    private int currentExp = 0;
    private final int baseExpRequirement = 100; // Base EXP needed to level up

    public Player(String name) {
        super(name, 100, 20);
        // Add a healing potion by default
        addItem(new HealingPotion());
    }

    @Override
    public void attack(Character target) {
        System.out.println(name + " attacks " + target.getName() + " for " + attackPower + " damage!");
        target.takeDamage(attackPower);
    }

    public void gainExp(int amount) {
        currentExp += amount;
        System.out.println("You gained " + amount + " EXP!");

        while (currentExp >= getExpRequiredForNextLevel()) {
            currentExp -= getExpRequiredForNextLevel();
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        System.out.println("You leveled up! You are now level " + level + "!");

        // Increase stats based on level
        setHp(getHp() + 25); // Increase max HP by 25 per level
        setAttackPower(getAttackPower() + 5); // Increase attack power by 5 per level
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentExp() {
        return currentExp;
    }

    public void setCurrentExp(int currentExp) {
        this.currentExp = currentExp;
    }

    public int getExpRequiredForNextLevel() {
        return baseExpRequirement + (level - 1) * 50; // Increases every level
    }
}
