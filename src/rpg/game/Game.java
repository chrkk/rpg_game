package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.systems.CombatSystem;
import rpg.items.Weapon;

public class Game {
    private Scanner scanner = new Scanner(System.in);

    private java.util.Random rand = new java.util.Random();


    private int zone = 1;          // start in zone 1
    private int forwardSteps = 0;  // track steps before boss
    private int crystals = 0;      // resource
    private int meat = 0;          // resource

    private Player player;

    public void launch() {
        boolean running = true;

        while (running) {
            TextEffect.typeWriter("----------------- DR. CAPSTONE -----------------", 20);
            TextEffect.typeWriter("1. START", 20);
            TextEffect.typeWriter("2. EXIT", 20);
            System.out.print("INPUT: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    playIntroStory();
                    createPlayer();   // 👈 moved here
                    wakeUpScene();    // 👈 then tutorial
                    break;
                case "2":
                    TextEffect.typeWriter("Thanks for playing!", 40);
                    running = false;
                    break;
                default:
                    TextEffect.typeWriter("Invalid choice. Please try again.", 40);
            }
        }
        scanner.close();
    }

    private void playIntroStory() {
        TextEffect.typeWriter("[Narrator] A sudden white light freezes humanity into stone...", 80);
        TextEffect.typeWriter("[POV] You look out the classroom window.", 80);
        TextEffect.typeWriter("The light grows brighter... until everything turns to stone.", 100);
        TextEffect.typeWriter("Darkness swallows your vision.", 120);

        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        TextEffect.typeWriter("...", 200);

        TextEffect.typeWriter("[POV] Crack. You awaken in a ruined classroom.", 80);
        TextEffect.typeWriter("Desks are broken, vines crawl through the windows, your classmates are petrified.", 80);
    }

    // 👇 New method for player creation
    private void createPlayer() {
        TextEffect.typeWriter("You focus... Who are you in this new world?", 60);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        TextEffect.typeWriter("Choose your class:", 40);
        TextEffect.typeWriter("1. Scientist (+Intelligence, +Defense)", 20);
        TextEffect.typeWriter("2. Fighter (+HP, +Defense)", 20);
        TextEffect.typeWriter("3. Archmage (+Intelligence, +Mana)", 20);
        System.out.print("Choice: ");
        String choice = scanner.nextLine();

        String trait;
        switch (choice) {
            case "1": trait = "Scientist"; break;
            case "2": trait = "Fighter"; break;
            case "3": trait = "Archmage"; break;
            default: trait = "Fighter";
        }

        player = new Player(name, trait);
        TextEffect.typeWriter("Identity set: " + player.getName() + " the " + player.getTrait(), 50);

        // Short story continuation
        TextEffect.typeWriter("[Narrator] You stand amidst silence. The world is broken, but you are alive.", 80);
        TextEffect.typeWriter("Your journey begins here...", 80);
    }

    private void wakeUpScene() {
        TextEffect.typeWriter("[Narrator] You feel weak. You need food and a weapon.", 80);
        TextEffect.typeWriter("Objective: Find food + find a weapon.", 80);

        boolean hasCrystal = false;
        boolean hasPencil = false;
        boolean hasWeapon = false;
        boolean awake = true;

        while (awake) {
            System.out.print("> What will you do? (craft / search / status / move): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "search":
                    if (!hasCrystal && !hasPencil) {
                        TextEffect.typeWriter("You search the ruined classroom...", 60);
                        TextEffect.typeWriter("You found: 1 Crystal Shard and a Pencil.", 60);
                        hasCrystal = true;
                        hasPencil = true;
                    } else {
                        TextEffect.typeWriter("You already searched here. Nothing else useful.", 60);
                    }
                    break;

                case "craft":
                    if (hasCrystal && hasPencil && player.getWeapon() == null) {
                        TextEffect.typeWriter("You combine the Pencil + Crystal Shard...", 60);
                        player.equipWeapon(new Weapon("Pencil Blade", 10));
                        TextEffect.typeWriter("You crafted a Pencil Blade! Your first weapon.", 60);
                        } else if (player.getWeapon() != null) {
                        TextEffect.typeWriter("You already crafted your weapon.", 60);
                        } else {
                        TextEffect.typeWriter("You don’t have enough materials to craft anything.", 60);
                        }
                    break;

                case "status":
                    TextEffect.typeWriter(
                        player.getName() + " the " + player.getTrait() +
                        " | HP: " + player.getHp() + "/" + player.getMaxHp() +
                        " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
                        " | Defense: " + player.getDefense() +
                        " | Intelligence: " + player.getIntelligence(),
                        40
                    );
                    break;

                case "move":
                    if (player.getWeapon() == null) {
                        TextEffect.typeWriter("You can’t leave yet. You need a weapon first.", 60);
                    } else {
                        TextEffect.typeWriter("Armed with your Pencil Blade, you step forward into the unknown...", 80);
                        awake = false; // exit tutorial loop
                        explorationLoop();
                    }
                    break;

                default:
                    TextEffect.typeWriter("Unknown command. Try craft / search / status / move.", 40);
            }
        }
    }


