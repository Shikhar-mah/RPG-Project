package com.characters;

import com.items.HealingPotion;
import com.items.Item;
import java.util.ArrayList;
import java.util.List;

public abstract class Character {
    protected String name;
    protected int hp;
    protected int attackPower;
    protected List<Item> inventory = new ArrayList<>();

    public Character(String name, int hp, int attackPower) {
        this.name = name;
        this.hp = hp;
        this.attackPower = attackPower;
    }

    public abstract void attack(Character target);

    public boolean isAlive() {
        return hp > 0;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttackPower() {
        return this.attackPower;
    }
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void takeDamage(int damage) {
        hp -= damage;

        if(hp<0){
            System.out.println(name + " takes " + damage + " damage! Remaining HP: " + 0);
        } else {
            System.out.println(name + " takes " + damage + " damage! Remaining HP: " + hp);
        }
//        System.out.println(name + " takes " + damage + " damage! Remaining HP: " + hp);
    }

    public void heal(int amount) {
        hp += amount;
        System.out.println(name + " heals for " + amount + " HP! Total HP: " + hp);
    }

    public void addItem(Item item, boolean silent) {
        inventory.add(item);
        if (!silent) {
            System.out.println(item.getName() + " added to inventory.");
        }
    }

    // Default version
    public void addItem(Item item) {
        this.addItem(item, false);
    }

        public List<Item> getInventory() {
        return inventory;
    }

}
