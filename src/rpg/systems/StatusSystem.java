package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;

public class StatusSystem {
    public static void showStatus(Player player, int meat, int shards) {
        TextEffect.typeWriter(
            "\n------ " + player.getName() + ": The " + player.getTrait() + " ------" +
            "\n| HP: " + player.getHp() + "/" + player.getMaxHp() +
            " | Level: " + player.getLevel() +
            " | EXP: " + player.getExp() + "/" + player.getExpToNextLevel() +
            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
            " | Defense: " + player.getDefense() +
            " | Intelligence: " + player.getIntelligence() +
            " | Weapon: " + (player.getWeapon() == null ? "None" : player.getWeapon().toString()) +
            " | Shards: " + shards +          // ✅ only show shards
            " | Meat: " + meat,               // ✅ keep meat
            40
        );
    }
}
