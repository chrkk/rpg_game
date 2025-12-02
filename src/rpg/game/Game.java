package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Supporter;
import rpg.items.Weapon;
import rpg.ui_design.Intro;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state = new GameState();

    private Player player;
    private boolean devFastModeEnabled = false;
    private boolean devSkipIntro = false;
    private Integer devPlayerLevelOverride = null;
    private boolean devSkipTutorial = false;

    // ... [launch, playIntroStory, createPlayer methods match previous] ...
    public void launch() {
        boolean running = true;

        try {
            while (running) {
                try {
                    //new ui
                    Intro.displayMainMenu();
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
        // 1. Confusion Phase
        TextEffect.typeWriter("...", 120);
        TextEffect.typeWriter("Darkness. Infinite darkness.", 60);
        TextEffect.typeWriter("You try to move, but your body feels... absent.", 60);
        TextEffect.typeWriter("Where am I? What happened to the exam? The paper?", 60);

        // 2. System Initialization
        TextEffect.typeWriter("\n[System] > Consciousness Detected.", 30);
        TextEffect.typeWriter("[System] > Initializing 'Stress Response' Protocol...", 30);
        
        TextEffect.typeWriter("\n'Who said that?' you think. The voice isn't coming from your ears. It's inside your mind.", 60);

        TextEffect.typeWriter("\n[System] > Loading Asset: SCHOOL_ROOFTOP.obj", 30);
        TextEffect.typeWriter("[System] > Materializing Player Avatar...", 30);

        // 3. The Awakening
        TextEffect.typeWriter("\n[POV] GASP.", 20);
        TextEffect.typeWriter("Your eyes snap open. The blinding white light fades into gray.", 60);
        TextEffect.typeWriter("You are standing on the School Rooftop. But it's... wrong.", 60);
        TextEffect.typeWriter("Statues of students stand frozen around you. The sky is shattered like glass.", 60);

        // 4. Objective Given
        TextEffect.typeWriter("\n[System] > Welcome, Player. Objective: SURVIVE your anxieties.", 40);
        TextEffect.typeWriter("You don't understand what's going on, but you feel a strange urge to just... go with the flow.", 60);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Setting the scene
        TextEffect.typeWriter("\nThe city below is eerily silent. Statues of your classmates stand frozen in time.", 60);
        TextEffect.typeWriter("The rooftop feels like a fragile sanctuary amidst the chaos — your first Safe Zone.", 60);
    }

    private void createPlayer() {
        TextEffect.typeWriter("\n[System] > Identify yourself.", 60);
        System.out.print("> Enter your name: ");
        String name = scanner.nextLine();
        TextEffect.typeWriter("\n[System] > Ah, a brave soul enters this broken world...", 40);
        
        Intro.displayClassSelection(); 

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
        TextEffect.typeWriter("[System] > Identity confirmed: " + "[ " + player.getName() + " the " + player.getTrait() + " ]", 50);
        TextEffect.typeWriter("[System] > Beginning Simulation...", 60);
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
        System.out.print("Enter desired zone (1-5): ");
        String input = scanner.nextLine();
        try {
            int zone = Integer.parseInt(input);
            if (zone < 1 || zone > 5) {
                System.out.println("Zone must be between 1 and 5.");
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
            ensureWeaponProgressForZone(zone);
            ensureSirKhaiForZone(zone);
            System.out.println(">> Zone set to " + zone + ". Progress counters reset.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number for zone.");
        }
    }

    private void ensureWeaponProgressForZone(int zone) {
        if (zone >= 2) {
            state.stage1WeaponCrafted = true;
        }
        if (zone >= 3) {
            state.stage2WeaponCrafted = true;
        }
        if (zone >= 4) {
            state.stage3WeaponCrafted = true;
        }
        if (zone >= 5) {
            state.stage4WeaponCrafted = true;
        }
    }

    private void ensureSirKhaiForZone(int zone) {
        if (zone < 2) {
            return;
        }

        Supporter sirKhai = null;
        for (Supporter supporter : state.supporters) {
            if (supporter != null && supporter.getName() != null
                    && supporter.getName().equalsIgnoreCase("Sir Khai")) {
                sirKhai = supporter;
                break;
            }
        }

        if (sirKhai == null) {
            sirKhai = new Supporter("Sir Khai", "Guidance Heal");
            state.supporters.add(sirKhai);
        }

        sirKhai.setRevived(true);
        state.metSirKhai = true;

        boolean supporterEquipped = false;
        for (Supporter supporter : state.supporters) {
            if (supporter != null && supporter.isEquipped()) {
                supporterEquipped = true;
                break;
            }
        }

        if (!supporterEquipped) {
            sirKhai.setEquipped(true);
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
        // ✅ UPDATED: New weapon names
        String[] recipes = { "Logic Blade", "Aftershock Hammer", "Trident of Storms" };
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