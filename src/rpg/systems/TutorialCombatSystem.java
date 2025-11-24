package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.items.Consumable;
import rpg.game.GameState;
import rpg.items.Weapon;

public class TutorialCombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private GameState state;
    private Random rand = new Random();

    public TutorialCombatSystem(GameState state) {
        this.state = state;
    }

    public boolean startTutorialCombat(Player player, Enemy enemy) {
        // Updated to [System] style
        TextEffect.typeWriter("⚔️ [System] > Combat Initiated! " + enemy.getName() + " blocks your path!", 50);

        try {
            while (player.isAlive() && enemy.isAlive()) {
                try {
                    TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                                          " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                                          " | Enemy HP: " + enemy.getHp(), 40);

                    TextEffect.typeWriter("Choose action: attack / defend / item / run", 30);
                    System.out.print("> ");
                    String action = scanner.nextLine();

                    boolean defended = false;

                    switch (action.toLowerCase()) {
                        case "attack":
                            try {
                                Weapon weapon = player.getWeapon();
                                if (weapon != null) {
                                    int dmg = weapon.rollDamage(rand) + (player.getIntelligence() / 2);
                                    enemy.takeDamage(dmg);
                                    TextEffect.typeWriter("You strike with your " + weapon.getName() +
                                                          " for " + dmg + " damage!", 40);
                                } else {
                                    int dmg = 1 + rand.nextInt(2);
                                    enemy.takeDamage(dmg);
                                    TextEffect.typeWriter("You punch wildly for " + dmg + " damage!", 40);
                                }
                            } catch (Exception e) {
                                TextEffect.typeWriter("Your attack falters unexpectedly.", 40);
                                System.err.println("Tutorial attack error -> " + e.getMessage());
                            }
                            break;

                        case "defend":
                            TextEffect.typeWriter("You brace yourself, reducing incoming damage.", 40);
                            defended = true;
                            break;

                        case "item":
                            try {
                                if (state.meat > 0) {
                                    Consumable meat = new Consumable("Meat", 10);
                                    meat.consume(player, state);
                                } else {
                                    TextEffect.typeWriter("You check your bag... but there's no item to consume!", 40);
                                }
                            } catch (Exception e) {
                                TextEffect.typeWriter("You fumble with your items.", 40);
                                System.err.println("Tutorial item error -> " + e.getMessage());
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
                        try {
                            int dmgTaken = enemy.enemyAction();
                            if (defended) dmgTaken /= 2;

                            int actualDamage = player.takeDamage(dmgTaken);
                            TextEffect.typeWriter("The " + enemy.getName() + " attacks! You take " + actualDamage + " damage.", 40);
                        } catch (Exception e) {
                            TextEffect.typeWriter("The enemy falters in its attack.", 40);
                            System.err.println("Tutorial enemy action error -> " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    TextEffect.typeWriter("Something went wrong during your turn.", 40);
                    System.err.println("Tutorial combat loop error -> " + e.getMessage());
                } finally {
                    // Runs every turn — could be used for logging or state checks
                }
            }
        } catch (Exception e) {
            TextEffect.typeWriter("A critical error occurred in the tutorial combat.", 40);
            System.err.println("TutorialCombatSystem error -> " + e.getMessage());
        }

        // --- Outcome ---
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);

            TextEffect.typeWriter("You feel a strange warmth flow through your body...", 70);
            
            // ✅ CHANGED HERE: Narrator -> System
            TextEffect.typeWriter("[System] > This is experience. With enough battles, you will grow stronger and unlock new powers.", 70);

            int expGained = enemy.getExpReward();
            player.gainExp(expGained);
            TextEffect.typeWriter("You gained " + expGained + " EXP!", 50);

            return true;
        } else {
            TextEffect.typeWriter("[System] > 💀 You were defeated... but this is only training. You awaken again, determined.", 50);
            player.healFull();
            return false;
        }
    }
}