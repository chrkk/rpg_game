package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.skills.Skill; 
import rpg.items.Consumable;
import rpg.items.Weapon;
import rpg.game.GameState;

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state;

    public CombatSystem(GameState state) {
        this.state = state;
    }

    public boolean startCombat(Player player, Enemy enemy) {
        TextEffect.typeWriter("⚔️ Combat Start! " + enemy.getName() + " appears!", 50);

        try {
            while (player.isAlive() && enemy.isAlive()) {
                try {
                    // --- Player Turn ---
                    TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                            " | Enemy HP: " + enemy.getHp(), 40);

                    if (state.skillsUnlocked) {
                        TextEffect.typeWriter("Choose action: attack / defend / skill / item / run", 30);
                    } else {
                        TextEffect.typeWriter("Choose action: attack / defend / item / run", 30);
                    }

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
                                    TextEffect.typeWriter("You swing your fists, but you have no weapon equipped!", 40);
                                    int dmg = 1 + rand.nextInt(2);
                                    enemy.takeDamage(dmg);
                                }
                            } catch (Exception e) {
                                TextEffect.typeWriter("Your attack falters unexpectedly.", 40);
                                System.err.println("Attack error -> " + e.getMessage());
                            }
                            break;

                        case "defend":
                            TextEffect.typeWriter("You brace yourself, reducing incoming damage.", 40);
                            defended = true;
                            break;

                        case "skill":
                            try {
                                if (!state.skillsUnlocked) {
                                    TextEffect.typeWriter("You haven’t unlocked your skills yet!", 40);
                                    break;
                                }
                                SkillSystem.useSkill(player, enemy, scanner);
                            } catch (Exception e) {
                                TextEffect.typeWriter("Skill use failed.", 40);
                                System.err.println("Skill error -> " + e.getMessage());
                            }
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
                                TextEffect.typeWriter("Item use failed.", 40);
                                System.err.println("Item error -> " + e.getMessage());
                            }
                            break;

                        case "run":
                            try {
                                if (rand.nextInt(100) < 50) {
                                    TextEffect.typeWriter("You successfully escaped!", 40);
                                    return false;
                                } else {
                                    TextEffect.typeWriter("You failed to escape!", 40);
                                }
                            } catch (Exception e) {
                                TextEffect.typeWriter("You stumble while trying to run.", 40);
                                System.err.println("Run error -> " + e.getMessage());
                            }
                            break;

                        case "kill":
                            enemy.takeDamage(enemy.getHp());
                            TextEffect.typeWriter("💀 [DEV] You unleash a hidden power... the enemy is instantly slain!", 40);
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
                            System.err.println("Enemy action error -> " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    TextEffect.typeWriter("Something went wrong during your turn.", 40);
                    System.err.println("Combat loop error -> " + e.getMessage());
                } finally {
                    // Runs every turn — could be used for logging or state checks
                }
            }
        } catch (Exception e) {
            TextEffect.typeWriter("A critical error occurred in combat.", 40);
            System.err.println("Combat system error -> " + e.getMessage());
        }

        // --- Outcome ---
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);

            if (enemy == state.currentZoneBoss) {
                state.revivalPotions++;
                TextEffect.typeWriter("✨ As the boss falls, you discover a glowing Revival Potion!", 50);
            }

            TextEffect.typeWriter("You loot: Food, Crystals, Shards, Materials.", 50);

            int expGained = enemy.getExpReward();
            player.gainExp(expGained);
            TextEffect.typeWriter("You gained " + expGained + " EXP!", 50);

            return true;
        } else {
            TextEffect.typeWriter("💀 You were defeated... Respawning at checkpoint.", 50);
            player.healFull();
            return false;
        }
    }
}
