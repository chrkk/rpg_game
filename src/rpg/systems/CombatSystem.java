package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.skills.Skill; // 🆕 Newly added - Import Skill base class
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

        while (player.isAlive() && enemy.isAlive()) {
            // --- Player Turn ---
            TextEffect.typeWriter("\nYour HP: " + player.getHp() + "/" + player.getMaxHp() +
                    " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                    " | Enemy HP: " + enemy.getHp(), 40);

            // 🆕 Newly added - Added "skill" as an action choice
            TextEffect.typeWriter("Choose action: attack / defend / skill / item / run", 30);
            System.out.print("> ");
            String action = scanner.nextLine();

            boolean defended = false;

            switch (action.toLowerCase()) {
                case "attack":
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
                    break;

                case "defend":
                    TextEffect.typeWriter("You brace yourself, reducing incoming damage.", 40);
                    defended = true;
                    break;

                // 🆕 Newly added - Skill usage system
                case "skill":
                    Skill[] skills = player.getSkills(); // get player’s 3 class-based skills
                    if (skills == null || skills.length == 0) {
                        TextEffect.typeWriter("You have no skills available!", 40);
                        break;
                    }

                    // Show list of available skills
                    TextEffect.typeWriter("\nChoose a skill to use:", 30);
                    for (int i = 0; i < skills.length; i++) {
                        System.out.println((i + 1) + ". " + skills[i].getName() + " (Mana Cost: " + skills[i].getManaCost() + ")");
                    }
                    System.out.print("> ");
                    String skillChoice = scanner.nextLine();

                    int chosenIndex;
                    try {
                        chosenIndex = Integer.parseInt(skillChoice) - 1;
                    } catch (NumberFormatException e) {
                        TextEffect.typeWriter("Invalid choice! You fumble your spellbook...", 40);
                        break;
                    }

                    if (chosenIndex < 0 || chosenIndex >= skills.length) {
                        TextEffect.typeWriter("That skill doesn’t exist!", 40);
                        break;
                    }

                    Skill chosenSkill = skills[chosenIndex];

                    // Check mana
                    if (player.getMana() < chosenSkill.getManaCost()) {
                        TextEffect.typeWriter("Not enough mana to use " + chosenSkill.getName() + "!", 40);
                        break;
                    }

                    // Consume mana and apply skill effect
                    player.useMana(chosenSkill.getManaCost());
                    int skillDamage = chosenSkill.getPower() + (player.getIntelligence() / 2);
                    enemy.takeDamage(skillDamage);

                    // Print skill text
                    TextEffect.typeWriter(chosenSkill.useSkill(), 40);
                    TextEffect.typeWriter("You dealt " + skillDamage + " damage using " + chosenSkill.getName() + "!", 40);
                    break;
                // 🆕 End of new Skill case

                case "item":
                    if (state.meat > 0) {
                        Consumable meat = new Consumable("Meat", 10);
                        meat.consume(player, state);
                    } else {
                        TextEffect.typeWriter("You check your bag... but there's no item to consume!", 40);
                    }
                    break;

                case "run":
                    if (rand.nextInt(100) < 50) {
                        TextEffect.typeWriter("You successfully escaped!", 40);
                        return false;
                    } else {
                        TextEffect.typeWriter("You failed to escape!", 40);
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
                int dmgTaken = enemy.enemyAction();
                if (defended) dmgTaken /= 2;

                int actualDamage = player.takeDamage(dmgTaken);
                TextEffect.typeWriter("The " + enemy.getName() + " attacks! You take " + actualDamage + " damage.", 40);
            }
        }

        // --- Outcome ---
        if (player.isAlive()) {
            TextEffect.typeWriter("🎉 You defeated the " + enemy.getName() + "!", 50);

            // Boss-specific loot and story
            if (enemy == state.currentZoneBoss) {
                state.revivalPotions++;
                TextEffect.typeWriter("✨ As the boss falls, you discover a glowing Revival Potion!", 50);
                // (Keep your existing boss dialogue)
            }

            TextEffect.typeWriter("You loot: Food, Crystals, Shards, Materials.", 50);

            // EXP reward (already implemented)
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
