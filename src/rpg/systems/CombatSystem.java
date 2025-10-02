package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();

    public boolean startCombat(Player player, Enemy enemy) {
        TextEffect.typeWriter("⚔️ Combat Start! " + enemy.getName() + " appears!", 50);

        while (player.isAlive() && enemy.isAlive()) {
            // --- Player Turn ---
            TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                                  " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                                  " | Enemy HP: " + enemy.getHp(), 40);

            TextEffect.typeWriter("Choose action: attack / defend / item / run", 30);
            System.out.print("> ");
            String action = scanner.nextLine();

            boolean defended = false;

            switch (action.toLowerCase()) {
                case "attack":
                    int dmg = player.getWeapon().getDamage() + (player.getIntelligence() / 2);
                    enemy.takeDamage(dmg);
                    TextEffect.typeWriter("You strike with your " + player.getWeapon().getName() +
                    " for " + dmg + " damage!", 40);
                    break;

                case "defend":
                    TextEffect.typeWriter("You brace yourself, reducing incoming damage.", 40);
                    defended = true;
                    break;

                case "item":
                    TextEffect.typeWriter("You rummage through your bag... (future: healing items, buffs)", 40);
                    break;

                case "run":
                    if (rand.nextInt(100) < 50) {
                        TextEffect.typeWriter("You successfully escaped!", 40);
                        return false; // escaped
                    } else {
                        TextEffect.typeWriter("You failed to escape!", 40);
                    }
                    break;

                default:
                    TextEffect.typeWriter("Invalid action. You hesitate...", 40);
            }

            // --- Enemy Turn ---
            if (enemy.isAlive()) {
                int dmgTaken = enemy.enemyAction();
                if (defended) dmgTaken /= 2;

                player.takeDamage(dmgTaken);
                TextEffect.typeWriter("The " + enemy.getName() + " attacks! You take " + dmgTaken + " damage.", 40);
            }
        }

        // --- Outcome ---
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);
            TextEffect.typeWriter("You loot: Food, Crystals, Shards, Materials.", 50);
            return true;
        } else {
            TextEffect.typeWriter("💀 You were defeated... Respawning at checkpoint.", 50);
            player.healFull(); // respawn with full stats
            return false;
        }
    }
}
