package com.characters;

import com.items.HealingPotion;

public class Player extends Character {

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
}
