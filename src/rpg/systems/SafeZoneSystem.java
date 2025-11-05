package rpg.systems;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class SafeZoneSystem {

    public static void enterSafeZone(Player player, GameState state, Scanner scanner) {
        try {
            // ✅ Always heal
            player.healFull();
            state.inSafeZone = true;

            // Zone-specific entrance text
            if (state.zone == 1) {
                TextEffect.typeWriter("You feel renewed as you enter the School Rooftop.", 60);
            } else if (state.zone == 2) {
                TextEffect.typeWriter("You step cautiously into the Ruined Lab. The air smells of rust and chemicals.", 60);
            } else {
                TextEffect.typeWriter("You feel renewed as you enter Safe Zone " + state.zone + ".", 60);
            }

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
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong while entering the safe zone.", 40);
            System.err.println("SafeZoneSystem error -> " + e.getMessage());
        } finally {
            // Always runs after entering safe zone
        }
    }

    private static void zone1Interaction() {
        try {
            TextEffect.typeWriter("You rest on the School Rooftop. The wind is calm, and your stats are restored.", 60);
        } catch (Exception e) {
            TextEffect.typeWriter("Something feels off while resting here.", 40);
            System.err.println("Zone1 interaction error -> " + e.getMessage());
        }
    }

    private static void zone2Interaction(GameState state, Scanner scanner) {
        try {
            if (!state.zone2IntroShown) {
                // Step 1: Story NPC (Professor Ashiro)
                TextEffect.typeWriter("[Professor Ashiro] These halls once birthed discovery… now they breed only danger.", 60);
                TextEffect.typeWriter("The guardian ahead is no mere beast — it is science gone astray. Only forged weapons can pierce its hide.", 60);

                // Step 2: Shop NPC introduction (Scrapwright Kuro)
                TextEffect.typeWriter("\nA scavenger emerges from the shadows of broken machinery.", 60);
                TextEffect.typeWriter("[Scrapwright Kuro] Heh, I deal in blueprints and scraps of the old world.", 60);
                TextEffect.typeWriter("From now on, you can visit my shop in any Safe Zone.", 60);

                // ✅ Unlock shop globally
                state.shopUnlocked = true;
                state.zone2IntroShown = true; // mark as shown

                // Offer immediate shop access
                System.out.print("Do you want to browse the shop now? (yes/no): ");
                try {
                    String choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("yes")) {
                        ShopSystem.openShop(state, scanner);
                    }
                } catch (Exception e) {
                    TextEffect.typeWriter("You hesitate... the scavenger shrugs and turns away for now.", 40);
                    System.err.println("Shop prompt error -> " + e.getMessage());
                }
            } else {
                // After intro has been shown, just generic safe zone with shop access
                genericInteraction(state, scanner);
            }
        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong during the Ruined Lab interaction.", 40);
            System.err.println("Zone2 interaction error -> " + e.getMessage());
        }
    }

    private static void genericInteraction(GameState state, Scanner scanner) {
        try {
            TextEffect.typeWriter("You rest safely here, the chaos outside feels distant for a moment.", 60);
            // Shop option is now handled globally in GameLoop
        } catch (Exception e) {
            TextEffect.typeWriter("You try to rest, but something feels wrong.", 40);
            System.err.println("Generic interaction error -> " + e.getMessage());
        }
    }

    public static void searchSafeZone(Player player, GameState state) {
        try {
            TextEffect.typeWriter("You search around but find nothing useful among the debris.", 60);
        } catch (Exception e) {
            TextEffect.typeWriter("You stumble while searching the safe zone.", 40);
            System.err.println("Safe zone search error -> " + e.getMessage());
        }
    }
}
