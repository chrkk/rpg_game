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

    // ... [Logic for handleMove, showPrompt, etc. stays exactly the same] ...

    public static void handleMove(
            Player player,
            Scanner scanner,
            Random rand,
            GameState state,
            Runnable safeZoneAction) {
        
        // ... [Existing code] ...
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
                        handleBoss(player, dir, state, zone, safeZoneAction, rand);
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

    // ... [Keep showPrompt, handleBackward, handleForward, checkStatueEncounter, spawnMob exactly as they were] ...
    
    private static void showPrompt(GameState state) {
        if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
            TextEffect.typeWriter("Choose your action:\n1) Safe Zone\n2) Farm\n3) Boss", 40);
        } else {
            TextEffect.typeWriter("Do you move forward or backward? (forward/backward)", 40);
        }
    }

    private static void handleBackward(GameState state, Runnable safeZoneAction,
            ZoneConfig zone, Player player, Random rand) {

        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 50);
            return;
        }

        if (state.bossGateDiscovered) {
            if (rand.nextInt(100) < 10) {
                Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                TextEffect.typeWriter("⚠️ [System] > Alert: An enemy blocks your retreat!", 60);
                CombatSystem combat = new CombatSystem(state);
                if (combat.startCombat(player, mob))
                    LootSystem.dropLoot(state);
            } else {
                TextEffect.typeWriter("🏃 [System] > Retreat successful. Returning to Safe Zone.", 60);
            }
            enterSafeZone(state, safeZoneAction);
            return;
        }

        // step-based retreat before gate discovery
        if (state.forwardSteps > 0) {
            state.forwardSteps--;
            TextEffect.typeWriter("🔙 You retrace your steps cautiously...", 50);
            if (state.forwardSteps == 0) {
                TextEffect.typeWriter("🏠 [System] > You have returned safely to the safe zone.", 60);
                enterSafeZone(state, safeZoneAction);
            } else if (rand.nextInt(100) < 40) {
                Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                TextEffect.typeWriter("⚔️ A " + mob.getName() + " lurks in the shadows!", 60);
                CombatSystem combat = new CombatSystem(state);
                if (combat.startCombat(player, mob))
                    LootSystem.dropLoot(state);
            } else {
                TextEffect.typeWriter("The path is eerily quiet...", 50);
            }
        } else {
            enterSafeZone(state, safeZoneAction);
        }
    }

    private static void handleForward(Player player, Scanner scanner, Random rand,
            GameState state, ZoneConfig zone) {
        state.inSafeZone = false;

        // 🆕 If boss gate already discovered, forward becomes farm
        if (state.bossGateDiscovered) {
            // Allow statue encounters while farming (reuse same statue logic)
            if (checkStatueEncounter(state, zone, rand, scanner)) return;
            spawnMob(state, zone, player, rand);
            return;
        }

        // Normal exploration before gate discovery
        state.forwardSteps++;
        narrateZoneExit(state);

        if (checkStatueEncounter(state, zone, rand, scanner))
            return;

        try {
            if (state.forwardSteps >= 5 && !state.bossGateDiscovered) {
                if (!BossGateSystem.canFightBoss(state, player, () -> {
                })) {
                    TextEffect.typeWriter(
                            "⛔ [System] > Access Denied. The boss gate remains sealed. You must grow stronger or craft the required weapon.",
                            60);
                } else {
                    TextEffect.typeWriter(
                            "🔥 [System] > Boss Gate Reached. New options available: farm, retreat, or challenge the boss.",
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

    private static void handleBoss(Player player, String choice, GameState state,
            ZoneConfig zone, Runnable safeZoneAction, Random rand) {
        if (state.forwardSteps < 5) {
            TextEffect.typeWriter("🚪 You haven’t reached the boss gate yet.", 60);
            return;
        }

        try {
            if (!state.bossGateDiscovered) {
                // First time at the gate → prompt here
                TextEffect.typeWriter("🔥 You stand before the boss gate.\n1) Challenge Boss\n2) Farm", 60);
                String firstChoice = new Scanner(System.in).nextLine();
                if (firstChoice.equals("1"))
                    startBossFight(player, state, zone, safeZoneAction);
                else if (firstChoice.equals("2")) {
                    state.bossGateDiscovered = true;
                    spawnMob(state, zone, player, rand);
                } else
                    TextEffect.typeWriter("Invalid choice. Enter 1 or 2.", 40);
            } else {
                // Gate already discovered → use choice passed in
                switch (choice) {
                    case "1":
                        enterSafeZone(state, safeZoneAction);
                        break;
                    case "2":
                        spawnMob(state, zone, player, rand);
                        break;
                    case "3":
                        if (!BossGateSystem.canFightBoss(state, player, safeZoneAction))
                            TextEffect.typeWriter("⛔ [System] > Access Denied. The boss gate remains sealed.", 60);
                        else
                            startBossFight(player, state, zone, safeZoneAction);
                        break;
                    default:
                        TextEffect.typeWriter("Invalid choice. Enter 1, 2, or 3.", 40);
                }
            }
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during the boss encounter.", 40);
            System.err.println("Boss error -> " + e.getMessage());
            enterSafeZone(state, safeZoneAction);
        }
    }

    private static void startBossFight(Player player, GameState state, ZoneConfig zone, Runnable safeZoneAction) {
        // 🆕 Show boss intro dialogue
        addBossIntroDialogue(zone.boss, state.zone);
        
        CombatSystem combat = new CombatSystem(state);
        boolean win = combat.startCombat(player, zone.boss);

        if (win) {
            // 🆕 Show victory dialogue
            addBossVictoryDialogue(zone.boss, state.zone);
            
            TextEffect.typeWriter("🏆 [System] > Target Eliminated: " + zone.boss.getName() + "! Zone Clear.", 80);

            // 🆕 Trigger Sir Khai revival event after Zone 1 miniboss
            if (state.zone == 1) {
                handleMinibossDefeat(player, new Scanner(System.in), state);
            }

            state.zone++;
            state.forwardSteps = 0;
            state.bossGateDiscovered = false; // reset for next zone
            state.inSafeZone = true;
            safeZoneAction.run();
        } else {
            TextEffect.typeWriter("[System] > Critical Failure. Respawning at Safe Zone...", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
        }
    }

    private static void narrateZoneExit(GameState state) {
        if (state.forwardSteps != 1)
            return;

        switch (state.zone) {
            case 1:
                TextEffect.typeWriter("\n🌌 [System] > Leaving School Rooftop Safe Zone...", 60);
                TextEffect.typeWriter("The path ahead winds through shattered classrooms and broken halls.", 60);
                TextEffect.typeWriter("Current Objective: Reach the Ruined Lab.", 60);
                break;
            case 2:
                TextEffect.typeWriter(
                        "\n⚙️ [System] > Exiting Ruined Lab...", 60);
                TextEffect.typeWriter("The road ahead twists through collapsed streets and burning wreckage.", 60);
                TextEffect.typeWriter("Current Objective: Reach the City Ruins.", 60);
                break;
            case 3:
                TextEffect.typeWriter(
                        "\n🏙️ [System] > Leaving City Ruins...", 60);
                TextEffect.typeWriter("Broken sky bridges sway overhead, guiding you to the Fractured Sky.", 60);
                TextEffect.typeWriter("Current Objective: Reach the Observation Deck.", 60);
                break;
            case 4:
                TextEffect.typeWriter(
                        "\n🌠 [System] > Approaching The Source...", 60);
                TextEffect.typeWriter("The Source blazes ahead — blinding white and void black entwined.", 60);
                TextEffect.typeWriter("Warning: Final Confrontation Imminent.", 60);
                break;
            default:
                TextEffect.typeWriter("\n🚶 [System] > Moving to next sector.", 60);
                break;
        }
    }

    private static boolean checkStatueEncounter(GameState state, ZoneConfig zone, Random rand, java.util.Scanner scanner) {
        if (state.zone <= 1)
            return false; // only after Stage 1
        int chance = rand.nextInt(100);
        if (chance < 15) { // 15% chance per forward step (increased)
            Supporter statue = SupporterPool.getRandomUnrevivedSupporter(state.zone, rand, state);
            if (statue != null) {
                TextEffect.typeWriter("🗿 [System] > Anomaly Detected: A mysterious statue appears in the " + zone.name + "...", 60);
                ReviveSystem.randomRevive(state, statue, scanner);
                return true; // event consumed the turn
            }
        }
        return false;
    }

    private static void spawnMob(GameState state, ZoneConfig zone, Player player, Random rand) {
        Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
        TextEffect.typeWriter("⚔️ [System] > Hostile Entity Detected: " + mob.getName() + "!", 60);
        CombatSystem combat = new CombatSystem(state);
        if (combat.startCombat(player, mob)) {
            LootSystem.dropLoot(state);
        }
    }

    private static void handleMinibossDefeat(Player player, Scanner scanner, GameState state) {
        // Award revival potion
        state.revivalPotions++;
        TextEffect.typeWriter("🏆 [System] > Achievement Unlocked: Fractured Logo Defeated. +1 Revival Potion.", 50);

        // Narrative: Hallway encounter with Sir Khai
        if (state.revivalPotions > 0) {
            TextEffect.typeWriter(
                    "[System] > Scanning Area... Object Identified: Statue of Sir Khai (Mentor).", 50);
            System.out.print("Do you want to use a Revival Potion to awaken him? (yes/no): ");
            String choice = scanner.nextLine();

            // Create Sir Khai supporter object
            Supporter sirKhai = new Supporter("Sir Khai", "Mentor", "Guidance Heal");

            if (choice.equalsIgnoreCase("yes")) {
                state.revivalPotions--;
                sirKhai.setRevived(true);
                state.supporters.add(sirKhai);
                TextEffect.typeWriter("✨ [System] > Revival Successful. Sir Khai has joined your party.", 50);
                TextEffect.typeWriter(
                        "\"You’ve done well to come this far,\" he says. \"Allow me to guide you onward.\"", 50);
            } else {
                TextEffect.typeWriter(
                        "You clutch the potion tightly and walk past the statue, leaving Sir Khai in silence...", 50);
                // Force revival anyway
                state.revivalPotions--;
                sirKhai.setRevived(true);
                state.supporters.add(sirKhai);
                TextEffect.typeWriter(
                        "⚠️ [System] > Destiny override engaged... The potion activates automatically.", 50);
            }
        }
    }

    private static void enterSafeZone(GameState state, Runnable safeZoneAction) {
        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 50);
            return;
        }
        state.inSafeZone = true;
        safeZoneAction.run();
    }


    // -------------------------------------------------------------
    // ✅ HERE IS THE ONLY CHANGED PART: BOSS DIALOGUES (STORY ONLY)
    // -------------------------------------------------------------

    private static void addBossIntroDialogue(Enemy boss, int zone) {
        TextEffect.typeWriter("\n[System] > ⚠️ Warning: High Stress Levels Detected. The air grows heavy...", 70);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        switch (zone) {
            case 1: // CITU Logo (Fractured) - Unchanged
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: INSTITUTIONAL_IDENTITY.", 70);
                TextEffect.typeWriter("Before you looms a massive crystalline structure—", 70);
                TextEffect.typeWriter("the CITU logo, fractured and pulsating with unnatural light.", 70);
                TextEffect.typeWriter("Cracks spider across its surface, leaking pale radiance.", 70);

                try { Thread.sleep(600); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[Fractured Logo] \"Student... why do you resist?\"", 90);
                TextEffect.typeWriter("[System] > The voice echoes directly in your mind, cold and hollow.", 80);
                TextEffect.typeWriter("[Fractured Logo] \"Accept eternity. Accept stillness. Accept peace.\"", 90);

                try { Thread.sleep(600); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nYou grip your weapon tighter.", 70);
                TextEffect.typeWriter("Peace isn't worth this price.", 80);
                break;

            case 2: // The Thesis Defense Panel - Unchanged
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: ACADEMIC_PRESSURE.", 70);
                TextEffect.typeWriter("Three professors sit behind a desk fused into one horrific entity.", 70);
                TextEffect.typeWriter("Their bodies merged by failed experiments, but their critique remains razor-sharp.", 70);
                TextEffect.typeWriter("You recognize them. They graded your papers. They failed your projects.", 70);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[Panel - Dr. Cruz] \"Where. Are. Your. CITATIONS?!\"", 90);
                TextEffect.typeWriter("[Panel - Prof. Santos] \"This methodology is COMPLETELY UNACCEPTABLE!\"", 90);
                TextEffect.typeWriter("[Panel - Dean Reyes] \"Did you even ATTEND my lectures?! DID YOU?!\"", 90);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nRed pens float around them like weapons.", 70);
                TextEffect.typeWriter("[System] > Threat Analysis: Rejection imminent.", 80);

                try { Thread.sleep(600); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nYou grip your weapon.", 70);
                TextEffect.typeWriter("\"I'm done defending. Time for YOU to answer questions.\"", 90);
                break;

            case 3: // 🆕 UPDATED: Earthquake Monster (Yanig)
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: TRAUMA_EVENT_EARTHQUAKE.", 70);
                TextEffect.typeWriter("The ground beneath you ripples like water.", 70);
                TextEffect.typeWriter("Concrete slabs from the school floor rise up, forming a jagged, hulking beast.", 70);
                TextEffect.typeWriter("[System] > Loading Memory: The panic. The evacuation bell. The shaking walls.", 70);

                try { Thread.sleep(600); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[Yanig, the Earth-Shaker] \"NOWHERE... IS... STABLE!\"", 90);
                TextEffect.typeWriter("The monster roars, the sound of grinding rebar and collapsing pillars.", 80);
                TextEffect.typeWriter("\"FALL... LIKE... THE... REST!\"", 90);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nYou steady your footing despite the tremors.", 70);
                TextEffect.typeWriter("\"I stood my ground then. I'll stand it now.\"", 90);
                break;

            case 4: // 🆕 UPDATED: Storm Monster (Bagyong Tino)
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: WEATHER_ANOMALY_TYPHOON.", 70);
                TextEffect.typeWriter("The wind howls, tearing at your clothes. Rain lashes sideways like needles.", 70);
                TextEffect.typeWriter("A swirling vortex of dark clouds and debris forms a colossal eye staring at you.", 70);
                TextEffect.typeWriter("[System] > Alert: Signal No. 5 Detected. Evacuation impossible.", 70);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[Bagyong Tino] \"DROWN... IN... THE... FLOOD!\"", 100);
                TextEffect.typeWriter("Thunder rattles your bones. The pressure is suffocating.", 80);
                TextEffect.typeWriter("\"WASH... AWAY... THE... WEAK!\"", 100);

                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nYou wipe the rain from your eyes and raise your Thunder Spear.", 70);
                TextEffect.typeWriter("\"Storms always pass. And you're just passing through.\"", 90);
                break;

            default:
                TextEffect.typeWriter("A powerful enemy stands before you.", 70);
                break;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        TextEffect.typeWriter("\n⚔️ [System] > Combat Protocol Engaged!\n", 70);
    }

    private static void addBossVictoryDialogue(Enemy boss, int zone) {
        TextEffect.typeWriter("\n", 50);

        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        switch (zone) {
            case 1: // CITU Logo Destroyed - Unchanged
                TextEffect.typeWriter("💥 The crystalline logo cracks, then shatters.", 70);
                TextEffect.typeWriter("[System] > Institutional Barrier Removed.", 70);

                try { Thread.sleep(600); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nFor a moment, you hear whispers—", 80);
                TextEffect.typeWriter("the voices of your classmates, faint and distant.", 80);
                TextEffect.typeWriter("\"...thank you...\"", 100);
                break;

            case 2: // The Thesis Defense Panel Falls - Unchanged
                TextEffect.typeWriter("💼 The panel collapses, their endless questions finally silenced.", 70);
                TextEffect.typeWriter("Papers scatter across the lab floor—all marked with brutal red ink.", 80);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nFrom the rubble, a single paper drifts down.", 70);
                TextEffect.typeWriter("At the top, in clean black ink, it reads:", 80);
                TextEffect.typeWriter("'PASSED - With Highest Honors'", 100);

                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[System] > Academic Validation Received.", 70);
                TextEffect.typeWriter("\"Never. Again.\"", 100);
                break;

            case 3: // 🆕 UPDATED: Yanig Defeated
                TextEffect.typeWriter("🏚️ The jagged concrete beast crumbles into dust.", 70);
                TextEffect.typeWriter("The tremors stop instantly. The ground becomes solid again.", 70);
                TextEffect.typeWriter("[System] > Seismic Activity Stabilized. Foundation Secure.", 80);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nYou take a deep breath. The panic is gone.", 70);
                TextEffect.typeWriter("\"The shaking stopped.\"", 90);
                break;

            case 4: // 🆕 UPDATED: Bagyong Tino Defeated
                TextEffect.typeWriter("🌤️ The vortex dissipates. The rain stops as if turned off by a switch.", 70);
                TextEffect.typeWriter("[System] > Weather System Normalized. Sun Emergence Detected.", 80);
                TextEffect.typeWriter("Clouds part, revealing a calm, golden sky.", 80);

                try { Thread.sleep(1000); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\nAbove you, The Source blazes brighter than ever.", 70);
                TextEffect.typeWriter("You can feel its pull. Its hunger.", 80);

                try { Thread.sleep(800); } catch (InterruptedException e) {}

                TextEffect.typeWriter("\n[System] > Final Path Unlocked.", 90);
                TextEffect.typeWriter("Beyond this point, there is no return.", 100);
                break;

            default:
                TextEffect.typeWriter("Victory is yours.", 70);
                break;
        }

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}