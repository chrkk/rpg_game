package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;

public class StatusSystem {
    public static void showStatus(Player player, int crystals, int meat) {
        TextEffect.typeWriter(
            "\n------ " + player.getName() + ": The " + player.getTrait() + " ------" +
            "\n| HP: " + player.getHp() + "/" + player.getMaxHp() +
            " | Level: " + player.getLevel() + // ---> new
            " | EXP: " + player.getExp() + "/" + player.getExpToNextLevel() + // ---> new
            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
            " | Defense: " + player.getDefense() +
            " | Intelligence: " + player.getIntelligence() +
            " | Weapon: " + (player.getWeapon() == null ? "None" : player.getWeapon().toString()) +
            " | Crystals: " + crystals +
            " | Meat: " + meat,
            40
        );
    }
}