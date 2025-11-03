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
            Runnable safeZoneAction) {
        TextEffect.typeWriter("Do you move forward or backward?", 40);
        System.out.print("> ");
        String dir = scanner.nextLine();

        ZoneConfig zone = WorldData.getZone(state.zone);
        state.currentZoneBoss = zone.boss;

        if (dir.equalsIgnoreCase("backward")) {
            // entering safe zone
            state.inSafeZone = true;
            safeZoneAction.run();

        } else if (dir.equalsIgnoreCase("forward")) {
            // leaving safe zone
            state.inSafeZone = false;
            state.forwardSteps++;

            // 🆕 Unlock skills when leaving SafeZone2 for the first time
            if (state.zone == 2 && !state.skillsUnlocked) {
                TextEffect.typeWriter(
                        "\nAs you step out of the rooftop safe zone, a surge of power awakens within you...", 50);
                TextEffect.typeWriter(
                        "Your class skills are now available! Use them in combat with the 'skill' command.", 50);
                state.skillsUnlocked = true;
            }

            if (state.forwardSteps >= 5) {
                // 🛡️ Check if player can fight boss (weapon requirement)
                if (!BossGateSystem.canFightBoss(state, player, safeZoneAction)) {
                    return; // stop here if requirement not met
                }

                // ✅ If requirement met, proceed to boss fight
                CombatSystem combat = new CombatSystem(state);
                boolean win = combat.startCombat(player, zone.boss);
                if (win) {
                    TextEffect.typeWriter("🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);
                    state.zone++;
                    state.forwardSteps = 0;
                    state.inSafeZone = true;
                    safeZoneAction.run();
                } else {
                    TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                    state.inSafeZone = true;
                    safeZoneAction.run();
                }
            } else {
                // Random mob encounter
                Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                CombatSystem combat = new CombatSystem(state);
                if (combat.startCombat(player, mob)) {
                    // 🆕 delegate loot handling
                    LootSystem.dropLoot(state); // ✅ fixed: only pass GameState
                }
            }
        } else {
            TextEffect.typeWriter("Invalid direction.", 40);
        }
    }
}