        private void explorationLoop() {
        boolean exploring = true;

        while (exploring) {
            System.out.print("> (craft / search / status / move): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "craft":
                    craftWeapon();
                    break;
                case "search":
                    TextEffect.typeWriter("You scavenge the area... but find nothing new outside combat.", 60);
                    break;
                case "status":
                    showStatus();
                    break;
                case "move":
                    handleMove();
                    break;
                default:
                    TextEffect.typeWriter("Unknown command.", 40);
            }
        }
    }

    private void handleMove() {
        TextEffect.typeWriter("Do you move forward or backward?", 40);
        System.out.print("> ");
        String dir = scanner.nextLine();

        if (dir.equalsIgnoreCase("backward")) {
            safeZone();
        } else if (dir.equalsIgnoreCase("forward")) {
            forwardSteps++;
            if (forwardSteps >= 5) {
                // Boss encounter
                Enemy boss = new Enemy("Stone Titan", 120, 15, 25);
                CombatSystem combat = new CombatSystem();
                boolean win = combat.startCombat(player, boss);
                if (win) {
                    TextEffect.typeWriter("🏆 You defeated the Boss! A new safe zone awaits...", 80);
                    zone++;
                    forwardSteps = 0; // reset for next chapter
                    safeZone();
                } else {
                    TextEffect.typeWriter("You awaken back at the safe zone...", 60);
                    safeZone();
                }
            } else {
                // RNG mob encounter
                int roll = rand.nextInt(100);
                if (roll < 70) {
                    Enemy mob = new Enemy("Wild Beast", 40, 8, 12);
                    CombatSystem combat = new CombatSystem();
                    if (combat.startCombat(player, mob)) {
                        int crystalDrop = 1 + rand.nextInt(2);
                        int meatDrop = rand.nextInt(2);
                        crystals += crystalDrop;
                        meat += meatDrop;
                        TextEffect.typeWriter("Loot: +" + crystalDrop + " Crystals, +" + meatDrop + " Meat.", 50);
                    }
                } else if (roll < 90) {
                    Enemy elite = new Enemy("Corrupted Guardian", 60, 12, 18);
                    CombatSystem combat = new CombatSystem();
                    if (combat.startCombat(player, elite)) {
                        int crystalDrop = 2 + rand.nextInt(3);
                        int meatDrop = 1;
                        crystals += crystalDrop;
                        meat += meatDrop;
                        TextEffect.typeWriter("Loot: +" + crystalDrop + " Crystals, +" + meatDrop + " Meat.", 50);
                    }
                } else {
                    TextEffect.typeWriter("The path is eerily quiet... nothing happens.", 60);
                }
            }
        } else {
            TextEffect.typeWriter("Invalid direction.", 40);
        }
    
    }

        private void safeZone() {
        // Auto recover after boss
        player.healFull();
        TextEffect.typeWriter("You feel renewed as you enter Safe Zone " + zone + ".", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);

        // Continue with the same 4 commands as always
        explorationLoop();
    }

    private void showStatus() {
        TextEffect.typeWriter(
            player.getName() + " the " + player.getTrait() +
            " | HP: " + player.getHp() + "/" + player.getMaxHp() +
            " | Mana: " + player.getMana() + "/" + player.getMaxMana() +
            " | Defense: " + player.getDefense() +
            " | Intelligence: " + player.getIntelligence() +
            " | Weapon: " + (player.getWeapon() == null ? "None" : player.getWeapon().toString()) +
            " | Crystals: " + crystals +
            " | Meat: " + meat,
            40
        );
    }
        private void craftWeapon() {
            if (player.getWeapon() != null && player.getWeapon().getName().equals("Pencil Blade") && crystals >= 3) {
            crystals -= 3;
            player.equipWeapon(new Weapon("Crystal Dagger", 20));
            TextEffect.typeWriter("You forged a Crystal Dagger! Stronger than your Pencil Blade.", 60);
            }       else if (player.getWeapon() != null && player.getWeapon().getName().equals("Crystal Dagger") && crystals >= 5) {
            crystals -= 5;
            player.equipWeapon(new Weapon("Crystal Sword", 35));
            TextEffect.typeWriter("You forged a Crystal Sword! Its edge gleams with power.", 60);
        }   else {
            TextEffect.typeWriter("You don’t have enough crystals or the right base weapon.", 60);
    }
    }




}
