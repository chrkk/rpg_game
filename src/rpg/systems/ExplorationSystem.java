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

    // --- Prompt ---

    private static void showPrompt(GameState state) {
        if (state.forwardSteps >= 5 && state.bossGateDiscovered) {
            TextEffect.typeWriter("Choose your action:\n1) Safe Zone\n2) Farm\n3) Boss", 40);
        } else {
            TextEffect.typeWriter("Do you move forward or backward? (forward/backward)", 40);
        }
    }

    // --- Backward / Safe zone ---
    private static void handleBackward(GameState state, Runnable safeZoneAction,
            ZoneConfig zone, Player player, Random rand) {

        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 50);
            return;
        }

        if (state.bossGateDiscovered) {
            if (rand.nextInt(100) < 10) {
                Enemy mob = zone.mobs[rand.nextInt(zone.mobs.length)];
                TextEffect.typeWriter("⚠️ An enemy blocks your retreat!", 60);
                CombatSystem combat = new CombatSystem(state);
                if (combat.startCombat(player, mob))
                    LootSystem.dropLoot(state);
            } else {
                TextEffect.typeWriter("🏃 You retreat swiftly back to the safe zone.", 60);
            }
            enterSafeZone(state, safeZoneAction);
            return;
        }

        // step-based retreat before gate discovery
        if (state.forwardSteps > 0) {
            state.forwardSteps--;
            TextEffect.typeWriter("🔙 You retrace your steps cautiously...", 50);
            if (state.forwardSteps == 0) {
                TextEffect.typeWriter("🏠 You have returned safely to the safe zone.", 60);
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

    // --- Forward / Exploration ---

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
                            TextEffect.typeWriter("⛔ The boss gate remains sealed.", 60);
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

    // --- Helper for boss fight outcome ---
private static void startBossFight(Player player, GameState state, ZoneConfig zone, Runnable safeZoneAction) {
    // 🆕 ADD THIS LINE: Show boss intro dialogue
    addBossIntroDialogue(zone.boss, state.zone);
    
    CombatSystem combat = new CombatSystem(state);
    boolean win = combat.startCombat(player, zone.boss);

    if (win) {
        // 🆕 ADD THIS LINE: Show victory dialogue
        addBossVictoryDialogue(zone.boss, state.zone);
        
        TextEffect.typeWriter("🏆 You defeated " + zone.boss.getName() + "! A new safe zone awaits...", 80);

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
                        "\n🏙️ You leave the City Ruins and climb toward the collapsed skyline...", 60);
                TextEffect.typeWriter("Broken sky bridges sway overhead, guiding you to the Fractured Sky.", 60);
                TextEffect.typeWriter("Somewhere above, the Observation Deck clings to the skyscraper's bones.", 60);
                break;
            case 4:
                TextEffect.typeWriter(
                        "\n🌠 You step off the Observation Deck and onto the final sky bridge...", 60);
                TextEffect.typeWriter("The Source blazes ahead — blinding white and void black entwined.", 60);
                TextEffect.typeWriter("Every step now is a vow: confrontation is inevitable.", 60);
                break;
            default:
                TextEffect.typeWriter("\n🚶 You set out from the safe zone, heading toward your next destination.", 60);
                break;
        }
    }

    // --- Statue encounter ---

    private static boolean checkStatueEncounter(GameState state, ZoneConfig zone, Random rand, java.util.Scanner scanner) {
        if (state.zone <= 1)
            return false; // only after Stage 1
        int chance = rand.nextInt(100);
        if (chance < 15) { // 15% chance per forward step (increased)
            Supporter statue = SupporterPool.getRandomUnrevivedSupporter(state.zone, rand, state);
            if (statue != null) {
                TextEffect.typeWriter("🗿 A mysterious statue appears in the " + zone.name + "...", 60);
                ReviveSystem.randomRevive(state, statue, scanner);
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

    private static void handleMinibossDefeat(Player player, Scanner scanner, GameState state) {
        // Award revival potion
        state.revivalPotions++;
        TextEffect.typeWriter("🏆 as you beat the fractured CITU logo! You obtained a Revival Potion.", 50);

        // Narrative: Hallway encounter with Sir Khai
        if (state.revivalPotions > 0) {
            TextEffect.typeWriter(
                    "You see a petrified statue of your teacher — Sir Khai, frozen mid-stance.", 50);
            System.out.print("Do you want to use a Revival Potion to awaken him? (yes/no): ");
            String choice = scanner.nextLine();

            // Create Sir Khai supporter object
            Supporter sirKhai = new Supporter("Sir Khai", "Mentor", "Guidance Heal");

            if (choice.equalsIgnoreCase("yes")) {
                state.revivalPotions--;
                sirKhai.setRevived(true);
                state.supporters.add(sirKhai);
                TextEffect.typeWriter("✨ The stone shell crumbles away... Sir Khai opens his eyes.", 50);
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
                        "⚠️ But destiny demands a guide... The potion glows and revives Sir Khai regardless.", 50);
            }
        }
    }

    // --- Safe Zone Helper ---
    private static void enterSafeZone(GameState state, Runnable safeZoneAction) {
        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 50);
            return;
        }
        state.inSafeZone = true;
        safeZoneAction.run();
    }

    private static void addBossIntroDialogue(Enemy boss, int zone) {
    TextEffect.typeWriter("\n⚠️ The air grows heavy...", 70);
    
    try {
        Thread.sleep(800);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    
    switch(zone) {
        case 1: // CITU Logo (Fractured)
            TextEffect.typeWriter("\n🏛️ Before you looms a massive crystalline structure—", 70);
            TextEffect.typeWriter("the CITU logo, fractured and pulsating with unnatural light.", 70);
            TextEffect.typeWriter("Cracks spider across its surface, leaking pale radiance.", 70);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[???] \"Student... why do you resist?\"", 90);
            TextEffect.typeWriter("The voice echoes directly in your mind, cold and hollow.", 80);
            TextEffect.typeWriter("\"Accept eternity. Accept stillness. Accept peace.\"", 90);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nYou grip your weapon tighter.", 70);
            TextEffect.typeWriter("Peace isn't worth this price.", 80);
            break;
            
        case 2: // The Thesis Defense Panel (Fused)
            TextEffect.typeWriter("\n📋 Three professors sit behind a desk fused into one horrific entity.", 70);
            TextEffect.typeWriter("Their bodies merged by failed experiments, but their critique remains razor-sharp.", 70);
            TextEffect.typeWriter("You recognize them. They graded your papers. They failed your projects.", 70);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[Panel - Dr. Cruz] \"Where. Are. Your. CITATIONS?!\"", 90);
            TextEffect.typeWriter("[Panel - Prof. Santos] \"This methodology is COMPLETELY UNACCEPTABLE!\"", 90);
            TextEffect.typeWriter("[Panel - Dean Reyes] \"Did you even ATTEND my lectures?! DID YOU?!\"", 90);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nRed pens float around them like weapons.", 70);
            TextEffect.typeWriter("Each one has failed a thousand students.", 80);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nYou grip your weapon.", 70);
            TextEffect.typeWriter("\"I'm done defending. Time for YOU to answer questions.\"", 90);
            break;
            
        case 3: // Screaming Billboard
            TextEffect.typeWriter("\n📺 A massive digital billboard flickers violently to life.", 70);
            TextEffect.typeWriter("Thousands of faces appear on screen—all screaming silently.", 70);
            TextEffect.typeWriter("Their mouths move in perfect, horrifying synchronization.", 70);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[Billboard - Static Burst] \"CONSUME. OBEY. FORGET.\"", 90);
            TextEffect.typeWriter("The words flash in blinding red letters.", 80);
            TextEffect.typeWriter("\"YOU WILL BE CONTENT. YOU WILL BE STILL.\"", 90);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nYou shield your eyes from the glare.", 70);
            TextEffect.typeWriter("\"The old world's dead. And I'm burying what's left.\"", 90);
            break;
            
        case 4: // Nimbus Tyrant
            TextEffect.typeWriter("\n⚡ Lightning splits the sky in jagged, violent arcs.", 70);
            TextEffect.typeWriter("Thunder shakes the broken skyscraper to its foundations.", 70);
            TextEffect.typeWriter("A colossal figure materializes from the storm clouds—", 70);
            TextEffect.typeWriter("part lightning, part shadow, all rage.", 70);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[Nimbus Tyrant] \"MORTAL. YOU DARE ASCEND?\"", 100);
            TextEffect.typeWriter("The voice is thunder itself, rattling your bones.", 80);
            TextEffect.typeWriter("\"I AM THE STORM. I AM THE SKY'S WRATH.\"", 100);
            TextEffect.typeWriter("\"THE SOURCE LIES BEYOND... BUT YOU WILL GO NO FURTHER.\"", 100);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nYou plant your feet on the trembling observation deck.", 70);
            TextEffect.typeWriter("Lightning dances along your Thunder Spear.", 80);
            TextEffect.typeWriter("\"I've come too far to stop now.\"", 90);
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
    
    TextEffect.typeWriter("\n⚔️ The battle begins!\n", 70);
}

