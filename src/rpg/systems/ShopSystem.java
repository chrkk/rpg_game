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
        try {
            TextEffect.typeWriter("🛒 Welcome to my shop! What would you like to buy?", 50);

            // Show basic items
            final int REVIVAL_PRICE = 6;
            final int CRYSTAL_PRICE = 10;

            int menuIndex = 1;
            TextEffect.typeWriter((menuIndex++) + ". Revival Potion (" + REVIVAL_PRICE + " Shards)", 40);
            TextEffect.typeWriter((menuIndex++) + ". Crystal (" + CRYSTAL_PRICE + " Shards)", 40);

            // Show blueprints after basic items
            for (int i = 0; i < inventory.size(); i++) {
                Blueprint bp = inventory.get(i);
                TextEffect.typeWriter((menuIndex++) + ". " + bp.getName() + " (" + bp.getPrice() + " Shards)", 40);
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
                // Map menu number to action
                int base = 1;
                int revivalOption = base; base++;
                int crystalOption = base; base++;
                int firstBlueprintOption = base; // maps to inventory.get(0)

                if (option == revivalOption) {
                    if (state.shards >= REVIVAL_PRICE) {
                        state.shards -= REVIVAL_PRICE;
                        state.revivalPotions += 1;
                        TextEffect.typeWriter("You bought a Revival Potion. You now have " + state.revivalPotions + ".", 60);
                    } else {
                        TextEffect.typeWriter("You don’t have enough shards.", 60);
                    }
                } else if (option == crystalOption) {
                    if (state.shards >= CRYSTAL_PRICE) {
                        state.shards -= CRYSTAL_PRICE;
                        state.crystals += 1;
                        TextEffect.typeWriter("You bought a Crystal. You now have " + state.crystals + " crystals.", 60);
                    } else {
                        TextEffect.typeWriter("You don’t have enough shards.", 60);
                    }
                } else if (option >= firstBlueprintOption && option < firstBlueprintOption + inventory.size()) {
                    int idx = option - firstBlueprintOption;
                    Blueprint selected = inventory.get(idx);

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
                TextEffect.typeWriter("Invalid input. Please enter a number.", 40);
                System.err.println("Shop input error -> " + e.getMessage());
            }

        } catch (Exception e) {
            TextEffect.typeWriter("Something went wrong while using the shop.", 40);
            System.err.println("Shop system error -> " + e.getMessage());
        } finally {
            // Always runs after shop interaction
            // Could be used for logging or cleanup
        }
    }
}
