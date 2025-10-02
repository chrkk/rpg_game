package rpg.game;

import java.util.Scanner;
import rpg.utils.TextEffect;
import rpg.characters.Player;
import rpg.characters.Enemy;  
import rpg.systems.CombatSystem; 

public class Game {
    private Scanner scanner = new Scanner(System.in);

    public void launch() {
        boolean running = true;

        while (running) {
            TextEffect.typeWriter("=========================", 20);
            TextEffect.typeWriter("   DR. CAPSTONE", 50);
            TextEffect.typeWriter("=========================", 20);
            TextEffect.typeWriter("1. Start Game", 20);
            TextEffect.typeWriter("2. Exit", 20);
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    startGame();
                    break;
                case "2":
                    TextEffect.typeWriter("Thanks for playing!", 50);
                    running = false;
                    break;
                default:
                    TextEffect.typeWriter("Invalid choice. Please try again.", 50);
            }
        }
        scanner.close();
    }

    private void startGame() {
        // Character creation
        TextEffect.typeWriter("Enter your character's name: ", 30);
        String name = scanner.nextLine();

        TextEffect.typeWriter("Choose your class/trait:", 30);
        TextEffect.typeWriter("1. Thinker", 20);
        TextEffect.typeWriter("2. Survivor", 20);
        TextEffect.typeWriter("3. Explorer", 20);
        System.out.print("Your choice: ");
        String choice = scanner.nextLine();

        String trait;
        switch (choice) {
            case "1": trait = "Thinker"; break;
            case "2": trait = "Survivor"; break;
            case "3": trait = "Explorer"; break;
            default: trait = "Survivor"; // fallback
        }

        Player player = new Player(name, trait);

         // Ask if they want to skip intro
    TextEffect.typeWriter("Do you want to watch the intro story? (yes / skip)", 40);
    String skipChoice = scanner.nextLine();

    if (!skipChoice.equalsIgnoreCase("skip")) {
        playIntroStory(player); // full cinematic
    } else {
        TextEffect.typeWriter("Skipping intro... Awakening in the ruined classroom.", 40);
    }

    // Tutorial starts either way
    startTutorial(player);
    }

    private void playIntroStory(Player player) {
    // POV intro
    TextEffect.typeWriter("[POV " + player.getName() + "]", 40);
    TextEffect.typeWriter("It was just another boring day in class.", 80);
    TextEffect.typeWriter("The teacher kept yapping about Data Structures,", 80);
    TextEffect.typeWriter("my classmates were just playing Kour.io,", 80);
    TextEffect.typeWriter("and I was—like always—just staring out the window.", 80);

    TextEffect.typeWriter("That’s when I noticed it. A faint white glow on the horizon.", 100);
    TextEffect.typeWriter("MC (muttering):", 30);
    TextEffect.typeWriter("...\"What is that?\"", 50);

    // Narrator
    TextEffect.typeWriter("[Narrator]", 30);
    TextEffect.typeWriter("At first, it was small, almost harmless.", 90);
    TextEffect.typeWriter("But then it spread across the sky,", 90);
    TextEffect.typeWriter("swallowing the sunlight,", 90);
    TextEffect.typeWriter("brighter and brighter until the whole room turned pale.", 90);

    // Classmate
    TextEffect.typeWriter("Classmate (shouting):", 30);
    TextEffect.typeWriter("\"Hey! Do you guys see that?!\"", 40);

    // POV panic
    TextEffect.typeWriter("[POV]", 30);
    TextEffect.typeWriter("The chatter around me turned into panic.", 80);
    TextEffect.typeWriter("Some students stood up, some laughed nervously,", 80);
    TextEffect.typeWriter("others stared wide-eyed at the window.", 80);
    TextEffect.typeWriter("The light was everywhere now—it burned in my eyes.", 90);

    TextEffect.typeWriter("I blinked and turned—", 40);
    TextEffect.typeWriter("And froze.", 20);

    TextEffect.typeWriter("My classmates weren’t moving.", 80);
    TextEffect.typeWriter("Their skin turned gray, rough... stone.", 100);
    TextEffect.typeWriter("The teacher’s hand stopped mid-sentence,", 80);
    TextEffect.typeWriter("chalk frozen against the board.", 80);

    TextEffect.typeWriter("MC (voice shaking):", 30);
    TextEffect.typeWriter("\"N-no... what’s happening?!\"", 40);

    // Narrator silence
    TextEffect.typeWriter("[Narrator]", 30);
    TextEffect.typeWriter("One by one, every living being in the room became a statue.", 100);
    TextEffect.typeWriter("Laughter, fear, screams—all silenced.", 100);
    TextEffect.typeWriter("The world stopped.", 120);

    // POV petrification
    TextEffect.typeWriter("[POV]", 30);
    TextEffect.typeWriter("The light hit me next.", 80);
    TextEffect.typeWriter("My body stiffened, heavy as stone crept up my skin.", 100);
    TextEffect.typeWriter("I couldn’t move. Couldn’t breathe.", 100);
    TextEffect.typeWriter("Darkness swallowed everything.", 120);

    // Pause for dramatic effect
    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    TextEffect.typeWriter("...", 200);
    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

    TextEffect.typeWriter("I don’t know how long I was gone.", 100);

    // Awakening
    TextEffect.typeWriter("[Narrator]", 30);
    TextEffect.typeWriter("Until finally...", 120);

    TextEffect.typeWriter("[POV]", 30);
    TextEffect.typeWriter("Crack.", 20);
    TextEffect.typeWriter("I gasped, collapsing forward as the stone shell shattered around me.", 80);
    TextEffect.typeWriter("Dust filled my lungs.", 80);
    TextEffect.typeWriter("The classroom was ruined—desks broken, vines crawling through the windows, ceiling cracked.", 100);

    TextEffect.typeWriter("But everyone else... still stone. Their faces frozen in fear.", 100);

    TextEffect.typeWriter("MC (whispering):", 30);
    TextEffect.typeWriter("\"...Why me? Why am I the only one awake?\"", 50);

    // Closing line
    TextEffect.typeWriter("[Narrator]", 30);
    TextEffect.typeWriter("In a world stolen by stone, one soul has returned.", 120);
    TextEffect.typeWriter("And the journey begins.", 120);
}


    private void startTutorial(Player player) {
        TextEffect.typeWriter("Narrator: You feel weak. Your stomach growls. You need food and something to defend yourself.", 50);
        TextEffect.typeWriter("Tutorial Goal: Find food. Find a weapon.", 50);

        // Step 1: look
        TextEffect.typeWriter("Type 'look' to examine your surroundings.", 30);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("look")) {
            TextEffect.typeWriter("You see desks, vines, shattered glass.", 40);
        }

        // Step 2: search
        TextEffect.typeWriter("Now type 'search' to see what you can find.", 30);
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("search")) {
            TextEffect.typeWriter("You find a stick (weapon) and some edible plants (food).", 40);
        }

        // Step 3: status
        TextEffect.typeWriter("Tutorial Unlocked: You can now check your status.", 40);
        TextEffect.typeWriter(player.getName() + " the " + player.getTrait() + " | HP: 100 | Stamina: 50 | Hunger: 20", 40);

        // Step 4: move
        TextEffect.typeWriter("Do you want to move to the Hallway or Outside?", 40);
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("hallway")) {
            TextEffect.typeWriter("Thick vines block the doorway. You’ll need something sharper than a stick...", 80);
            TextEffect.typeWriter("Tutorial: Combine Stick + Glass Shard → Crude Knife (crafting unlocked).", 60);
        } else {
            TextEffect.typeWriter("A stray dog growls, blocking your path...", 80);
            TextEffect.typeWriter("Tutorial: Type 'attack' to fight with your stick, or 'run' to escape.", 60);

            Enemy dog = new Enemy("Stray Dog", 40, 10);
            rpg.systems.CombatSystem combat = new rpg.systems.CombatSystem();
            boolean survived = combat.startCombat(player, dog);

            if (!survived) {
            TextEffect.typeWriter("You collapse... but awaken again in the ruined classroom.", 80);
            return; // restart tutorial in future
    }
        }

        // End of tutorial → safe zone
        TextEffect.typeWriter("After struggle and survival, you stumble into an abandoned camp.", 80);
        TextEffect.typeWriter("For now, it is safe. You can rest here and prepare for what comes next.", 80);

        boolean inSafeZone = true;
        while (inSafeZone) {
        TextEffect.typeWriter("Safe Zone Options: recover / leave", 40);
         String choice = scanner.nextLine();
           if (choice.equalsIgnoreCase("recover")) {
             player.healFull();
              TextEffect.typeWriter("You rest and recover to full health. HP: " + player.getHp() + "/" + player.getMaxHp(), 40);
           } else if (choice.equalsIgnoreCase("leave")) {
               TextEffect.typeWriter("You leave the camp, ready for the journey ahead...", 60);
               inSafeZone = false;
           } else {
               TextEffect.typeWriter("Invalid choice.", 40);
           }
        }
    }
}
