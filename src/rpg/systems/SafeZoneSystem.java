package rpg.systems;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class SafeZoneSystem {

    public static void enterSafeZone(Player player, GameState state, Scanner scanner) {
        // ✅ Always heal
        player.healFull();
        state.inSafeZone = true;
        TextEffect.typeWriter("You feel renewed as you enter Safe Zone " + state.zone + ".", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);

        // ✅ Zone-specific interactions
        switch (state.zone) {
            case 1:
                zone1Interaction();
                break;
            case 2:
                zone2Interaction(state, scanner);
                break;
            default:
                genericInteraction(state, scanner);
        }
    }

    private static void zone1Interaction() {
        TextEffect.typeWriter("You rest on the School Rooftop. Your stats are restored.", 60);
    }

    private static void zone2Interaction(GameState state, Scanner scanner) {
        if (!state.zone2IntroShown) {
            // Step 1: Story NPC
            TextEffect.typeWriter("[Elder Scholar] Traveler, the guardian ahead cannot be harmed by ordinary steel.",
                    60);
            TextEffect.typeWriter("Seek the knowledge of forging — only then will you stand a chance.", 60);

            // Step 2: Shop NPC introduction
            TextEffect.typeWriter("\nA merchant approaches you.", 60);
            TextEffect.typeWriter("[Merchant] Greetings! I deal in blueprints and secrets of crafting.", 60);
            TextEffect.typeWriter("From now on, you can visit my shop in any Safe Zone.", 60);

            // ✅ Unlock shop globally
            state.shopUnlocked = true;
            state.zone2IntroShown = true; // mark as shown

            // Offer immediate shop access
            System.out.print("Do you want to browse the shop now? (yes/no): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                ShopSystem.openShop(state, scanner);
            }
        } else {
            // After intro has been shown, just generic safe zone with shop access
            genericInteraction(state, scanner);
        }
    }

    private static void genericInteraction(GameState state, Scanner scanner) {
        TextEffect.typeWriter("You rest safely here.", 60);
        // Shop option is now handled globally in GameLoop
    }

    public static void searchSafeZone(Player player, GameState state) {
        TextEffect.typeWriter("You search around but find nothing useful.", 60);
    }
}
