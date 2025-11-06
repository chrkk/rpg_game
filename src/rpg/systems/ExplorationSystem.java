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
import rpg.world.SupporterPool;

public class ExplorationSystem {

    public static void handleMove(
            Player player,
            Scanner scanner,
            Random rand,
            GameState state,
            Runnable safeZoneAction) {

        try {
            ZoneConfig zone = WorldData.getZone(state.zone);
            state.currentZoneBoss = zone.boss;

            showPrompt(state);
            System.out.print("> ");
            String dir = scanner.nextLine();

            switch (dir.toLowerCase()) {
                case "backward":
                    handleBackward(state, safeZoneAction);
                    break;

                case "forward":
                    handleForward(player, scanner, rand, state, zone);
                    break;

                case "boss":
                    handleBoss(player, scanner, state, zone, safeZoneAction);
                    break;

                default:
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

    // --- Prompt ---

    private static void showPrompt(GameState state) {
        if (state.forwardSteps >= 5) {
            TextEffect.typeWriter("Do you move backward, forward, or challenge the boss? (backward/forward/boss)", 40);
        } else {
            TextEffect.typeWriter("Do you move backward or forward? (backward/forward)", 40);
        }
    }

    // --- Backward / Safe zone ---

    private static void handleBackward(GameState state, Runnable safeZoneAction) {
        try {
            state.inSafeZone = true;
            safeZoneAction.run();
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong entering the safe zone.", 40);
            System.err.println("Safe zone error -> " + e.getMessage());
        }
    }

    // --- Forward / Exploration ---

    private static void handleForward(Player player, Scanner scanner, Random rand,
                                      GameState state, ZoneConfig zone) {
        state.inSafeZone = false;
        state.forwardSteps++;

        // Narration on first step out of the safe zone for the current stage
        narrateZoneExit(state);

        // Unlock skills when leaving SafeZone2 for the first time
        if (state.zone == 2 && !state.skillsUnlocked) {
            TextEffect.typeWriter("\nAs you step out of the Ruined Lab, a surge of power awakens within you...", 50);
            TextEffect.typeWriter("Your class skills are now available! Use them in combat with the 'skill' command.\n", 50);
            state.skillsUnlocked = true;
        }

        // Random statue encounter after Stage 1
        if (checkStatueEncounter(state, zone, rand)) {
            return; // statue event consumed the turn
        }

        // Boss gate and farming logic
        if (state.forwardSteps >= 5) {
            try {
                if (!state.bossGateDiscovered) {
                    // First time reaching the boss gate
                    if (!BossGateSystem.canFightBoss(state, player, () -> {})) {
                        TextEffect.typeWriter(
                                "⛔ The boss gate remains sealed. You must grow stronger or craft the required weapon before you can face it.",
                                60);
                    } else {
                        TextEffect.typeWriter(
                                "🔥 You arrive at the boss gate. Type 'boss' when you are ready to challenge "
                                        + zone.boss.getName() + ", or keep moving forward to farm mobs.",
                                60);
                    }
                    state.bossGateDiscovered = true; // mark as discovered
                }

                // Farming encounter (always happens when moving forward at/after the gate)
                spawnMob(state, zone, player, rand);

            } catch (Exception e) {
                TextEffect.typeWriter("Something went wrong during the gate encounter.", 40);
                System.err.println("Gate encounter error -> " + e.getMessage());
            }
        } else {
            // Normal mob encounters before the gate
            try {
                spawnMob(state, zone, player, rand);
            } catch (Exception e) {
                TextEffect.typeWriter("Something went wrong during the encounter.", 40);
                System.err.println("Mob encounter error -> " + e.getMessage());
            }
        }
    }

    // --- Boss command ---

    private static void handleBoss(Player player, Scanner scanner, GameState state,
                                   ZoneConfig zone, Runnable safeZoneAction) {
        try {
            if (state.forwardSteps >= 5) {
                if (!BossGateSystem.canFightBoss(state, player, safeZoneAction)) {
                    TextEffect.typeWriter(
                            "⛔ The boss gate remains sealed. You must meet the requirements before you can fight.",
                            60);
                    return;
                }

                CombatSystem combat = new CombatSystem(state);
                boolean win = combat.startCombat(player, zone.boss);
                if (win) {
                    TextEffect.typeWriter(
                            "🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);
                    state.zone++;
                    state.forwardSteps = 0;
                    state.bossGateDiscovered = false; // reset for the next stage’s gate
                    state.inSafeZone = true;
                    safeZoneAction.run();
                } else {
                    TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                    state.inSafeZone = true;
                    safeZoneAction.run();
                }
            } else {
                TextEffect.typeWriter("🚪 You haven’t reached the boss gate yet. Keep moving forward.", 60);
            }
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during the boss encounter.", 40);
            System.err.println("Boss fight error -> " + e.getMessage());
            state.inSafeZone = true;
            safeZoneAction.run();
        }
    }

    // --- Narration ---

    private static void narrateZoneExit(GameState state) {
        if (state.forwardSteps != 1) return;

        switch (state.zone) {
            case 1:
                TextEffect.typeWriter("\n🌌 You leave the safety of the School Rooftop behind...", 60);
                TextEffect.typeWriter("The path ahead winds through shattered classrooms and broken halls.", 60);
                TextEffect.typeWriter("Your goal: reach the next safe haven — the Ruined Lab.", 60);
                break;
            case 2:
                TextEffect.typeWriter(
                        "\n⚙️ You depart from the Ruined Lab, its broken machines fading into the distance...", 60);
                TextEffect.typeWriter("The road ahead twists through collapsed streets and burning wreckage.", 60);
                TextEffect.typeWriter("Your destination: the City Ruins, the next hub of survival.", 60);
                break;
            case 3:
                TextEffect.typeWriter(
                        "\n🏙️ You step out of the City Ruins, leaving behind its fragile refuge...", 60);
                TextEffect.typeWriter("The wasteland stretches before you, scarred by silence and danger.", 60);
                TextEffect.typeWriter("Your journey continues toward the next unknown landmark...", 60);
                break;
            default:
                TextEffect.typeWriter("\n🚶 You set out from the safe zone, heading toward your next destination.", 60);
                break;
        }
    }

    // --- Statue encounter ---

    private static boolean checkStatueEncounter(GameState state, ZoneConfig zone, Random rand) {
        if (state.zone <= 1) return false; // only after Stage 1
        int chance = rand.nextInt(100);
        if (chance < 10) { // 10% chance per forward step
            Supporter statue = SupporterPool.getRandomSupporter(state.zone, rand);
            if (statue != null) {
                TextEffect.typeWriter("🗿 A mysterious statue appears in the " + zone.name + "...", 60);
                ReviveSystem.randomRevive(state, statue);
                return true; // event consumed the turn
            }
        }
        return false;
    }

    // --- Mob spawning / farming ---

    private static void spawnMob(GameState state, ZoneConfig zone, Player player, Random rand) {
        Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
        TextEffect.typeWriter("⚔️ A " + mob.getName() + " prowls nearby!", 60);
        CombatSystem combat = new CombatSystem(state);
        if (combat.startCombat(player, mob)) {
            LootSystem.dropLoot(state);
        }
    }
}
