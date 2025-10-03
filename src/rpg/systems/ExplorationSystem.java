package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class ExplorationSystem {

    public static void explorationLoop(Player player, Scanner scanner, Random rand, GameState state) {
    boolean exploring = true;

    while (exploring) {
        System.out.print("> (craft / search / status / move): ");
        String command = scanner.nextLine();

        switch (command.toLowerCase()) {
            case "craft":
                state.crystals = rpg.systems.CraftingSystem.craftWeapon(player, state.crystals);
                break;
            case "search":
                TextEffect.typeWriter("You scavenge the area... but find nothing new outside combat.", 60);
                break;
            case "status":
                rpg.systems.StatusSystem.showStatus(player, state.crystals, state.meat);
                break;
            case "move":
                handleMove(player, scanner, rand, state,
                        () -> rpg.systems.SafeZoneSystem.enterSafeZone(player, state));
                break;
            default:
                TextEffect.typeWriter("Unknown command.", 40);
        }
    }
}


    public static void handleMove(
            Player player,
            Scanner scanner,
            Random rand,
            GameState state,
            Runnable safeZoneAction
    ) {
        TextEffect.typeWriter("Do you move forward or backward?", 40);
        System.out.print("> ");
        String dir = scanner.nextLine();

        if (dir.equalsIgnoreCase("backward")) {
            safeZoneAction.run();
        } else if (dir.equalsIgnoreCase("forward")) {
            state.forwardSteps++;
            if (state.forwardSteps >= 5) {
                Enemy boss = new Enemy("Stone Titan", 120, 15, 25);
                rpg.systems.CombatSystem combat = new rpg.systems.CombatSystem();
                boolean win = combat.startCombat(player, boss);
                if (win) {
                    TextEffect.typeWriter("🏆 You defeated the Boss! A new safe zone awaits...", 80);
                    state.zone++;
                    state.forwardSteps = 0;
                    safeZoneAction.run();
                } else {
                    TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                    safeZoneAction.run();
                }
            } else {
                int roll = rand.nextInt(100);
                if (roll < 70) {
                    Enemy mob = new Enemy("Wild Beast", 40, 8, 12);
                    rpg.systems.CombatSystem combat = new rpg.systems.CombatSystem();
                    if (combat.startCombat(player, mob)) {
                        int crystalDrop = 1 + rand.nextInt(2);
                        int meatDrop = rand.nextInt(2);
                        state.crystals += crystalDrop;
                        state.meat += meatDrop;
                        TextEffect.typeWriter("Loot: +" + crystalDrop + " Crystals, +" + meatDrop + " Meat.", 50);
                    }
                } else if (roll < 90) {
                    Enemy elite = new Enemy("Corrupted Guardian", 60, 12, 18);
                    rpg.systems.CombatSystem combat = new rpg.systems.CombatSystem();
                    if (combat.startCombat(player, elite)) {
                        int crystalDrop = 2 + rand.nextInt(3);
                        int meatDrop = 1;
                        state.crystals += crystalDrop;
                        state.meat += meatDrop;
                        TextEffect.typeWriter("Loot: +" + crystalDrop + " Crystals, +" + meatDrop + " Meat.", 50);
                    }
                } else {
                    TextEffect.typeWriter("The path is eerily quiet... nothing happens.", 60);
                }
            }
        } else {
            TextEffect.typeWriter("Invalid direction.", 40);
        }
    }
}
