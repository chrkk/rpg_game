// package rpg.systems;

// import rpg.characters.Player;
// import rpg.game.GameState;
// import rpg.utils.TextEffect;

// public class StatusSystem {

//     // Main status display
//     public static void showStatus(Player player, GameState state) {
        
//         // 1. PREPARE DATA
//         String weaponName = (player.getWeapon() == null ? "None" : player.getWeapon().toString());
//         String progressBar = createProgressBar(state); 

//         // 2. BUILD THE DESIGN
//         String border = "+======================================================+";
        
//         String output = "\n" + border + "\n" +
//             // NAME HEADER
//             String.format("| %-52s |\n", player.getName() + " [" + player.getTrait() + "]") +
//             "|------------------------------------------------------|\n" +
            
//             // STATS COLUMNS
//             formatTwoColumns("HP: " + player.getHp() + "/" + player.getMaxHp(),      "Mana: " + player.getMana() + "/" + player.getMaxMana()) +
//             formatTwoColumns("Lvl: " + player.getLevel(),                            "Exp: " + player.getExp() + "/" + player.getExpToNextLevel()) +
//             formatTwoColumns("Def: " + player.getDefense(),                          "Int: " + player.getIntelligence()) +
//             formatOneColumn("Weapon: " + weaponName) +
            
//             // 🆕 BAG HINT
//             formatOneColumn("💼 Bag: Type 'bag' to view items") +

//             // FOOTER
//             "|------------------------------------------------------|\n" +
//             String.format("| Progress: %-42s |\n", progressBar) +
//             border;

//         // 3. DISPLAY
//         System.out.println(output);
//     }

   

//     // --- HELPER METHODS ---
//     private static String formatTwoColumns(String leftText, String rightText) {
//         return String.format("| %-25s | %-24s |\n", leftText, rightText);
//     }

//     private static String formatOneColumn(String text) {
//     return String.format("| %-52s |\n", text);
//     }

//     private static String createProgressBar(GameState state) {
//         int maxSteps = 5;
//         String bar = "[S";
        
//         if (state.bossGateDiscovered) {
//             return "[S#####B] (BOSS GATE)";
//         }

//         for (int i = 0; i < maxSteps; i++) {
//             if (i < state.forwardSteps) bar += "#";
//             else bar += "-";
//         }
//         return bar + "B] Step " + state.forwardSteps;
//     }


// }


package rpg.systems;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;
import rpg.ui.UIDesign; // 🆕 NEW IMPORT

public class StatusSystem {

    // Main status display - NOW USES FANCY UI
    public static void showStatus(Player player, GameState state) {
        UIDesign.displayStatus(player, state);
    }

    // Bag display - NOW USES FANCY UI
    public static void showBag(GameState state) {
        UIDesign.displayBag(state);
    }

    public static void showItemMenu(GameState state) {
        UIDesign.displayItemMenu(state);
    }
}