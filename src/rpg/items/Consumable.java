package rpg.items;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class Consumable {
    public enum Type {
        MEAT,
        MEDIUM_POTION,
        MANA_POTION
    }

    private final String name;
    private final int effectAmount;
    private final Type type;

    public Consumable(Type type, String name, int effectAmount) {
        this.type = type;
        this.name = name;
        this.effectAmount = effectAmount;
    }

    public void consume(Player player, GameState state) {
        if (player == null || state == null) return;

        int available = getCount(state);
        if (available <= 0) {
            TextEffect.typeWriter("No " + name + " available!", 40);
            return;
        }

        int restoredAmount = 0;
        switch (type) {
            case MEAT:
            case MEDIUM_POTION:
                if (player.getHp() >= player.getMaxHp()) {
                    TextEffect.typeWriter("Your HP is already full!", 40);
                    return;
                }
                int beforeHp = player.getHp();
                player.setHp(player.getHp() + effectAmount);
                restoredAmount = player.getHp() - beforeHp;
                break;
            case MANA_POTION:
                if (player.getMana() >= player.getMaxMana()) {
                    TextEffect.typeWriter("Your mana is already full!", 40);
                    return;
                }
                restoredAmount = player.restoreMana(effectAmount);
                break;
            default:
                break;
        }

        if (restoredAmount <= 0) {
            return;
        }

        setCount(state, available - 1);

        String restoredStat = (type == Type.MANA_POTION) ? "Mana" : "HP";
        TextEffect.typeWriter(player.getName() + " used " + name + " and restored " + restoredAmount + " " + restoredStat + "!", 40);
        String currentStat = (type == Type.MANA_POTION)
                ? player.getMana() + "/" + player.getMaxMana()
                : player.getHp() + "/" + player.getMaxHp();
        TextEffect.typeWriter("Current " + restoredStat + ": " + currentStat, 40);
        TextEffect.typeWriter("Remaining " + name + ": " + getCount(state), 40);
    }

    private int getCount(GameState state) {
        switch (type) {
            case MEAT:
                return state.meat;
            case MEDIUM_POTION:
                return state.mediumPotions;
            case MANA_POTION:
                return state.manaPotions;
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
            case MANA_POTION:
                state.manaPotions = Math.max(0, value);
                break;
            default:
                break;
        }
    }

    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return effectAmount;
    }
}
