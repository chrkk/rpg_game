package rpg.systems;

import java.util.*;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.items.Blueprint;

public class ShopSystem {

    // ✅ Shop inventory (easy to expand later)
    private static final List<Blueprint> inventory = Arrays.asList(
        new Blueprint("Crystal Sword Blueprint", "Crystal Sword", 10),
        new Blueprint("Flame Axe Blueprint", "Flame Axe", 15),
        new Blueprint("Shadow Bow Blueprint", "Shadow Bow", 20)
    );

    public static void openShop(GameState state, Scanner scanner) {
        TextEffect.typeWriter("🛒 Welcome to my shop! What would you like to buy?", 50);

        // Show inventory
        for (int i = 0; i < inventory.size(); i++) {
            Blueprint bp = inventory.get(i);
            TextEffect.typeWriter((i + 1) + ". " + bp.getName() + " (" + bp.getPrice() + " Shards)", 40);
        }
        TextEffect.typeWriter("0. Leave shop", 40);

        System.out.print("> ");
        String choice = scanner.nextLine();

        try {
            int option = Integer.parseInt(choice);
            if (option == 0) {
                TextEffect.typeWriter("You leave the shop.", 40);
                return;
            }

            if (option > 0 && option <= inventory.size()) {
                Blueprint selected = inventory.get(option - 1);

                if (state.shards >= selected.getPrice()) {
                    state.shards -= selected.getPrice();

                    // ✅ Unlock blueprint
                    state.unlockedRecipes.add(selected.getUnlocksRecipe());

                    TextEffect.typeWriter("You bought the " + selected.getName() +
                        "! You can now discover the " + selected.getUnlocksRecipe() + " recipe in the world.", 60);
                } else {
                    TextEffect.typeWriter("You don’t have enough shards.", 60);
                }
            } else {
                TextEffect.typeWriter("Invalid choice.", 40);
            }
        } catch (NumberFormatException e) {
            TextEffect.typeWriter("Invalid input.", 40);
        }
    }
}
