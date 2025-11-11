package rpg.systems;

import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.characters.Supporter;

import java.util.Scanner;

public class ReviveSystem {

    // Scripted revival (e.g., Sir Khai after Stage 1 miniboss)
    public static void scriptedRevive(GameState state, Supporter sirKhai, Scanner scanner) {
        while (!state.metSirKhai) {
            try {
                TextEffect.typeWriter("\n🗿 A statue stands before you... Sir Khai, frozen in stone.", 60);
                TextEffect.typeWriter("A glowing aura surrounds him. Do you wish to revive? (yes/no)", 60);

                System.out.print("> ");
                String choice = scanner.nextLine().trim().toLowerCase();

                switch (choice) {
                    case "yes":
                        if (state.revivalPotions > 0) {
                            state.revivalPotions--;
                            sirKhai.setRevived(true);
                            state.supporters.add(sirKhai);
                            state.metSirKhai = true;
                            TextEffect.typeWriter("✨ Sir Khai awakens, joining you as your first supporter!", 60);
                        } else {
                            TextEffect.typeWriter("❌ You lack a Revival Potion. The statue remains silent.", 60);
                        }
                        break;

                    case "no":
                        TextEffect.typeWriter("❌ You cannot proceed without reviving a supporter.", 60);
                        break;

                    default:
                        TextEffect.typeWriter("⚠️ Invalid input. Please type 'yes' or 'no'.", 60);
                        break;
                }
            } catch (Exception e) {
                TextEffect.typeWriter("An error occurred while processing your choice.", 60);
                System.err.println("ReviveSystem error -> " + e.getMessage());
            }
        }
    }

    // Random statue revival outside safe zones
    public static void randomRevive(GameState state, Supporter supporter) {
        try {
            if (state.revivalPotions > 0) {
                state.revivalPotions--;
                supporter.setRevived(true);
                TextEffect.typeWriter("✨ You used a Revival Potion to awaken " + supporter.getName() + "!", 60);
                state.supporters.add(supporter); // track revived allies
            } else {
                TextEffect.typeWriter("🗿 The statue remains silent... you lack a Revival Potion.", 60);
            }
        } catch (Exception e) {
            TextEffect.typeWriter("An error occurred during statue revival.", 60);
            System.err.println("RandomRevive error -> " + e.getMessage());
        }
    }
}
