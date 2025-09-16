package rpg;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=========================");
            System.out.println("   TEXT RPG ADVENTURE");
            System.out.println("=========================");
            System.out.println("1. Start Game");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    startGame();
                    break;
                case "2":
                    System.out.println("Thanks for playing!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void startGame() {
        System.out.println("The adventure begins...");
        // Call your Game class here
    }
}
