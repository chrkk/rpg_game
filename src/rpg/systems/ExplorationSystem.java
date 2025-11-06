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

            // 🆕 If boss gate discovered, interpret numeric input
            if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
                switch (dir) {
                    case "1": // Safe Zone
                        handleBackward(state, safeZoneAction, zone, player, rand);
                        break;
                    case "2": // Farm
                        handleForward(player, scanner, rand, state, zone);
                        break;
                    case "3": // Boss
                        handleBoss(player, scanner, state, zone, safeZoneAction, rand);
                        break;
                    default:
                        TextEffect.typeWriter("Invalid choice. Please enter 1, 2, or 3.", 40);
                }
            } else {
                // Before discovery → text commands
                switch (dir.toLowerCase()) {
                    case "backward":
                        handleBackward(state, safeZoneAction, zone, player, rand);
                        break;
                    case "forward":
                        handleForward(player, scanner, rand, state, zone);
                        break;
                    default:
                        TextEffect.typeWriter("Invalid direction.", 40);
                }
            }

        } catch (Exception e) {
            TextEffect.typeWriter("An unexpected error occurred while exploring.", 40);
            System.err.println("Exploration error -> " + e.getMessage());
        }
    }

    // --- Prompt ---

    private static void showPrompt(GameState state) {
        if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
            // After gate discovery → numeric menu
            TextEffect.typeWriter("Choose your action:\n1) Enter Safe Zone\n2) Farm\n3) Challenge Boss", 40);
        } else {
            // Before discovery → step-based exploration
            TextEffect.typeWriter("Do you move forward or backward? (forward/backward)", 40);
        }
    }

    // --- Backward / Safe zone ---

    private static void handleBackward(GameState state, Runnable safeZoneAction,
            ZoneConfig zone, Player player, Random rand) {
        try {
            if (state.bossGateDiscovered) {
                // After boss gate discovery → mostly safe retreat, but small chance of mob
                int chance = rand.nextInt(100);
                if (chance < 10) { // 10% chance
                    Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                    TextEffect.typeWriter("⚠️ An enemy suddenly blocks your retreat!", 60);
                    CombatSystem combat = new CombatSystem(state);
                    if (combat.startCombat(player, mob)) {
                        LootSystem.dropLoot(state);
                    }
                } else {
                    TextEffect.typeWriter("🏃 You retreat swiftly back to the safe zone.", 60);
                }
                state.inSafeZone = true;
                safeZoneAction.run();
                return;
            }

            // Before gate discovery → step-based retreat
            if (state.forwardSteps > 0) {
                state.forwardSteps--; // decrement steps
                TextEffect.typeWriter("🔙 You retrace your steps cautiously...", 50);

                if (state.forwardSteps == 0) {
                    state.inSafeZone = true;
                    TextEffect.typeWriter("🏠 You have returned safely to the safe zone.", 60);
                    safeZoneAction.run();
                } else {
                    // 🆕 Chance-based encounter (e.g., 40% chance)
                    if (rand.nextInt(100) < 40) {
                        Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                        TextEffect.typeWriter("⚔️ A " + mob.getName() + " lurks in the shadows as you retreat!", 60);
                        CombatSystem combat = new CombatSystem(state);
                        if (combat.startCombat(player, mob)) {
                            LootSystem.dropLoot(state);
                        }
                    } else {
                        TextEffect.typeWriter("The path is eerily quiet... you slip back without incident.", 50);
                    }
                }
            } else {
                // Already at safe zone
                state.inSafeZone = true;
                safeZoneAction.run();
            }
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong while retreating.", 40);
            System.err.println("Backward error -> " + e.getMessage());
        }
    }

    // --- Forward / Exploration ---

    private static void handleForward(Player player, Scanner scanner, Random rand,
            GameState state, ZoneConfig zone) {
        state.inSafeZone = false;

        // 🆕 If boss gate already discovered, forward becomes farm
        if (state.bossGateDiscovered) {
            spawnMob(state, zone, player, rand);
            return;
        }

        // Normal exploration before gate discovery
        state.forwardSteps++;
        narrateZoneExit(state);

        if (state.zone == 2 && !state.skillsUnlocked) {
            TextEffect.typeWriter("\nAs you step out of the Ruined Lab, a surge of power awakens within you...", 50);
            TextEffect.typeWriter("Your class skills are now available! Use them in combat with the 'skill' command.\n",
                    50);
            state.skillsUnlocked = true;
        }

        if (checkStatueEncounter(state, zone, rand))
            return;

        try {
            if (state.forwardSteps >= 5 && !state.bossGateDiscovered) {
                if (!BossGateSystem.canFightBoss(state, player, () -> {
                })) {
                    TextEffect.typeWriter(
                            "⛔ The boss gate remains sealed. You must grow stronger or craft the required weapon before you can face it.",
                            60);
                } else {
                    TextEffect.typeWriter(
                            "🔥 You arrive at the boss gate. New move options are available: farm, retreat, or challenge the boss.",
                            60);

                }
                state.bossGateDiscovered = true;
            }
            spawnMob(state, zone, player, rand);
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during exploration.", 40);
            System.err.println("Forward error -> " + e.getMessage());
        }
    }

    // --- Boss command ---

    private static void handleBoss(Player player, Scanner scanner, GameState state,
            ZoneConfig zone, Runnable safeZoneAction, Random rand) {
        if (state.forwardSteps < 5) {
            TextEffect.typeWriter("🚪 You haven’t reached the boss gate yet. Keep moving forward.", 60);
            return;
        }

        try {
            if (!state.bossGateDiscovered) {
                // First encounter: fight or farm
                TextEffect.typeWriter("🔥 You stand before the boss gate.\n1) Challenge Boss\n2) Farm", 60);
                String choice = scanner.nextLine();
                if (choice.equals("1"))
                    startBossFight(player, state, zone, safeZoneAction);
                else if (choice.equals("2")) {
                    state.bossGateDiscovered = true;
                    spawnMob(state, zone, player, rand);
                } else
                    TextEffect.typeWriter("Invalid choice. Enter 1 or 2.", 40);
            } else {
                // After discovery: safe zone / farm / boss
                TextEffect.typeWriter("Choose your action:\n1) Enter Safe Zone\n2) Farm\n3) Challenge Boss", 40);
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        state.inSafeZone = true;
                        safeZoneAction.run();
                        break;
                    case "2":
                        spawnMob(state, zone, player, rand);
                        break;
                    case "3":
                        if (!BossGateSystem.canFightBoss(state, player, safeZoneAction)) {
                            TextEffect.typeWriter(
                                    "⛔ The boss gate remains sealed. You must meet the requirements before you can fight.",
                                    60);
                        } else
                            startBossFight(player, state, zone, safeZoneAction);
                        break;
                    default:
                        TextEffect.typeWriter("Invalid choice. Enter 1, 2, or 3.", 40);
                }
            }
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during the boss encounter.", 40);
            System.err.println("Boss error -> " + e.getMessage());
            state.inSafeZone = true;
            safeZoneAction.run();
        }
    }

    // --- Helper for boss fight outcome ---
    private static void startBossFight(Player player, GameState state, ZoneConfig zone, Runnable safeZoneAction) {
        CombatSystem combat = new CombatSystem(state);
        boolean win = combat.startCombat(player, zone.boss);
        if (win) {
            TextEffect.typeWriter("🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);
            state.zone++;
            state.forwardSteps = 0;
            state.bossGateDiscovered = false; // reset for next zone
            state.inSafeZone = true;
            safeZoneAction.run();
        } else {
            TextEffect.typeWriter("You awaken back at the safe zone...", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
        }
    }

    // --- Narration ---

    private static void narrateZoneExit(GameState state) {
        if (state.forwardSteps != 1)
            return;

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
        if (state.zone <= 1)
            return false; // only after Stage 1
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
