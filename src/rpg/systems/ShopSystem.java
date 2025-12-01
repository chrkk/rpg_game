// package rpg.systems;

// import java.util.*;
// import rpg.utils.TextEffect;
// import rpg.game.GameState;
// import rpg.items.Blueprint;

// public class ShopSystem {

//     public static void openShop(GameState state, Scanner scanner) {
//         try {
//             TextEffect.typeWriter("🛒 [System] > Merchant Interface Loaded. Browse wares.", 50);

//             // Get available blueprints based on current zone
//             List<Blueprint> availableBlueprints = getAvailableBlueprints(state);

//             final int REVIVAL_PRICE = 6;
//             final int CRYSTAL_PRICE = 10;
//             final int MEDIUM_POTION_PRICE = 4;

//             int menuIndex = 1;
//             TextEffect.typeWriter((menuIndex++) + ". Revival Potion (" + REVIVAL_PRICE + " Shards)", 40);
//             TextEffect.typeWriter((menuIndex++) + ". Crystal (" + CRYSTAL_PRICE + " Shards)", 40);
//             TextEffect.typeWriter((menuIndex++) + ". Potion (Medium) (" + MEDIUM_POTION_PRICE + " Shards)", 40);

//             // Show only zone-appropriate blueprints
//             for (int i = 0; i < availableBlueprints.size(); i++) {
//                 Blueprint bp = availableBlueprints.get(i);
                
//                 // Show if already unlocked OR if it's available in current zone
//                 boolean alreadyUnlocked = state.unlockedRecipes.contains(bp.getUnlocksRecipe());
//                 String status = alreadyUnlocked ? " [OWNED]" : "";
                
//                 TextEffect.typeWriter((menuIndex++) + ". " + bp.getName() + " (" + bp.getPrice() + " Shards)" + status, 40);
//             }

//             TextEffect.typeWriter("0. Leave shop", 40);

//             System.out.print("> ");
//             String choice = scanner.nextLine();

//             try {
//                 int option = Integer.parseInt(choice);
//                 if (option == 0) {
//                     TextEffect.typeWriter("You leave the shop.", 40);
//                     return;
//                 }
                
//                 int base = 1;
//                 int revivalOption = base; base++;
//                 int crystalOption = base; base++;
//                 int mediumPotionOption = base; base++;
//                 int firstBlueprintOption = base;

//                 if (option == revivalOption) {
//                     if (state.shards >= REVIVAL_PRICE) {
//                         state.shards -= REVIVAL_PRICE;
//                         state.revivalPotions += 1;
//                         TextEffect.typeWriter("You bought a Revival Potion. You now have " + state.revivalPotions + ".", 60);
//                     } else {
//                         TextEffect.typeWriter("You don't have enough shards.", 60);
//                     }
//                 } else if (option == crystalOption) {
//                     if (state.shards >= CRYSTAL_PRICE) {
//                         state.shards -= CRYSTAL_PRICE;
//                         state.crystals += 1;
//                         TextEffect.typeWriter("You bought a Crystal. You now have " + state.crystals + " crystals.", 60);
//                     } else {
//                         TextEffect.typeWriter("You don't have enough shards.", 60);
//                     }
//                 } else if (option == mediumPotionOption) {
//                     if (state.shards >= MEDIUM_POTION_PRICE) {
//                         state.shards -= MEDIUM_POTION_PRICE;
//                         state.mediumPotions += 1;
//                         TextEffect.typeWriter("You bought a Potion (Medium). You now have " + state.mediumPotions + ".", 60);
//                     } else {
//                         TextEffect.typeWriter("You don't have enough shards.", 60);
//                     }
//                 } else if (option >= firstBlueprintOption && option < firstBlueprintOption + availableBlueprints.size()) {
//                     int idx = option - firstBlueprintOption;
//                     Blueprint selected = availableBlueprints.get(idx);

//                     // Check if already owned
//                     if (state.unlockedRecipes.contains(selected.getUnlocksRecipe())) {
//                         TextEffect.typeWriter("You already own this blueprint!", 60);
//                         return;
//                     }

//                     if (state.shards >= selected.getPrice()) {
//                         state.shards -= selected.getPrice();
//                         state.unlockedRecipes.add(selected.getUnlocksRecipe());
//                         TextEffect.typeWriter("You bought the " + selected.getName() +
//                             "! You can now discover the " + selected.getUnlocksRecipe() + " recipe in the world.", 60);
//                     } else {
//                         TextEffect.typeWriter("You don't have enough shards.", 60);
//                     }
//                 } else {
//                     TextEffect.typeWriter("Invalid choice.", 40);
//                 }
//             } catch (NumberFormatException e) {
//                 TextEffect.typeWriter("Invalid input. Please enter a number.", 40);
//                 System.err.println("Shop input error -> " + e.getMessage());
//             }

//         } catch (Exception e) {
//             TextEffect.typeWriter("Something went wrong while using the shop.", 40);
//             System.err.println("Shop system error -> " + e.getMessage());
//         }
//     }

//     private static List<Blueprint> getAvailableBlueprints(GameState state) {
//         List<Blueprint> available = new ArrayList<>();
        
//         if (state.zone >= 2) {
//             available.add(new Blueprint("Logic Blade Blueprint", "Logic Blade", 10));
//         }
        
//         if (state.zone >= 3) {
//             available.add(new Blueprint("Aftershock Hammer Blueprint", "Aftershock Hammer", 15));
//         }
        
