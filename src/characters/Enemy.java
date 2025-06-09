package characters;

public class Enemy extends Character {

    public Enemy(String name, int hp, int attackPower) {
        super(name, hp, attackPower);
    }

    @Override
    public void attack(Character target) {
        System.out.println(name + " attacks " + target.getName() + " for " + attackPower + " damage!");
        target.takeDamage(attackPower);
    }
}
