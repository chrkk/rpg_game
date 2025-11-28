package rpg;

import rpg.game.Game;
import java.util.Scanner;
import rpg.ui_design.TitleBanner;

public class Main {
    public static void main(String[] args) {
        // Title Banner (ASCII Art)
        // System.out.println(" ______   ______            _______  _______  _______  _______  _______  _______  __    _  _______ ");
        // System.out.println("|      | |    _ |          |       ||   _   ||       ||       ||       ||       ||  |  | ||       |");
        // System.out.println("|  _    ||   | ||          |       ||  |_|  ||    _  ||  _____||_     _||   _   ||   |_| ||    ___|");
        // System.out.println("| | |   ||   |_||_         |       ||       ||   |_| || |_____   |   |  |  | |  ||       ||   |___ ");
        // System.out.println("| |_|   ||    __  | ___    |      _||       ||    ___||_____  |  |   |  |  |_|  ||  _    ||    ___|");
        // System.out.println("|       ||   |  | ||   |   |     |_ |   _   ||   |     _____| |  |   |  |       || | |   ||   |___ ");
        // System.out.println("|______| |___|  |_||___|   |_______||__| |__||___|    |_______|  |___|  |_______||_|  |__||_______|");
        // System.out.println("                                     v1.0  |  by The Formality");
        // System.out.println();

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
