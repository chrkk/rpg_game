package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;

public class TutorialCombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();

    public boolean startTutorialCombat(Player player, Enemy enemy) {
        TextEffect.typeWriter("⚔️ Tutorial Combat! " + enemy.getName() + " blocks your path!", 50);

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
                    // In tutorial, running is disabled
                    TextEffect.typeWriter("You try to run... but there’s nowhere to escape!", 40);
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

            // EXP introduction
            TextEffect.typeWriter("You feel a strange warmth flow through your body...", 70);
            TextEffect.typeWriter("[Narrator] This is experience. With enough battles, you will grow stronger and unlock new powers.", 70);

            // Later, when Role 1 finishes EXP system:
            // player.gainExp(enemy.getExpReward());

            return true;
        } else {
            TextEffect.typeWriter("💀 You were defeated... but this is only training. You awaken again, determined.", 50);
            player.healFull(); // respawn with full stats
            return false;
        }
    }
}
