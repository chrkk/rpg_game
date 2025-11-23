package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.items.Weapon;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state = new GameState();

    private Player player;
    private boolean devFastModeEnabled = false;
    private boolean devSkipIntro = false;
    private Integer devPlayerLevelOverride = null;
    private boolean devSkipTutorial = false;

    public void launch() {
        boolean running = true;

        try {
            while (running) {
                try {
                    TextEffect.typeWriter("----------------- DR. CAPSTONE -----------------", 20);
                    TextEffect.typeWriter("1. START", 20);
                    TextEffect.typeWriter("2. EXIT", 20);
                    System.out.print("INPUT: ");

                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            if (!devSkipIntro) {
                                playIntroStory();
                            } else {
                                System.out.println(">> Intro skipped via developer menu.");
                            }
                            createPlayer();
                            applyDeveloperOverridesAfterCreation();
                            beginAdventure();
                            break;
                        case "2":
                            TextEffect.typeWriter("Thanks for playing!", 40);
                            running = false;
                            break;
                        case "devskip":
                            System.out.println(">> Developer shortcut: skipping intro...");
                            devFastModeEnabled = true;
                            TextEffect.fastMode = true; // 🆕 enable fast mode globally
                            state.fastMode = true;
                            createPlayer();
                            applyDeveloperOverridesAfterCreation();
                            beginAdventure();
                            break;
                        case "devmenu":
                            openDeveloperMenu();
                            break;

                        default:
                            TextEffect.typeWriter("Invalid choice. Please try again.", 40);
                    }
                } catch (Exception e) {
                    // Hybrid handling: friendly + technical log
                    TextEffect.typeWriter("Something went wrong while processing your choice.", 40);
                    System.err.println("Main menu error -> " + e.getMessage());
                }
            }
        } catch (Exception e) {
            TextEffect.typeWriter("A critical error occurred. Exiting game.", 40);
            System.err.println("Game launch error -> " + e.getMessage());
        } finally {
            // Always close scanner at the end
            scanner.close();
        }
    }

    private void playIntroStory() {
        TextEffect.typeWriter("[Narrator] A sudden white light freezes humanity into stone...", 50);
        TextEffect.typeWriter("[POV] You glance out the classroom window.", 50);
        TextEffect.typeWriter("The light intensifies... until everything turns to stone.", 70);
        TextEffect.typeWriter("Darkness engulfs your vision.", 80);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // Hybrid handling
            TextEffect.typeWriter("Your vision flickers strangely...", 40);
            System.err.println("Intro sleep interrupted -> " + e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            TextEffect.typeWriter("...", 120);
        }

        // ✅ Updated to rooftop instead of classroom
        TextEffect.typeWriter("[POV] Crack. You awaken on the School Rooftop.", 60);
        TextEffect.typeWriter(
                "The city below is eerily silent. Statues of your classmates stand frozen in time, " +
                        "while vines creep across the broken walls around you.",
                60);
        TextEffect.typeWriter(
                "The rooftop feels like a fragile sanctuary amidst the chaos — your first Safe Zone.", 60);
    }

    private void createPlayer() {
        TextEffect.typeWriter("You focus... Who are you in this new world?", 60);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        TextEffect.typeWriter("Choose your class:", 40);

        // Scientist
        TextEffect.typeWriter("1. Scientist (+Intelligence, +Defense)", 20);
        TextEffect.typeWriter("   Skills:", 20);
        TextEffect.typeWriter("     - Chemical Strike: You hurl a vial of corrosive chemicals (Basic)", 15);
        TextEffect.typeWriter("     - Plasma Field: Unleash a field of charged plasma (Secondary)", 15);
        TextEffect.typeWriter("     - Nuclear Blast: A blinding explosion engulfs everything (Ultimate)", 15);

        // Fighter
        TextEffect.typeWriter("2. Fighter (+HP, +Defense)", 20);
        TextEffect.typeWriter("   Skills:", 20);
        TextEffect.typeWriter("     - Power Punch: Deliver a bone-crushing punch (Basic)", 15);
        TextEffect.typeWriter("     - War Cry: Boost morale with a ferocious cry (Secondary)", 15);
        TextEffect.typeWriter("     - Earth Breaker: Slam the ground with a devastating shockwave (Ultimate)", 15);

        // Archmage
        TextEffect.typeWriter("3. Archmage (+Intelligence, +Mana)", 20);
        TextEffect.typeWriter("   Skills:", 20);
        TextEffect.typeWriter("     - Fire Bolt: Conjure a blazing bolt of fire (Basic)", 15);
        TextEffect.typeWriter("     - Arcane Shield: Summon a barrier to reduce damage (Secondary)", 15);
        TextEffect.typeWriter("     - Meteor Storm: Call down a cataclysmic storm of meteors (Ultimate)", 15);

        System.out.print("Choice: ");
        String choice;
        String trait = null;

        while (trait == null) {
            choice = scanner.nextLine();

            try {
                int option = Integer.parseInt(choice);

                switch (option) {
                    case 1:
                        trait = "Scientist";
                        break;
                    case 2:
                        trait = "Fighter";
                        break;
                    case 3:
                        trait = "Archmage";
                        break;
                    default:
                        TextEffect.typeWriter("That number doesn’t match a class. Please enter 1, 2, or 3.", 40);
                        System.out.print("Choice: ");
                }
            } catch (NumberFormatException e) {
                TextEffect.typeWriter("Invalid input. Please enter a number (1, 2, or 3).", 40);
                System.err.println("Invalid class selection -> " + e.getMessage());
                System.out.print("Choice: ");
            } finally {
                // Always re-prompt if invalid
            }
        }

        player = new Player(name, trait);
        TextEffect.typeWriter("Identity set: " + player.getName() + " the " + player.getTrait(), 50);

        TextEffect.typeWriter("[Narrator] You stand amidst silence. The world is broken, but you are alive.", 80);
        TextEffect.typeWriter("Your journey begins here...", 80);
    }

    private void applyDeveloperOverridesAfterCreation() {
        if (devPlayerLevelOverride != null && player != null) {
            player.developerSetLevel(devPlayerLevelOverride);
            System.out.println(">> Developer override applied: Player level set to " + devPlayerLevelOverride + ".");
        }
    }

    private void openDeveloperMenu() {
        boolean menuOpen = true;

        while (menuOpen) {
            System.out.println("\n===== Developer Menu =====");
            System.out.println("1. Toggle Fast Mode: " + (devFastModeEnabled ? "ON ⚡" : "OFF 🐌"));
            System.out.println("2. Toggle Skip Intro: " + (devSkipIntro ? "ON ⏭️" : "OFF 📖"));
            System.out.println("3. Set Zone (Current: " + state.zone + ")");
            System.out.println("4. Give Items (Crystals, Shards, Potions)");
            String levelLabel = devPlayerLevelOverride != null
                    ? devPlayerLevelOverride.toString()
                    : (player != null ? String.valueOf(player.getLevel()) : "N/A");
            System.out.println("5. Set Player Level (Current: " + levelLabel + ")");
            System.out.println("6. Unlock All Blueprints");
            System.out.println("7. Toggle Skip Tutorial: " + (devSkipTutorial ? "ON 🎯" : "OFF 🎒"));
            System.out.println("0. Exit Dev Menu");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    devFastModeEnabled = !devFastModeEnabled;
                    TextEffect.fastMode = devFastModeEnabled;
                    state.fastMode = devFastModeEnabled;
                    System.out.println("Fast Mode is now " + (devFastModeEnabled ? "ON" : "OFF"));
                    break;
                case "2":
                    devSkipIntro = !devSkipIntro;
                    System.out.println("Skip Intro is now " + (devSkipIntro ? "ON" : "OFF"));
                    break;
                case "3":
                    configureZoneOverride();
                    break;
                case "4":
                    grantDeveloperItems();
                    break;
                case "5":
                    configurePlayerLevelOverride();
                    break;
                case "6":
                    unlockAllBlueprints();
                    break;
                case "7":
                    devSkipTutorial = !devSkipTutorial;
                    System.out.println("Skip Tutorial is now " + (devSkipTutorial ? "ON" : "OFF"));
                    break;
                case "0":
                    menuOpen = false;
                    break;
                default:
                    System.out.println("Invalid dev menu choice.");
            }
        }
    }

    private void configureZoneOverride() {
        System.out.print("Enter desired zone (1-4): ");
        String input = scanner.nextLine();
        try {
            int zone = Integer.parseInt(input);
            if (zone < 1 || zone > 4) {
                System.out.println("Zone must be between 1 and 4.");
                return;
            }
            state.zone = zone;
            state.forwardSteps = 0;
            state.bossGateDiscovered = false;
            state.currentZoneBoss = null;
            state.inSafeZone = true;
            state.zone2IntroShown = zone > 1;
            state.zone3IntroShown = zone > 2;
            state.zone4IntroShown = zone > 3;
            if (zone >= 2)
                state.shopUnlocked = true;
            System.out.println(">> Zone set to " + zone + ". Progress counters reset.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number for zone.");
        }
    }

    private void grantDeveloperItems() {
        state.crystals = Math.max(0, state.crystals + readDelta("Crystals", state.crystals));
        state.shards = Math.max(0, state.shards + readDelta("Shards", state.shards));
        state.revivalPotions = Math.max(0, state.revivalPotions + readDelta("Revival Potions", state.revivalPotions));
        System.out.println(">> Inventory updated: " + state.crystals + " crystals, " + state.shards + " shards, "
                + state.revivalPotions + " potions.");
    }

    private int readDelta(String label, int current) {
        System.out.print("Add how many " + label + "? (Current: " + current + "): ");
        String input = scanner.nextLine();
        if (input.trim().isEmpty())
            return 0;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. No changes applied to " + label + ".");
            return 0;
        }
    }

    private void configurePlayerLevelOverride() {
        System.out.print("Enter desired player level (1-99, 0 to clear): ");
        String input = scanner.nextLine();
        try {
            int level = Integer.parseInt(input);
            if (level == 0) {
                devPlayerLevelOverride = null;
                System.out.println("Player level override cleared.");
                return;
            }
            if (level < 1) {
                System.out.println("Level must be 1 or higher.");
                return;
            }
            devPlayerLevelOverride = level;
            if (player != null) {
                player.developerSetLevel(level);
            }
            System.out.println(">> Player level override set to " + level + ".");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number for level.");
        }
    }

    private void unlockAllBlueprints() {
        String[] recipes = { "Crystal Sword", "Flame Axe", "Thunder Spear" };
        for (String recipe : recipes) {
            state.unlockedRecipes.add(recipe);
            state.recipeItems.put(recipe, true);
        }
        state.shopUnlocked = true;
        System.out.println(">> All weapon blueprints unlocked and marked as discovered.");
    }

    private void beginAdventure() {
        if (devSkipTutorial) {
            System.out.println(">> Tutorial skipped via developer menu.");
            ensurePostTutorialState();
            new GameLoop(player, state, scanner, rand).start();
        } else {
            new Tutorial(player, state, scanner, rand).start();
        }
    }

    private void ensurePostTutorialState() {
        if (player.getWeapon() == null) {
            player.equipWeapon(new Weapon("Pencil Blade", 8, 12, 0.05, 1.5));
            state.stage1WeaponCrafted = true;
        }

        if (state.zone == 1 && state.forwardSteps < 1) {
            state.forwardSteps = 1;
        }

        if (state.shards < 1) {
            state.shards = 1;
        }
    }
}
