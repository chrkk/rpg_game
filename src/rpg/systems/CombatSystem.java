package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.characters.Supporter;
import rpg.skills.Skill;
import rpg.items.Consumable;
import rpg.items.Weapon;
import rpg.game.GameState;

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

        try {
            while (player.isAlive() && enemy.isAlive()) {
                // increment turn counter at the start of the player's turn
                turnCount++;
                try {
                    // --- Player Turn ---
                    TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                            " | Enemy HP: " + enemy.getHp(), 40);
                    
                    // ✅ Show skill option only if player has unlocked skills (Level >= 2 and skills assigned)
                    if (player.getLevel() >= 2 && player.getSkills().length > 0) {
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
                                // NEW: Show dynamic item menu
                                showItemMenu(player);
                                defended = true; //on defense para di masayang ag gi heal
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
                                    "The " + enemy.getName() + " attacks! You take " + actualDamage + " damage.", 40);
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

    // 🆕 NEW: Show available items in combat
    private void showItemMenu(Player player) {
        TextEffect.typeWriter("\n📦 Available Items:", 30);
        
        int optionNumber = 1;
        boolean hasItems = false;
        
        // Show meat if available
        if (state.meat > 0) {
            TextEffect.typeWriter(optionNumber + ". Meat (Heal 10 HP) x" + state.meat, 30);
            hasItems = true;
            optionNumber++;
        }
        
        // Show revival potions (but can't use in combat)
        if (state.revivalPotions > 0) {
            TextEffect.typeWriter(optionNumber + ". Revival Potion x" + state.revivalPotions + " (Can't use in combat)", 30);
            hasItems = true;
            optionNumber++;
        }
        
        if (!hasItems) {
            TextEffect.typeWriter("Your bag is empty! No consumables available.", 40);
            return;
        }
        
        TextEffect.typeWriter("0. Cancel", 30);
        
        System.out.print("> Choose item: ");
        String choice = scanner.nextLine();
        
        try {
            int option = Integer.parseInt(choice);
            
            if (option == 0) {
                TextEffect.typeWriter("You close your bag.", 40);
                return;
            }
            
            // Use the selected item
            useItemByOption(option, player);
            
        } catch (NumberFormatException e) {
            TextEffect.typeWriter("Invalid choice!", 40);
        }
    }

    // 🆕 NEW: Handle item usage based on selection
    private void useItemByOption(int option, Player player) {
        int currentOption = 1;
        
        // Option 1: Meat (if available)
        if (state.meat > 0) {
            if (option == currentOption) {
                // Check if player is at max HP
                if (player.getHp() == player.getMaxHp()) {
                    TextEffect.typeWriter("Your HP is already full! No need to use Meat.", 40);
                    return;
                }
                
                Consumable meat = new Consumable("Meat", 10);
                meat.consume(player, state);
                return;
            }
            currentOption++;
        }
        
        // Option 2: Revival Potion (if available)
        if (state.revivalPotions > 0) {
            if (option == currentOption) {
                TextEffect.typeWriter("Revival Potions can only be used outside combat!", 40);
                return;
            }
            currentOption++;
        }
        
        TextEffect.typeWriter("Invalid choice!", 40);
    }
}