package rpg.systems;

import java.util.Scanner;
import rpg.game.GameState;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.items.Consumable;

public class BagSystem {
    static Scanner scanner = new Scanner(System.in);
    // Main bag display method
    public static void showBag(GameState state) {
        String border = "+======================================================+";
        
        String output = "\n" + border + "\n" +
            "| 💼 BAG                                                |\n" +
            "|------------------------------------------------------|\n" +
            
            // MATERIALS SECTION
            "| MATERIALS                                            |\n" +
            formatBagItem("Crystals", state.crystals) +
            formatBagItem("Shards", state.shards) +
            "|------------------------------------------------------|\n" +
            
            // CONSUMABLES SECTION
            "| CONSUMABLES                                          |\n" +
            formatBagItem("Meat", state.meat) +
            formatBagItem("Revival Potions", state.revivalPotions) +
            "|------------------------------------------------------|\n" +
            
            // KEY ITEMS SECTION (showing crafted weapons as achievements)
            "| CRAFTED WEAPONS                                      |\n" +
            formatBagCheck("Stage 1: Pencil Blade", state.stage1WeaponCrafted) +
            formatBagCheck("Stage 2: Crystal Sword", state.stage2WeaponCrafted) +
            formatBagCheck("Stage 3: Flame Axe", state.stage3WeaponCrafted) +
            formatBagCheck("Stage 4: Thunder Spear", state.stage4WeaponCrafted) +
            
            border;

        System.out.println(output);  // Direct print - no text effect!
    }

    // --- HELPER METHODS ---

    // Helper for bag items (shows item name and quantity)
    private static String formatBagItem(String itemName, int quantity) {
        return String.format("|   %-25s x%-23d |\n", itemName, quantity);
    }

    // Helper for boolean items (shows checkmark or X)
    private static String formatBagCheck(String itemName, boolean acquired) {
        String status = acquired ? "✓" : "✗";
        return String.format("|   %s %-48s |\n", status, itemName);
    }


// 🆕 NEW: Show available items in combat
    public static void showItemMenu(Player player, GameState state) {
        TextEffect.typeWriter("\n📦 Available Items:", 30);
        
        int optionNumber = 1;
        boolean hasItems = false;
        
        // Show meat if available
        if (state.meat > 0) {
            TextEffect.typeWriter(optionNumber + ". Meat (Heal 10 HP) x" + state.meat, 30);
            hasItems = true;
            optionNumber++;
        }
        
        // Show revival potions (but can't use in combat)
        if (state.revivalPotions > 0) {
            TextEffect.typeWriter(optionNumber + ". Revival Potion x" + state.revivalPotions + " (Can't use in combat)", 30);
            hasItems = true;
            optionNumber++;
        }
        
        if (!hasItems) {
            TextEffect.typeWriter("Your bag is empty! No consumables available.", 40);
            return;
        }
        
        TextEffect.typeWriter("0. Cancel", 30);
        
        System.out.print("> Choose item: ");
        String choice = scanner.nextLine();
        
        try {
            int option = Integer.parseInt(choice);
            
            if (option == 0) {
                TextEffect.typeWriter("You close your bag.", 40);
                return;
            }
            
            // Use the selected item
            useItemByOption(option, player, state);
            
        } catch (NumberFormatException e) {
            TextEffect.typeWriter("Invalid choice!", 40);
        }
    }

    // 🆕 NEW: Handle item usage based on selection
    private static void useItemByOption(int option, Player player, GameState state) {
        int currentOption = 1;
        
        // Option 1: Meat (if available)
        if (state.meat > 0) {
            if (option == currentOption) {
                // Check if player is at max HP
                if (player.getHp() == player.getMaxHp()) {
                    TextEffect.typeWriter("Your HP is already full! No need to use Meat.", 40);
                    return;
                }
                
                Consumable meat = new Consumable("Meat", 10);
                meat.consume(player, state);
                return;
            }
            currentOption++;
        }
        
        // Option 2: Revival Potion (if available)
        if (state.revivalPotions > 0) {
            if (option == currentOption) {
                TextEffect.typeWriter("Revival Potions can only be used outside combat!", 40);
                return;
            }
            currentOption++;
        }
        
        TextEffect.typeWriter("Invalid choice!", 40);
    }










}