package rpg.systems;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class StatusSystem {

    // Main status display
    public static void showStatus(Player player, GameState state) {
        
        // 1. PREPARE DATA
        String weaponName = (player.getWeapon() == null ? "None" : player.getWeapon().toString());
        String progressBar = createProgressBar(state); 

        // 2. BUILD THE DESIGN
        String border = "+======================================================+";
        
        String output = "\n" + border + "\n" +
            // NAME HEADER
            String.format("| %-52s |\n", player.getName() + " [" + player.getTrait() + "]") +
            "|------------------------------------------------------|\n" +
            
            // STATS COLUMNS
            formatTwoColumns("HP: " + player.getHp() + "/" + player.getMaxHp(),      "Mana: " + player.getMana() + "/" + player.getMaxMana()) +
            formatTwoColumns("Lvl: " + player.getLevel(),                            "Exp: " + player.getExp() + "/" + player.getExpToNextLevel()) +
            formatTwoColumns("Def: " + player.getDefense(),                          "Int: " + player.getIntelligence()) +
            formatOneColumn("Weapon: " + weaponName) +
            
            // 🆕 BAG HINT
            formatOneColumn("💼 Bag: Type 'bag' to view items") +

            // FOOTER
            "|------------------------------------------------------|\n" +
            String.format("| Progress: %-42s |\n", progressBar) +
            border;

        // 3. DISPLAY
        TextEffect.typeWriter(output, 10);
    }

    // NEW: Bag display method
    public static void showBag(GameState state) {
        String border = "+======================================================+";
        
        String output = "\n" + border + "\n" +
            "| 💼 BAG                                               |\n" +
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

        TextEffect.typeWriter(output, 10);
    }

    // --- HELPER METHODS ---

    private static String formatTwoColumns(String leftText, String rightText) {
        return String.format("| %-25s | %-24s |\n", leftText, rightText);
    }

    private static String formatOneColumn(String text) {
    return String.format("| %-52s |\n", text);
    }


    // 🆕 Helper for bag items (shows item name and quantity)
    private static String formatBagItem(String itemName, int quantity) {
        return String.format("|   %-25s x%-23d |\n", itemName, quantity);
    }

    // 🆕 Helper for boolean items (shows checkmark or X)
    private static String formatBagCheck(String itemName, boolean acquired) {
        String status = acquired ? "✓" : "✗";
        return String.format("|   %s %-48s |\n", status, itemName);
    }

    private static String createProgressBar(GameState state) {
        int maxSteps = 5;
        String bar = "[S";
        
        if (state.bossGateDiscovered) {
            return "[S#####B] (BOSS GATE)";
        }

        for (int i = 0; i < maxSteps; i++) {
            if (i < state.forwardSteps) bar += "#";
            else bar += "-";
        }
        return bar + "B] Step " + state.forwardSteps;
    }
}