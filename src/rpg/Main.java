package rpg;

import java.util.Scanner;
import rpg.utils.TextEffect; 

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            TextEffect.typeWriter("=========================", 20);
            TextEffect.typeWriter("   TEXT RPG ADVENTURE", 50);
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

    private static void startGame() {
        TextEffect.typeWriter("The adventure begins...", 100);
        TextEffect.typeWriter("You wake up in a dark forest.", 100);
        TextEffect.typeWriter("A path leads north into the shadows.", 100);
    }
}
