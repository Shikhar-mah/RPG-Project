package com.characters;

public class Enemy extends Character {

    public Enemy(String name, int hp, int attackPower) {
        super(name, hp, attackPower);
    }

    protected int expGiven = hp*2;
    @Override
    public void attack(Character target) {
        System.out.println(name + " attacks " + target.getName() + " for " + attackPower + " damage!");
        target.takeDamage(attackPower);
    }

    public void giveExp(Player player, int hp) {
        int exp = hp * 2;
        player.gainExp(exp);
    }
}
