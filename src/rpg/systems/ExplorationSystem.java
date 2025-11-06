package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.characters.Supporter;
import rpg.characters.Enemy;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.world.ZoneConfig;
import rpg.world.WorldData;
import rpg.world.SupporterPool; // 🆕 import the supporter pool

public class ExplorationSystem {

    public static void handleMove(
            Player player,
            Scanner scanner,
            Random rand,
            GameState state,
            Runnable safeZoneAction) {

        try {
            TextEffect.typeWriter("Do you move forward or backward?", 40);
            System.out.print("> ");
            String dir = scanner.nextLine();

            ZoneConfig zone = WorldData.getZone(state.zone);
            state.currentZoneBoss = zone.boss;

            if (dir.equalsIgnoreCase("backward")) {
                try {
                    state.inSafeZone = true;
                    safeZoneAction.run();
                } catch (Exception e) {
                    TextEffect.typeWriter("Something went wrong entering the safe zone.", 40);
                    System.err.println("Safe zone error -> " + e.getMessage());
                }

            } else if (dir.equalsIgnoreCase("forward")) {
                state.inSafeZone = false;
                state.forwardSteps++;

                // 🆕 Safe zone exit narration (only once per zone)
                if (state.forwardSteps == 1) {
                    switch (state.zone) {
                        case 1:
                            TextEffect.typeWriter("\n🌌 You leave the safety of the School Rooftop behind...", 60);
                            TextEffect.typeWriter("The path ahead winds through shattered classrooms and broken halls.", 60);
                            TextEffect.typeWriter("Your goal: reach the next safe haven — the Ruined Lab.", 60);
                            break;
                        case 2:
                            TextEffect.typeWriter("\n⚙️ You depart from the Ruined Lab, its broken machines fading into the distance...", 60);
                            TextEffect.typeWriter("The road ahead twists through collapsed streets and burning wreckage.", 60);
                            TextEffect.typeWriter("Your destination: the City Ruins, the next hub of survival.", 60);
                            break;
                        case 3:
                            TextEffect.typeWriter("\n🏙️ You step out of the City Ruins, leaving behind its fragile refuge...", 60);
                            TextEffect.typeWriter("The wasteland stretches before you, scarred by silence and danger.", 60);
                            TextEffect.typeWriter("Your journey continues toward the next unknown landmark...", 60);
                            break;
                        default:
                            TextEffect.typeWriter("\n🚶 You set out from the safe zone, heading toward your next destination.", 60);
                            break;
                    }
                }

                // Unlock skills when leaving SafeZone2 for the first time
                if (state.zone == 2 && !state.skillsUnlocked) {
                    TextEffect.typeWriter(
                            "\nAs you step out of the rooftop safe zone, a surge of power awakens within you...", 50);
                    TextEffect.typeWriter(
                            "Your class skills are now available! Use them in combat with the 'skill' command.", 50);
                    state.skillsUnlocked = true;
                }

                // 🆕 Random statue encounter after Stage 1
                if (state.zone > 1) { // only after Stage 1
                    int chance = rand.nextInt(100);
                    if (chance < 10) { // 10% chance per forward step
                        Supporter statue = SupporterPool.getRandomSupporter(state.zone, rand);
                        if (statue != null) {
                            TextEffect.typeWriter("🗿 A mysterious statue appears in the "
                                    + zone.name + "...", 60);
                            ReviveSystem.randomRevive(state, statue);
                            return;
                        }
                    }
                }

                if (state.forwardSteps >= 5) {
                    try {
                        if (!BossGateSystem.canFightBoss(state, player, safeZoneAction)) {
                            return; // stop here if requirement not met
                        }

                        CombatSystem combat = new CombatSystem(state);
                        boolean win = combat.startCombat(player, zone.boss);
                        if (win) {
                            TextEffect.typeWriter(
                                    "🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);

                            // Tutorial: miniboss drops Revival Potion
                            TextEffect.typeWriter("✨ As the boss falls, you discover a glowing Revival Potion!", 60);
                            state.revivalPotions++;

                            // Scripted Sir Khai statue event
                            if (state.zone == 1 && !state.metSirKhai) {
                                Supporter sirKhai = new Supporter("Sir Khai", "Teacher", "Guidance");
                                ReviveSystem.scriptedRevive(state, sirKhai, scanner);
                            }

                            state.zone++;
                            state.forwardSteps = 0;
                            state.inSafeZone = true;
                            safeZoneAction.run();
                        } else {
                            TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                            state.inSafeZone = true;
                            safeZoneAction.run();
                        }
                    } catch (Exception e) {
                        TextEffect.typeWriter("Something went wrong during the boss encounter.", 40);
                        System.err.println("Boss fight error -> " + e.getMessage());
                        state.inSafeZone = true;
                        safeZoneAction.run();
                    }
                } else {
                    try {
                        Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                        TextEffect.typeWriter("⚔️ A " + mob.getName() + " emerges from the shadows!", 60);
                        CombatSystem combat = new CombatSystem(state);
                        if (combat.startCombat(player, mob)) {
                            LootSystem.dropLoot(state);
                        }
                    } catch (Exception e) {
                        TextEffect.typeWriter("Something went wrong during the encounter.", 40);
                        System.err.println("Mob encounter error -> " + e.getMessage());
                    }
                }

            } else {
                TextEffect.typeWriter("Invalid direction.", 40);
            }

        } catch (Exception e) {
            TextEffect.typeWriter("An unexpected error occurred while exploring.", 40);
            System.err.println("Exploration error -> " + e.getMessage());
        } finally {
            // Always runs after exploration attempt
            // Could be used for logging or ensuring state consistency
        }
    }
}
