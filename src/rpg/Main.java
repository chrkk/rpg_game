package rpg;

import rpg.game.Game;
import java.util.Scanner;
import rpg.ui_design.TitleBanner;

public class Main {
    public static void main(String[] args) {
        //new title banner
        TitleBanner.displayTitleBanner();
        // Loading Effect
        System.out.print("Loading");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restore interrupt flag
                System.err.println("Loading interrupted -> " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error during loading -> " + e.getMessage());
            } finally {
                System.out.print(".");
            }
        }
        System.out.println("\n");

        // Prompt to begin
        System.out.println("Press ENTER to begin your journey...");
        Scanner scanner = new Scanner(System.in);
        try {
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Input error at start -> " + e.getMessage());
        }

        Game game = new Game();

        if (args.length > 0 && args[0].equalsIgnoreCase("skipIntro")) {
            System.out.println(">> Skipping intro story...");
            // game.skipIntro(); // optional
        }

        try {
            game.launch();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in the game -> " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always runs, even if an exception is thrown
            System.out.println(">> Closing resources... Thanks for playing DR. CAPSTONE!");
            try {
                scanner.close();
            } catch (Exception e) {
                System.err.println("Error closing scanner -> " + e.getMessage());
            }
        }
    }
}
