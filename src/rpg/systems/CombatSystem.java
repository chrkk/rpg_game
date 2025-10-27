package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;

//new imports for meat consumable
import rpg.items.Consumable;
import rpg.game.GameState;

public class CombatSystem {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    // GameState state = new GameState();
    private GameState state; //new

    //new constuctor to rcv current gameState values
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
            
            // 🆕 Give EXP to player ---> new
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
