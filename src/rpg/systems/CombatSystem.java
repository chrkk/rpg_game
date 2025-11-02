package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;

// new imports for meat consumable
import rpg.items.Consumable;
import rpg.items.Weapon;
import rpg.game.GameState;

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state;

    // constructor to receive current gameState values
    public CombatSystem(GameState state) {
        this.state = state; // store the shared game state
    }

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
                    Weapon weapon = player.getWeapon();
                    if (weapon != null) {
                        // 🎲 roll dynamic damage (range + crit)
                        int dmg = weapon.rollDamage(rand) + (player.getIntelligence() / 2);
                        enemy.takeDamage(dmg);
                        TextEffect.typeWriter("You strike with your " + weapon.getName() +
                                " for " + dmg + " damage!", 40);
                    } else {
                        TextEffect.typeWriter("You swing your fists, but you have no weapon equipped!", 40);
                        int dmg = 1 + rand.nextInt(2); // barehand fallback
                        enemy.takeDamage(dmg);
                    }
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
                    if (rand.nextInt(100) < 50) {
                        TextEffect.typeWriter("You successfully escaped!", 40);
                        return false; // escaped
                    } else {
                        TextEffect.typeWriter("You failed to escape!", 40);
                    }
                    break;

                case "kill":
                    enemy.takeDamage(enemy.getHp()); // dev cheat
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

            // Boss-specific loot: Revival Potion
            if (enemy == state.currentZoneBoss) {
                state.revivalPotions++;
                TextEffect.typeWriter("✨ As the boss falls, you discover a glowing Revival Potion!", 50);

                // Narrative: Hallway encounter with Sir Khai
                if (state.revivalPotions > 0) {
                    System.out.println(
                            "\nYou step into a dimly lit hallway, its walls cracked and lined with broken lockers...");
                    System.out.println(
                            "At the far end, you see a petrified statue of your teacher — Sir Khai, frozen mid-stance.");
                    System.out.print("Do you want to use a Revival Potion to awaken him? (yes/no): ");
                    String choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("yes")) {
                        state.revivalPotions--;
                        System.out.println("✨ The stone shell crumbles away... Sir Khai opens his eyes.");
                        System.out.println("\"You’ve done well to come this far,\" he says. \"Allow me to guide you onward.\"");
                        // Later: add Sir Khai as a Supporter in GameState
                    } else {
                        System.out.println("You clutch the potion tightly and walk past the statue, leaving Sir Khai in silence...");
                    }
                }
            }

            TextEffect.typeWriter("You loot: Food, Crystals, Shards, Materials.", 50);

            // Give EXP to player
            int expGained = enemy.getExpReward();
            player.gainExp(expGained);
            TextEffect.typeWriter("You gained " + expGained + " EXP!", 50);

            return true;
        } else {
            TextEffect.typeWriter("💀 You were defeated... Respawning at checkpoint.", 50);
            player.healFull(); // respawn with full stats
            return false;
        }
    }
}
