package items;

import characters.Character;

public class HealingPotion implements Item, Usable {
    private final String name = "Healing Potion";
    private final int healAmount = 30;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void use(Character target) {
        target.heal(healAmount);
        System.out.println(target.getName() + " used a Healing Potion!");
    }
}
