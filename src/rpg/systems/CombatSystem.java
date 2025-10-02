package rpg.systems;

import java.util.Scanner;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);

    public boolean startCombat(Player player, Enemy enemy) {
        TextEffect.typeWriter("Combat Start! " + enemy.getName() + " appears!", 50);

        while (player.isAlive() && enemy.isAlive()) {
            // Player turn
            TextEffect.typeWriter("Your HP: " + player.getHp() + "/" + player.getMaxHp() +
                                  " | Enemy HP: " + enemy.getHp(), 40);
            TextEffect.typeWriter("Choose action: attack / run", 30);
            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("attack")) {
                int dmg = 15; // base damage
                enemy.takeDamage(dmg);
                TextEffect.typeWriter("You hit the " + enemy.getName() + " for " + dmg + " damage!", 40);
            } else if (action.equalsIgnoreCase("run")) {
                TextEffect.typeWriter("You manage to escape!", 40);
                return false; // escaped
            }

            // Enemy turn (if still alive)
            if (enemy.isAlive()) {
                int dmg = enemy.getAttack();
                player.takeDamage(dmg);
                TextEffect.typeWriter("The " + enemy.getName() + " bites you for " + dmg + " damage!", 40);
            }
        }

        if (player.isAlive()) {
            TextEffect.typeWriter("You defeated the " + enemy.getName() + "!", 50);
            return true;
        } else {
            TextEffect.typeWriter("You were defeated...", 50);
            return false;
        }
    }
}
