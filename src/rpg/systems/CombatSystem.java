package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.items.Weapon;
import rpg.game.GameState;
// import rpg.systems.BagSystem; --> deleted

import rpg.ui.UIDesign; //new

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state;
    // Track combat turns (supporter state moved to SupporterSystem)
    private int turnCount = 0;
    private SupporterSystem supporterSystem = new SupporterSystem();

    public CombatSystem(GameState state) {
        this.state = state;
    }

    public boolean startCombat(Player player, Enemy enemy) {
        state.lootDisplayed = false;
        TextEffect.typeWriter("⚔️ Combat Start! " + enemy.getName() + " appears!", 50);

        // Apply per-combat supporter start effects via SupporterSystem
        supporterSystem.applyStartOfCombat(state, player);
        int enemyMaxHp = enemy.getHp();
        try {
            while (player.isAlive() && enemy.isAlive()) {
                // increment turn counter at the start of the player's turn
                turnCount++;
                try {
                    // --- Player Turn ---
                    // TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                    //         " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                    //         " | Enemy HP: " + enemy.getHp(), 40);
                    
                    //new ui display
                    // UIDesign.displayCombatFrame(player, enemy, enemyMaxHp, flavorText); --> will implement soon
                    UIDesign.displayCombatStatus(player, enemy, enemyMaxHp);
                    boolean hasSkills = player.getLevel() >= 2 && player.getSkills().length > 0;
                    UIDesign.displayCombatActions(hasSkills);

                    // ✅ Show skill option only if player has unlocked skills (Level >= 2 and skills assigned)
                    // if (player.getLevel() >= 2 && player.getSkills().length > 0) {
                    //     TextEffect.typeWriter("Choose action: attack / defend / skill / item / run", 30);
                    // } else {
                    //     TextEffect.typeWriter("Choose action: attack / defend / item / run", 30);
                    // }

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
                                // ✅ Check level and skills array instead of state.skillsUnlocked
                                if (player.getLevel() < 2 || player.getSkills().length == 0) {
                                    TextEffect.typeWriter("You haven't unlocked your skills yet!", 40);
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
                                StatusSystem.showItemMenu(state);
                                System.out.print("> Choose item: ");
                                String itemChoice = scanner.nextLine();

                                try {
                                    int itemOption = Integer.parseInt(itemChoice);
                                    if (itemOption == 0) {
                                        TextEffect.typeWriter("You close your bag.", 40);
                                    } else {
                                        boolean handled = StatusSystem.handleItemSelection(state, player, itemOption);
                                        if (!handled) {
                                            TextEffect.typeWriter("Invalid choice or item unavailable!", 40);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    TextEffect.typeWriter("Invalid choice!", 40);
                                }
                            } catch (Exception e) {
                                TextEffect.typeWriter("Something went wrong while using items!", 40);
                                System.err.println("Item use error -> " + e.getMessage());
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
                            TextEffect.typeWriter(
                                    "💀 [DEV] You unleash a hidden power... the enemy is instantly slain!", 40);
                            break;

                        default:
                            TextEffect.typeWriter("Invalid action. You hesitate...", 40);
                    }

                    // Let SupporterSystem handle per-turn supporter actions
                    supporterSystem.performSupporterTurns(state, player, enemy, turnCount, rand);

                    // --- Enemy Turn ---
                    if (enemy.isAlive()) {
                        try {
                            int dmgTaken = enemy.enemyAction();
                            if (defended)
                                dmgTaken /= 2;

                            int actualDamage = player.takeDamage(dmgTaken);
                            TextEffect.typeWriter(
                                    "The [ " + enemy.getName() + " ] attacks! You take " + actualDamage + " damage.", 40);
                        } catch (Exception e) {
                            TextEffect.typeWriter("The enemy falters in its attack.", 40);
                            System.err.println("Enemy action error -> " + e.getMessage());
                        }
                    }

                    // SupporterSystem decrements its own timers; nothing to do here

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
        // Reset any temporary supporter buffs after combat
        try {
            supporterSystem.resetAfterCombat(player);
        } catch (Exception e) {
            System.err.println("Error resetting supporter state after combat -> " + e.getMessage());
        }
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);

            LootSystem.dropLoot(state);

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