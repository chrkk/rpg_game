package rpg.systems;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class StatusSystem {
    public static void showStatus(Player player, int shards, GameState state) {
        TextEffect.typeWriter(
            "\n------ " + player.getName() + ": The " + player.getTrait() + " ------" +
            "\n| HP: " + player.getHp() + "/" + player.getMaxHp() +
            " | Level: " + player.getLevel() +
            " | EXP: " + player.getExp() + "/" + player.getExpToNextLevel() +
            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
            " | Defense: " + player.getDefense() +
            " | Intelligence: " + player.getIntelligence() +
            " | Weapon: " + (player.getWeapon() == null ? "None" : player.getWeapon().toString()) +
            " | Shards: " + shards,
            40
        );

        // 🆕 ASCII Progress Bar
        int steps = state.forwardSteps;
        int maxSteps = 5; // threshold for boss gate
        StringBuilder bar = new StringBuilder("\nProgress: [S");

        if (state.bossGateDiscovered) {
            // Always show full bar once gate is found
            for (int i = 0; i < maxSteps; i++) bar.append("#");
            bar.append("B] Step ").append(maxSteps).append("/").append(maxSteps)
               .append(" (Boss Gate Reached)");
        } else {
            for (int i = 0; i < maxSteps; i++) {
                if (i < steps) bar.append("#");
                else bar.append("-");
            }
            bar.append("B] Step ").append(steps).append("/").append(maxSteps);
            if (steps == 0) {
                bar.append(" (Safe Zone)");
            }
        }

        TextEffect.typeWriter(bar.toString(), 40);
    }
}
