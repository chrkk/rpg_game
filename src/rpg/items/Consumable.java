package rpg.items;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class Consumable {
    public enum Type {
        MEAT,
        MEDIUM_POTION
    }

    private final String name;
    private final int healAmount;
    private final Type type;

    public Consumable(Type type, String name, int healAmount) {
        this.type = type;
        this.name = name;
        this.healAmount = healAmount;
    }

    public void consume(Player player, GameState state) {
        if (player == null || state == null) return;

        int available = getCount(state);
        if (available <= 0) {
            TextEffect.typeWriter("No " + name + " available!", 40);
            return;
        }

        if (player.getHp() >= player.getMaxHp()) {
            TextEffect.typeWriter("Your HP is already full!", 40);
            return;
        }

        int newHp = Math.min(player.getHp() + healAmount, player.getMaxHp());
        player.setHp(newHp);

        setCount(state, available - 1);

        TextEffect.typeWriter(player.getName() + " used " + name + " and restored " + healAmount + " HP!", 40);
        TextEffect.typeWriter("Current HP: " + player.getHp() + "/" + player.getMaxHp(), 40);
        TextEffect.typeWriter("Remaining " + name + ": " + getCount(state), 40);
    }

    private int getCount(GameState state) {
        switch (type) {
            case MEAT:
                return state.meat;
            case MEDIUM_POTION:
                return state.mediumPotions;
            default:
                return 0;
        }
    }

    private void setCount(GameState state, int value) {
        switch (type) {
            case MEAT:
                state.meat = Math.max(0, value);
                break;
            case MEDIUM_POTION:
                state.mediumPotions = Math.max(0, value);
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return healAmount;
    }
}
