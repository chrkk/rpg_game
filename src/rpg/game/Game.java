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
}
