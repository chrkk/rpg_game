package rpg.systems;

import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

public class StatusSystem {

    public static void showStatus(Player player, int shards, GameState state) {
        
        // 1. PREPARE DATA: Get the weapon name and progress bar text first
        String weaponName = (player.getWeapon() == null ? "None" : player.getWeapon().toString());
        String progressBar = createProgressBar(state); 

        // 2. BUILD THE DESIGN: Create the box using a simple format
        String border = "+======================================================+";
        
        String output = "\n" + border + "\n" +
            // NAME HEADER
            String.format("| %-52s |\n", player.getName() + " [" + player.getTrait() + "]") +
            "|------------------------------------------------------|\n" +
            
            // STATS COLUMNS (Left Side | Right Side)
            formatTwoColumns("HP: " + player.getHp() + "/" + player.getMaxHp(),      "Mana: " + player.getMana() + "/" + player.getMaxMana()) +
            formatTwoColumns("Lvl: " + player.getLevel(),                             "Exp: " + player.getExp() + "/" + player.getExpToNextLevel()) +
            formatTwoColumns("Def: " + player.getDefense(),                           "Int: " + player.getIntelligence()) +
            formatTwoColumns("Weapon: " + weaponName,                                    "Shards: " + shards) +

            // FOOTER
            "|------------------------------------------------------|\n" +
            String.format("| Progress: %-42s |\n", progressBar) +
            border;

        // 3. DISPLAY
        TextEffect.typeWriter(output, 10);
    }

    // --- HELPER METHODS (These hide the messy logic) ---

    // This makes sure the text fits into two neat columns
    private static String formatTwoColumns(String leftText, String rightText) {
        // %-25s means "make this take up 25 spaces"
        return String.format("| %-25s | %-24s |\n", leftText, rightText);
    }

    // This handles the logic for drawing the [###--] bar
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