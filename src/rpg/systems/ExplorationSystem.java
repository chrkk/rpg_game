package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.world.ZoneConfig;
import rpg.world.WorldData;

public class ExplorationSystem {


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

    ZoneConfig zone = WorldData.getZone(state.zone);

    if (dir.equalsIgnoreCase("backward")) {
        safeZoneAction.run();
    } else if (dir.equalsIgnoreCase("forward")) {
        state.forwardSteps++;
        if (state.forwardSteps >= 5) {
            CombatSystem combat = new CombatSystem();
            boolean win = combat.startCombat(player, zone.boss);
            if (win) {
                TextEffect.typeWriter("🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);
                state.zone++;
                state.forwardSteps = 0;
                safeZoneAction.run();
            } else {
                TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                safeZoneAction.run();
            }
        } else {
            int roll = rand.nextInt(100);
            Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
            CombatSystem combat = new CombatSystem();
            if (combat.startCombat(player, mob)) {
                int crystalDrop = 1 + rand.nextInt(2);
                int meatDrop = rand.nextInt(2);
                state.crystals += crystalDrop;
                state.meat += meatDrop;
                TextEffect.typeWriter("Loot: +" + crystalDrop + " Crystals, +" + meatDrop + " Meat.", 50);
            }
        }
    } else {
        TextEffect.typeWriter("Invalid direction.", 40);
     }
    }
}   