private static void addBossVictoryDialogue(Enemy boss, int zone) {
    TextEffect.typeWriter("\n", 50);
    
    try {
        Thread.sleep(600);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    
    switch(zone) {
        case 1: // CITU Logo Destroyed
            TextEffect.typeWriter("💥 The crystalline logo cracks, then shatters.", 70);
            TextEffect.typeWriter("Fragments of light dissolve into nothing.", 70);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nFor a moment, you hear whispers—", 80);
            TextEffect.typeWriter("the voices of your classmates, faint and distant.", 80);
            TextEffect.typeWriter("\"...thank you...\"", 100);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nBut they fade. They're still stone.", 80);
            TextEffect.typeWriter("This was just the first step.", 90);
            break;
            
        case 2: // The Thesis Defense Panel Falls
            TextEffect.typeWriter("💼 The panel collapses, their endless questions finally silenced.", 70);
            TextEffect.typeWriter("Papers scatter across the lab floor—all marked with brutal red ink.", 80);
            TextEffect.typeWriter("Comments like 'REVISE', 'UNCLEAR', 'REDO' flutter away into dust.", 80);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nFrom the rubble, a single paper drifts down.", 70);
            TextEffect.typeWriter("At the top, in clean black ink, it reads:", 80);
            TextEffect.typeWriter("'PASSED - With Highest Honors'", 100);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nYou pick it up and whisper:", 70);
            TextEffect.typeWriter("\"Never. Again.\"", 100);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nFrom somewhere in the shadows, you hear Professor Ashiro chuckle softly.", 70);
            break;
            
        case 3: // Screaming Billboard Goes Dark
            TextEffect.typeWriter("📴 The billboard flickers violently, then goes dark.", 70);
            TextEffect.typeWriter("The screaming faces vanish, one by one.", 70);
            TextEffect.typeWriter("Silence falls over the City Ruins.", 80);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nFor the first time since arriving here...", 70);
            TextEffect.typeWriter("you hear the wind. Just the wind.", 90);
            
            try { Thread.sleep(600); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[Raze's voice echoes from somewhere] \"Well done. The path is open.\"", 80);
            break;
            
        case 4: // Nimbus Tyrant Defeated
            TextEffect.typeWriter("⚡ The Nimbus Tyrant roars as lightning consumes it from within.", 70);
            TextEffect.typeWriter("Thunder crashes one final time, then... silence.", 80);
            TextEffect.typeWriter("The storm clouds part, revealing clear sky beyond.", 80);
            
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\nAbove you, The Source blazes brighter than ever.", 70);
            TextEffect.typeWriter("You can feel its pull. Its hunger.", 80);
            
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            
            TextEffect.typeWriter("\n[Narrator] The final path is clear.", 90);
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
