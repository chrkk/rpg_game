package rpg.systems;

import java.util.*;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.items.Blueprint;

public class ShopSystem {

    public static void openShop(GameState state, Scanner scanner) {
        try {
            TextEffect.typeWriter("🛒 [System] > Merchant Interface Loaded. Browse wares.", 50);

            // Get available blueprints based on current zone
            List<Blueprint> availableBlueprints = getAvailableBlueprints(state);

            final int REVIVAL_PRICE = 6;
            final int CRYSTAL_PRICE = 10;

            int menuIndex = 1;
            TextEffect.typeWriter((menuIndex++) + ". Revival Potion (" + REVIVAL_PRICE + " Shards)", 40);
            TextEffect.typeWriter((menuIndex++) + ". Crystal (" + CRYSTAL_PRICE + " Shards)", 40);

            // Show only zone-appropriate blueprints
            for (int i = 0; i < availableBlueprints.size(); i++) {
                Blueprint bp = availableBlueprints.get(i);
                
                // Show if already unlocked OR if it's available in current zone
                boolean alreadyUnlocked = state.unlockedRecipes.contains(bp.getUnlocksRecipe());
                String status = alreadyUnlocked ? " [OWNED]" : "";
                
                TextEffect.typeWriter((menuIndex++) + ". " + bp.getName() + " (" + bp.getPrice() + " Shards)" + status, 40);
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
                
                int base = 1;
                int revivalOption = base; base++;
                int crystalOption = base; base++;
                int firstBlueprintOption = base;

                if (option == revivalOption) {
                    if (state.shards >= REVIVAL_PRICE) {
                        state.shards -= REVIVAL_PRICE;
                        state.revivalPotions += 1;
                        TextEffect.typeWriter("You bought a Revival Potion. You now have " + state.revivalPotions + ".", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough shards.", 60);
                    }
                } else if (option == crystalOption) {
                    if (state.shards >= CRYSTAL_PRICE) {
                        state.shards -= CRYSTAL_PRICE;
                        state.crystals += 1;
                        TextEffect.typeWriter("You bought a Crystal. You now have " + state.crystals + " crystals.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough shards.", 60);
                    }
                } else if (option >= firstBlueprintOption && option < firstBlueprintOption + availableBlueprints.size()) {
                    int idx = option - firstBlueprintOption;
                    Blueprint selected = availableBlueprints.get(idx);

                    // Check if already owned
                    if (state.unlockedRecipes.contains(selected.getUnlocksRecipe())) {
                        TextEffect.typeWriter("You already own this blueprint!", 60);
                        return;
                    }

                    if (state.shards >= selected.getPrice()) {
                        state.shards -= selected.getPrice();
                        state.unlockedRecipes.add(selected.getUnlocksRecipe());
                        TextEffect.typeWriter("You bought the " + selected.getName() +
                            "! You can now discover the " + selected.getUnlocksRecipe() + " recipe in the world.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough shards.", 60);
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
        }
    }

    private static List<Blueprint> getAvailableBlueprints(GameState state) {
        List<Blueprint> available = new ArrayList<>();
        
        if (state.zone >= 2) {
            available.add(new Blueprint("Logic Blade Blueprint", "Logic Blade", 10));
        }
        
        if (state.zone >= 3) {
            available.add(new Blueprint("Aftershock Hammer Blueprint", "Aftershock Hammer", 15));
        }
        
        // ✅ UPDATED: Trident of Storms
        if (state.zone >= 4) {
            available.add(new Blueprint("Trident of Storms Blueprint", "Trident of Storms", 25));
        }
        
        return available;
    }
}