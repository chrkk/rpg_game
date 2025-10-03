package rpg;

import rpg.game.Game;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //  Title Banner
        System.out.println("===================================");
        System.out.println("            DR. CAPSTONE           ");
        System.out.println("===================================");
        System.out.println("        v1.0  |  by The Formality");
        System.out.println("===================================");

        // Loading Effect
        System.out.print("Loading");
        for (int i = 0; i < 3; i++) {
            try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            System.out.print(".");
        }
        System.out.println("\n");

        // Prompt to begin
        System.out.println("Press ENTER to begin your journey...");
        new Scanner(System.in).nextLine();

        // Command-line argument support
        Game game = new Game();
        if (args.length > 0 && args[0].equalsIgnoreCase("skipIntro")) {
            // You can add a skipIntro() method in Game if you want
            System.out.println(">> Skipping intro story...");
            // game.skipIntro(); // optional
        }

        // Exception handling
        try {
            game.launch();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