//         // ✅ UPDATED: Trident of Storms
//         if (state.zone >= 4) {
//             available.add(new Blueprint("Trident of Storms Blueprint", "Trident of Storms", 25));
//         }
        
//         return available;
//     }
// }

package rpg.systems;

import java.util.*;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.items.Blueprint;

public class ShopSystem {

    public static void openShop(GameState state, Scanner scanner) {
        try {
            // ✅ UPDATED: Display fancy shop header with current shards
            displayShopHeader(state.shards);

            List<Blueprint> availableBlueprints = getAvailableBlueprints(state);

            final int REVIVAL_PRICE = 6;
            final int CRYSTAL_PRICE = 10;
            final int MEDIUM_POTION_PRICE = 4;
            final int MANA_POTION_PRICE = 5;

            int menuIndex = 1;
            System.out.println("║                                                          ║");
            displayShopItem(menuIndex++, "Revival Potion", REVIVAL_PRICE, false);
            displayShopItem(menuIndex++, "Crystal", CRYSTAL_PRICE, false);
            displayShopItem(menuIndex++, "Potion (Medium)", MEDIUM_POTION_PRICE, false);
            displayShopItem(menuIndex++, "Mana Potion", MANA_POTION_PRICE, false);

            // Show zone-appropriate blueprints
            for (Blueprint bp : availableBlueprints) {
                boolean alreadyUnlocked = state.unlockedRecipes.contains(bp.getUnlocksRecipe());
                displayShopItem(menuIndex++, bp.getName(), bp.getPrice(), alreadyUnlocked);
            }

            System.out.println("║                                                          ║");
            System.out.println("║  [0] 🚪 Leave Shop                                       ║");
            System.out.println("║                                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");

            System.out.print("> ");
            String choice = scanner.nextLine();

            try {
                int option = Integer.parseInt(choice);
                if (option == 0) {
                    TextEffect.typeWriter("You leave the shop.", 40);
                    return;
                }
                
                int base = 1;
                int revivalOption = base++;
                int crystalOption = base++;
                int mediumPotionOption = base++;
                int manaPotionOption = base++;
                int firstBlueprintOption = base;

                if (option == revivalOption) {
                    purchaseItem(state, "Revival Potion", REVIVAL_PRICE, () -> state.revivalPotions++);
                } else if (option == crystalOption) {
                    purchaseItem(state, "Crystal", CRYSTAL_PRICE, () -> state.crystals++);
                } else if (option == mediumPotionOption) {
                    purchaseItem(state, "Potion (Medium)", MEDIUM_POTION_PRICE, () -> state.mediumPotions++);
                } else if (option == manaPotionOption) {
                    purchaseItem(state, "Mana Potion", MANA_POTION_PRICE, () -> state.manaPotions++);
                } else if (option >= firstBlueprintOption && option < firstBlueprintOption + availableBlueprints.size()) {
                    int idx = option - firstBlueprintOption;
                    Blueprint selected = availableBlueprints.get(idx);

                    if (state.unlockedRecipes.contains(selected.getUnlocksRecipe())) {
                        TextEffect.typeWriter("You already own this blueprint!", 60);
                        return;
                    }

                    if (state.shards >= selected.getPrice()) {
                        state.shards -= selected.getPrice();
                        state.unlockedRecipes.add(selected.getUnlocksRecipe());
                        TextEffect.typeWriter("You bought the " + selected.getName() +
                            "! You can now discover the " + selected.getUnlocksRecipe() + " recipe in the world.", 60);
                        TextEffect.typeWriter("Remaining Shards: " + state.shards, 40);
                    } else {
                        TextEffect.typeWriter("You don't have enough shards. (Need: " + selected.getPrice() + ", Have: " + state.shards + ")", 60);
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

    // ✅ NEW: Display shop header with current shards
    private static void displayShopHeader(int currentShards) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🛒  MERCHANT SHOP  🛒                 ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.printf("║  💎 Your Shards: %-39d ║%n", currentShards);
        System.out.println("╠══════════════════════════════════════════════════════════╣");
    }

     // ✅ NEW: Display individual shop items with better formatting
    private static void displayShopItem(int index, String itemName, int price, boolean owned) {
        String status = owned ? " [OWNED]" : "";
        
        // Check if it's a blueprint (longer name with "Blueprint" in it)
        boolean isBlueprint = itemName.toLowerCase().contains("blueprint");
        
        if (isBlueprint) {
            // Blueprint format - slightly tighter spacing
            String line = String.format("║  [%d] %-32s %3d 💎%s     ║", 
                index, 
                truncate(itemName, 32), 
                price,
                status
            );
            System.out.println(line);
        } else {
            // Regular item format
            String line = String.format("║  [%d] %-35s %3d 💎%-7s   ║", 
                index, 
                truncate(itemName, 35), 
                price,
                status
            );
            System.out.println(line);
        }
    }

    // ✅ NEW: Helper method for purchasing with feedback
    private static void purchaseItem(GameState state, String itemName, int price, Runnable onPurchase) {
        if (state.shards >= price) {
            state.shards -= price;
            onPurchase.run();
            TextEffect.typeWriter("✅ Purchase successful! You bought " + itemName + ".", 60);
            TextEffect.typeWriter("Remaining Shards: " + state.shards, 40);
        } else {
            TextEffect.typeWriter("❌ Not enough shards. (Need: " + price + ", Have: " + state.shards + ")", 60);
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
        
        if (state.zone >= 4) {
            available.add(new Blueprint("Trident of Storms Blueprint", "Trident of Storms", 25));
        }
        
        return available;
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}