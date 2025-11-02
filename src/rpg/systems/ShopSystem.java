package rpg.systems;

import java.util.Scanner;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.items.Blueprint;

public class ShopSystem {

    public static void openShop(GameState state, Scanner scanner) {
        TextEffect.typeWriter("🛒 Welcome to my shop! What would you like to buy?", 50);
        TextEffect.typeWriter("1. Crystal Sword Blueprint (10 Shards)", 40);
        TextEffect.typeWriter("0. Leave shop", 40);
        System.out.print("> ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                if (state.shards >= 10) {
                    state.shards -= 10;
                    Blueprint bp = new Blueprint("Crystal Sword Blueprint", "Crystal Sword");

                    // ✅ Mark recipe as discoverable, not directly craftable
                    state.unlockedRecipes.add(bp.getUnlocksRecipe());

                    TextEffect.typeWriter(
                            "You bought the " + bp.getName() +
                                    "! You can now discover the " + bp.getUnlocksRecipe()
                                    + " recipe through exploration or drops.",
                            60);
                } else {
                    TextEffect.typeWriter("You don’t have enough shards.", 60);
                }
                break;

            case "0":
                TextEffect.typeWriter("You leave the shop.", 40);
                break;

            default:
                TextEffect.typeWriter("Invalid choice.", 40);
        }
    }
}
