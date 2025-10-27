package rpg.items;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class Consumable {
    private String name;
    private int healAmount;

    public Consumable(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    public void consume(Player player, GameState state) {
        if (player == null || state == null) return;

        // Check if player has any meat
        if (state.meat <= 0) {
            TextEffect.typeWriter("No meat available!", 40);
            return;
        }

        int currentHp = player.getHp();
        int maxHp = player.getMaxHp();
        int newHp = currentHp + healAmount;

        // Prevent overhealing
        if (newHp > maxHp) {
            newHp = maxHp;
        }

        // Heal player using proper setter
        player.setHp(newHp);

        // Use one meat
        state.meat -= 1;

        TextEffect.typeWriter(player.getName() + " ate Meat and restored " + healAmount + " HP!",40);
        TextEffect.typeWriter("Current HP: " + player.getHp() + "/" + player.getMaxHp(), 40);
        TextEffect.typeWriter("Remaining Meat: " + state.meat, 40);
    }

    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
