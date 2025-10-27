package rpg.systems;

import java.util.Scanner;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.items.Consumable;
import rpg.game.GameState;

public class TutorialCombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private GameState state;

    public TutorialCombatSystem(GameState state) {
        this.state = state;
    }

    public boolean startTutorialCombat(Player player, Enemy enemy) {
        TextEffect.typeWriter("⚔️ Tutorial Combat! " + enemy.getName() + " blocks your path!", 50);

        while (player.isAlive() && enemy.isAlive()) {
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
                    if (state.meat > 0) {
                        Consumable meat = new Consumable("Meat", 10);
                        meat.consume(player, state);
                    } else {
                        TextEffect.typeWriter("You check your bag... but there's no item to consume!", 40);
                    }
                    break;

                case "run":
                    TextEffect.typeWriter("You try to run... but there’s nowhere to escape!", 40);
                    break;

                default:
                    TextEffect.typeWriter("Invalid action. You hesitate...", 40);
            }

            // --- Enemy Turn ---
            if (enemy.isAlive()) {
                int dmgTaken = enemy.enemyAction();
                if (defended) dmgTaken /= 2;

                int actualDamage = player.takeDamage(dmgTaken);
                TextEffect.typeWriter("The " + enemy.getName() + " attacks! You take " + actualDamage + " damage.", 40);
            }
        }

        // --- Outcome ---
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);

            // EXP introduction + actual gain
            TextEffect.typeWriter("You feel a strange warmth flow through your body...", 70);
            TextEffect.typeWriter("[Narrator] This is experience. With enough battles, you will grow stronger and unlock new powers.", 70);

            int expGained = enemy.getExpReward();
            player.gainExp(expGained);
            TextEffect.typeWriter("You gained " + expGained + " EXP!", 50);

            return true;
        } else {
            TextEffect.typeWriter("💀 You were defeated... but this is only training. You awaken again, determined.", 50);
            player.healFull();
            return false;
        }
    }
}
