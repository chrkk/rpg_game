package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.systems.CraftingSystem;
import rpg.systems.ExplorationSystem;
import rpg.systems.SafeZoneSystem;
import rpg.systems.StatusSystem;
import rpg.items.Weapon;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private Random rand = new Random();
    private GameState state = new GameState();

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
                    createPlayer();
                    wakeUpScene();
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
    TextEffect.typeWriter("[Narrator] A sudden white light freezes humanity into stone...", 50);
    TextEffect.typeWriter("[POV] You glance out the classroom window.", 50);
    TextEffect.typeWriter("The light intensifies... until everything turns to stone.", 70);
    TextEffect.typeWriter("Darkness engulfs your vision.", 80);

    try { Thread.sleep(800); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    TextEffect.typeWriter("...", 120);

    TextEffect.typeWriter("[POV] Crack. You awaken in a ruined classroom.", 60);
    TextEffect.typeWriter("Desks lie broken, vines crawl through shattered windows, your classmates stand petrified.", 60);
    }


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

        TextEffect.typeWriter("[Narrator] You stand amidst silence. The world is broken, but you are alive.", 80);
        TextEffect.typeWriter("Your journey begins here...", 80);
    }

    private void wakeUpScene() {
        TextEffect.typeWriter("[Narrator] You feel weak. You need food and a weapon.", 80);
        TextEffect.typeWriter("Objective: Find food + find a weapon.", 80);

        boolean hasCrystal = false;
        boolean hasPencil = false;
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
                            state.crystals -= 1; // use up the crystal shard
                            player.equipWeapon(new Weapon("Pencil Blade", 10));
                            TextEffect.typeWriter("You combined a Pencil and a Crystal Shard into a Pencil Blade!", 60);
                        } else {
                            state.crystals = CraftingSystem.craftWeapon(player, state.crystals);
                            }
                    break;

                case "status":
                    StatusSystem.showStatus(player, state.crystals, state.meat);
                    break;

                case "move":
                    if (player.getWeapon() == null) {
                        TextEffect.typeWriter("You can’t leave yet. You need a weapon first.", 60);
                    } else {
                        TextEffect.typeWriter("Armed with your Pencil Blade, you step forward into the unknown...", 80);
                        awake = false;
                        ExplorationSystem.explorationLoop(player, scanner, rand, state);
                    }
                    break;

                default:
                    TextEffect.typeWriter("Unknown command. Try craft / search / status / move.", 40);
            }
        }
    }
}
