package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.utils.TextEffect;
import rpg.characters.Player;

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
                    new Tutorial(player, state, scanner, rand).start();
                    break;
                case "2":
                    TextEffect.typeWriter("Thanks for playing!", 40);
                    running = false;
                    break;
                case "devskip": // developer command (hidden)
                    System.out.println(">> Developer shortcut: skipping intro...");
                    createPlayer();
                    new Tutorial(player, state, scanner, rand).start();
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

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        TextEffect.typeWriter("...", 120);

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
        TextEffect.typeWriter("1. Scientist (+Intelligence, +Defense)", 20);
        TextEffect.typeWriter("2. Fighter (+HP, +Defense)", 20);
        TextEffect.typeWriter("3. Archmage (+Intelligence, +Mana)", 20);
        System.out.print("Choice: ");
        String choice = scanner.nextLine();

        String trait;
        switch (choice) {
            case "1":
                trait = "Scientist";
                break;
            case "2":
                trait = "Fighter";
                break;
            case "3":
                trait = "Archmage";
                break;
            default:
                trait = "Fighter";
        }

        player = new Player(name, trait);
        TextEffect.typeWriter("Identity set: " + player.getName() + " the " + player.getTrait(), 50);

        TextEffect.typeWriter("[Narrator] You stand amidst silence. The world is broken, but you are alive.", 80);
        TextEffect.typeWriter("Your journey begins here...", 80);
    }

}
