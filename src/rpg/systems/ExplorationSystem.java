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

            if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
                switch (dir) {
                    case "1":
                        handleBackward(state, safeZoneAction, zone, player, rand);
                        break;
                    case "2":
                        handleForward(player, scanner, rand, state, zone);
                        break;
                    case "3":
                        handleBoss(player, dir, state, zone, safeZoneAction, rand);
                        break;
                    default:
                        TextEffect.typeWriter("Invalid choice. Please enter 1, 2, or 3.", 40);
                }
            } else {
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

    private static void showPrompt(GameState state) {
        if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
            TextEffect.typeWriter("Choose your action:\n1) Safe Zone\n2) Farm\n3) Boss", 40);
        } else {
            TextEffect.typeWriter("Do you move forward or backward? (forward/backward)", 40);
        }
    }

    private static void handleBackward(GameState state, Runnable safeZoneAction, ZoneConfig zone, Player player, Random rand) {
        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 50);
            return;
        }

        if (state.bossGateDiscovered) {
            if (rand.nextInt(100) < 10) {
                Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                TextEffect.typeWriter("⚠️ [System] > Alert: An enemy blocks your retreat!", 60);
                CombatSystem combat = new CombatSystem(state);
                if (combat.startCombat(player, mob)) {
                    LootSystem.dropLoot(state);
                }
            } else {
                TextEffect.typeWriter("🏃 [System] > Retreat successful. Returning to Safe Zone.", 60);
            }
            enterSafeZone(state, safeZoneAction);
            return;
        }

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
                if (combat.startCombat(player, mob)) {
                    LootSystem.dropLoot(state);
                }
            } else {
                TextEffect.typeWriter("The path is eerily quiet...", 50);
            }
        } else {
            enterSafeZone(state, safeZoneAction);
        }
    }

    private static void handleForward(Player player, Scanner scanner, Random rand, GameState state, ZoneConfig zone) {
        state.inSafeZone = false;
        
        if (state.bossGateDiscovered) {
            if (checkStatueEncounter(state, zone, rand, scanner)) {
                return;
            }
            spawnMob(state, zone, player, rand);
            return;
        }

        state.forwardSteps++;
        narrateZoneExit(state);

        if (checkStatueEncounter(state, zone, rand, scanner)) {
            return;
        }

        try {
            if (state.forwardSteps >= 5 && !state.bossGateDiscovered) {
                if (!BossGateSystem.canFightBoss(state, player, () -> {})) {
                    TextEffect.typeWriter("⛔ [System] > Access Denied. The boss gate remains sealed.", 60);
                } else {
                    TextEffect.typeWriter("🔥 [System] > Boss Gate Reached. Options: farm, retreat, or challenge.", 60);
                }
                state.bossGateDiscovered = true;
            }
            spawnMob(state, zone, player, rand);
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during exploration.", 40);
        }
    }

    private static void handleBoss(Player player, String choice, GameState state, ZoneConfig zone, Runnable safeZoneAction, Random rand) {
        if (state.forwardSteps < 5) {
            TextEffect.typeWriter("🚪 You haven’t reached the boss gate yet.", 60);
            return;
        }

        if (choice.equals("1") && !state.bossGateDiscovered) {
            startBossFight(player, state, zone, safeZoneAction);
        } else if (choice.equals("3") && state.bossGateDiscovered) {
            if (BossGateSystem.canFightBoss(state, player, safeZoneAction)) {
                startBossFight(player, state, zone, safeZoneAction);
            } else {
                TextEffect.typeWriter("⛔ [System] > Access Denied.", 60);
            }
        } else if (choice.equals("2")) {
            state.bossGateDiscovered = true;
            spawnMob(state, zone, player, rand);
        } else if (choice.equals("1")) {
            enterSafeZone(state, safeZoneAction);
        }
    }

    private static void startBossFight(Player player, GameState state, ZoneConfig zone, Runnable safeZoneAction) {
        addBossIntroDialogue(zone.boss, state.zone);

        CombatSystem combat = new CombatSystem(state);
        boolean win = combat.startCombat(player, zone.boss);

        if (win) {
            addBossVictoryDialogue(zone.boss, state.zone);

            // ✅ IF FINAL BOSS (Zone 5 / Default), TRIGGER ENDING
            if (state.zone >= 5) {
                triggerEndingSequence();
                System.exit(0);
            }

            TextEffect.typeWriter("🏆 [System] > Target Eliminated: " + zone.boss.getName() + "! Zone Clear.", 80);
            
            if (state.zone == 1) {
                handleMinibossDefeat(player, new Scanner(System.in), state);
            }

            state.zone++;
            state.forwardSteps = 0;
            state.bossGateDiscovered = false;
            state.inSafeZone = true;
            safeZoneAction.run();
        } else {
            TextEffect.typeWriter("[System] > Critical Failure. Respawning at Safe Zone...", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
        }
    }

    private static void narrateZoneExit(GameState state) {
        if (state.forwardSteps != 1) {
            return;
        }
        
        switch (state.zone) {
            case 1:
                TextEffect.typeWriter("\n🌌 [System] > Leaving School Rooftop...", 60);
                break;
            case 2:
                TextEffect.typeWriter("\n⚙️ [System] > Exiting Ruined Lab...", 60);
                break;
            case 3:
                TextEffect.typeWriter("\n🏙️ [System] > Leaving City Ruins...", 60);
                break;
            case 4:
                TextEffect.typeWriter("\n🌠 [System] > Approaching The Source...", 60);
                break;
            case 5:
                TextEffect.typeWriter("\n💻 [System] > Entering Core Logic...", 60);
                break;
            default:
                TextEffect.typeWriter("\n🚶 [System] > Moving to next sector.", 60);
                break;
        }
    }

    private static boolean checkStatueEncounter(GameState state, ZoneConfig zone, Random rand, java.util.Scanner scanner) {
        if (state.zone <= 1 || rand.nextInt(100) >= 15) {
            return false;
        }
        
        Supporter statue = SupporterPool.getRandomUnrevivedSupporter(state.zone, rand, state);
        if (statue != null) {
            TextEffect.typeWriter("🗿 [System] > Anomaly Detected: A statue appears...", 60);
            ReviveSystem.randomRevive(state, statue, scanner);
            return true;
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
        state.revivalPotions++;
        TextEffect.typeWriter("🏆 [System] > Achievement: Fractured Logo Defeated. +1 Potion.", 50);
        
        if (state.revivalPotions > 0) {
            TextEffect.typeWriter("[System] > Object Identified: Statue of Sir Khai (Mentor).", 50);
            System.out.print("Revive him? (yes/no): ");
            String choice = scanner.nextLine();

            Supporter sirKhai = null;
            for (Supporter supporter : state.supporters) {
                if (supporter != null && supporter.getName() != null && supporter.getName().equalsIgnoreCase("Sir Khai")) {
                    sirKhai = supporter;
                    break;
                }
            }
            if (sirKhai == null) {
                sirKhai = new Supporter("Sir Khai", "Mentor", "Guidance Heal");
                state.supporters.add(sirKhai);
            }

            sirKhai.setRevived(true);
            state.metSirKhai = true;

            if (choice.equalsIgnoreCase("yes")) {
                state.revivalPotions--;
                TextEffect.typeWriter("✨ [System] > Sir Khai joined the party.", 50);
            } else {
                state.revivalPotions--;
                TextEffect.typeWriter("⚠️ [System] > Destiny override... Sir Khai revived automatically.", 50);
            }
        }
    }

    private static void enterSafeZone(GameState state, Runnable safeZoneAction) {
        if (state.inSafeZone) {
            TextEffect.typeWriter("Already in safe zone.", 50);
            return;
        }
        state.inSafeZone = true;
        safeZoneAction.run();
    }

    // -------------------------------------------------------------
    // ✅ BOSS INTRO: The Choice (CS vs IT)
    // -------------------------------------------------------------
    private static void addBossIntroDialogue(Enemy boss, int zone) {
        TextEffect.typeWriter("\n[System] > ⚠️ Warning: High Stress Levels Detected. The air grows heavy...", 70);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // handle interruption
        }

        switch (zone) {
            case 1:
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: INSTITUTIONAL_IDENTITY.", 70);
                TextEffect.typeWriter("[Fractured Logo] \"Accept eternity. Accept stillness.\"", 90);
                break;
            case 2:
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: ACADEMIC_PRESSURE.", 70);
                TextEffect.typeWriter("[Panel] \"Where. Are. Your. CITATIONS?!\"", 90);
                break;
            case 3:
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: TRAUMA_EVENT_EARTHQUAKE.", 70);
                TextEffect.typeWriter("[Yanig] \"NOWHERE... IS... STABLE!\"", 90);
                break;
            case 4:
                TextEffect.typeWriter("\n[System] > Visualizing Obstacle: WEATHER_ANOMALY_TYPHOON.", 70);
                TextEffect.typeWriter("[Bagyong Tino] \"DROWN... IN... THE... FLOOD!\"", 90);
                break;

            // ✅ THE FINAL BOSS: THE CHOICE
            default:
                TextEffect.typeWriter("\n[System] > CRITICAL ALERT: CAREER_PATH_DECISION REQUIRED.", 70);
                TextEffect.typeWriter("You stand before The Source. It splits into two blinding paths.", 70);
                TextEffect.typeWriter("A shadow stands at the fork. It has no face, only a mirror reflecting your doubt.", 70);

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    // handle interruption
                }

                TextEffect.typeWriter("\n[The Choice] \"To the left: The Code. The Logic. The CS Path you started.\"", 90);
                TextEffect.typeWriter("[The Choice] \"To the right: The Systems. The Networks. The IT Path you crave.\"", 90);
                TextEffect.typeWriter("[The Choice] \"Choose wrong, and you waste everything. Are you quitting? Or evolving?\"", 90);

                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    // handle interruption
                }

                TextEffect.typeWriter("\n[System] > Panic Levels Rising. Decision Paralysis Imminent.", 70);
                TextEffect.typeWriter("You ready your weapon.", 70);
                TextEffect.typeWriter("\"It's not about the path. It's about who walks it.\"", 90);
                break;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // handle interruption
        }
        TextEffect.typeWriter("\n⚔️ [System] > Combat Protocol Engaged!\n", 70);
    }

    // -------------------------------------------------------------
    // ✅ BOSS VICTORY
    // -------------------------------------------------------------
    private static void addBossVictoryDialogue(Enemy boss, int zone) {
        TextEffect.typeWriter("\n", 50);

        switch (zone) {
            case 1:
                TextEffect.typeWriter("[System] > Institutional Barrier Removed.", 70);
                break;
            case 2:
                TextEffect.typeWriter("[System] > Academic Validation Received.", 70);
                break;
            case 3:
                TextEffect.typeWriter("[System] > Seismic Activity Stabilized.", 70);
                break;
            case 4:
                TextEffect.typeWriter("[System] > Weather System Normalized.", 70);
                break;

            // ✅ THE FINAL VICTORY
            default:
                TextEffect.typeWriter("💥 The mirror shatters. The two paths merge into one horizon.", 70);
                TextEffect.typeWriter("The doubt dissolves. You step forward, no longer afraid of the switch.", 70);
                TextEffect.typeWriter("[System] > DECISION ACCEPTED. SIMULATION COMPLETE.", 80);
                break;
        }
    }

    // -------------------------------------------------------------
    // ✅ THE TWIST ENDING SEQUENCE
    // -------------------------------------------------------------
    private static void triggerEndingSequence() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // handle interruption
        }

        // Waking up
        System.out.println("\n\n\n");
        TextEffect.typeWriter("...Hey.", 60);
        TextEffect.typeWriter("...Hey! Wake up!", 60);

        TextEffect.typeWriter("\nYour eyes snap open. You jolt up from your desk.", 30);
        TextEffect.typeWriter("Fluorescent lights buzz overhead. You're in the library.", 50);
        TextEffect.typeWriter("Your laptop screen is on. A shifting form document is open.", 50);

        TextEffect.typeWriter("\n[Friend] \"Dude, you passed out while looking at the shifting forms. You okay?\"", 50);
        TextEffect.typeWriter("You look at the screen. The cursor blinks on the 'Course' field.", 50);

        TextEffect.typeWriter("\nYou rub your eyes. \"Yeah... yeah. I think I know what to write now.\"", 50);
        TextEffect.typeWriter("[Friend] \"CS or IT?\"", 50);
        TextEffect.typeWriter("You smile. \"I'm just gonna be me.\"", 50);

        TextEffect.typeWriter("\nYou stand up to leave, grabbing your bag.", 50);
        TextEffect.typeWriter("But then you feel something heavy in your pocket.", 50);

        // The Cliffhanger Twist
        TextEffect.typeWriter("\nYou reach inside and pull it out.", 60);
        TextEffect.typeWriter("It pulses with a faint, warm light.", 60);

        TextEffect.typeWriter("\nIt's a glowing blue Crystal Shard.", 100);

        TextEffect.typeWriter("\nAnd then, text appears... floating in the air right in front of your face:", 60);
        TextEffect.typeWriter("\n[System] > New Game Plus Available. Do you wish to continue?", 80);

        TextEffect.typeWriter("\nTHANK YOU FOR PLAYING DR. CAPSTONE.", 100);
    }
}