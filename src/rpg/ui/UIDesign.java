package rpg.ui;

import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.game.GameState;
import rpg.world.WorldData;

/**
 * OPTION 1: ASCII FRAMES (Retro RPG Style)
 * Provides consistent UI across combat and safe zone systems
 */
public class UIDesign {

    // ═══════════════════════════════════════════════════════════
    // COMBAT SYSTEM UI - For both Tutorial and Main Combat
    // ═══════════════════════════════════════════════════════════

    /**
     * Display the main combat frame showing player and enemy stats
     * @param player The player character
     * @param enemy The enemy being fought
     * @param enemyMaxHp The enemy's maximum HP (tracked from combat start)
     * @param flavorText Optional description of the enemy's state
     */
    public static void displayCombatFrame(Player player, Enemy enemy, int enemyMaxHp, String flavorText) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🗡️  COMBAT ARENA  ⚔️                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        System.out.printf("║   [ENEMY] %-46s ║%n", truncate(enemy.getName(), 46));
        System.out.printf("║   HP: %-50s ║%n", createHealthBar(enemy.getHp(), enemyMaxHp, 20));
        
        if (flavorText != null && !flavorText.isEmpty()) {
            System.out.println("║                                                          ║");
            System.out.printf("║   ⚡ %-52s ║%n", truncate(flavorText, 52));
        }
        
        System.out.println("║                                                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        
        String playerTitle = player.getName() + " [" + player.getTrait() + "]";
        System.out.printf("║   [YOU] %-36s Lvl %-3d ║%n", truncate(playerTitle, 36), player.getLevel());
        System.out.printf("║   HP: %-24s  MP: %-21s ║%n",
            createHealthBar(player.getHp(), player.getMaxHp(), 10),
            createManaBar(player.getMana(), player.getMaxMana(), 8));
        
        if (player.getWeapon() != null) {
            System.out.printf("║   Weapon: %-46s ║%n", 
                truncate(player.getWeapon().getName(), 46));
        }
        
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║   > [A]ttack  [D]efend  [S]kill  [I]tem  [R]un          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    /**
     * Display combat action menu based on available abilities
     * @param hasSkills Whether the player has unlocked skills
     */
    public static void displayCombatActions(boolean hasSkills) {
        System.out.println("╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ⚔️  YOUR TURN  ⚔️                          ║");
        System.out.println("╠═════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                             ║");
        System.out.println("║   Type 'attack'  → ⚔️  Strike with your weapon               ║");
        System.out.println("║   Type 'defend'  → 🛡️  Brace for incoming damage             ║");
        
        if (hasSkills) {
            System.out.println("║   Type 'skill'   → 🔮 Cast a powerful ability               ║");
        }
        
        System.out.println("║   Type 'item'    → 🎒 Use consumable from bag               ║");
        System.out.println("║   Type 'run'     → 🏃 Attempt to flee combat                ║");
        System.out.println("║                                                             ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    }

    /**
     * Simple combat status update (used during combat loop)
     * @param player The player character
     * @param enemy The enemy being fought
     * @param enemyMaxHp The enemy's maximum HP (tracked from combat start)
     */
    public static void displayCombatStatus(Player player, Enemy enemy, int enemyMaxHp) {
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.printf("║   [ENEMY] %-24s HP: %-20s ║%n", 
            truncate(enemy.getName(), 24),
            createHealthBar(enemy.getHp(), enemyMaxHp, 10));
        System.out.printf("║   [YOU]   %-24s HP: %-20s ║%n",
            truncate(player.getName(), 24),
            createHealthBar(player.getHp(), player.getMaxHp(), 10));
        System.out.println("╚═════════════════════════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════════════════════
    // BAG SYSTEM UI
    // ═══════════════════════════════════════════════════════════

    /**
     * Display the bag/inventory UI
     * @param state Game state containing inventory items
     */
    public static void displayBag(GameState state) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                      💼 BAG                              ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        
        // MATERIALS SECTION
        System.out.println("║                                                          ║");
        System.out.println("║  📦 MATERIALS                                            ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        System.out.printf("║     Crystals %-43s ║%n", "x" + state.crystals);
        System.out.printf("║     Shards %-45s ║%n", "x" + state.shards);
        System.out.println("║                                                          ║");
        
        // CONSUMABLES SECTION
        System.out.println("║  🍖 CONSUMABLES                                          ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        System.out.printf("║     Meat %-47s ║%n", "x" + state.meat);
        System.out.printf("║     Revival Potions %-36s ║%n", "x" + state.revivalPotions);
        System.out.println("║                                                          ║");
        
        // CRAFTED WEAPONS (Key Items)
        System.out.println("║  ⚔️  CRAFTED WEAPONS                                      ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        displayBagCheck("Stage 1: Pencil Blade", state.stage1WeaponCrafted);
        displayBagCheck("Stage 2: Crystal Sword", state.stage2WeaponCrafted);
        displayBagCheck("Stage 3: Flame Axe", state.stage3WeaponCrafted);
        displayBagCheck("Stage 4: Thunder Spear", state.stage4WeaponCrafted);
        System.out.println("║                                                          ║");
        
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    /**
     * Helper for displaying checkbox items in bag
     */
    private static void displayBagCheck(String itemName, boolean acquired) {
        String status = acquired ? "✓" : "✗";
        System.out.printf("║     %s %-48s   ║%n", status, itemName);
    }

    // ═══════════════════════════════════════════════════════════
    // STATUS SYSTEM UI
    // ═══════════════════════════════════════════════════════════

    /**
     * Display the player status screen
     * @param player The player character
     * @param state Game state for progress tracking
     */
    public static void displayStatus(Player player, GameState state) {
        String weaponName = (player.getWeapon() == null ? "None" : player.getWeapon().toString());
        String progressBar = createProgressBarForStatus(state);
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.printf("║  %-55s ║%n", player.getName() + " [" + player.getTrait() + "]");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        
        // STATS - Two Column Layout
        System.out.printf("║  %-26s %-27s   ║%n", 
            "❤️  HP: " + player.getHp() + "/" + player.getMaxHp(),
            "🔮 Mana: " + player.getMana() + "/" + player.getMaxMana());
        System.out.printf("║  %-25s %-27s  ║%n",
            "📊 Lvl: " + player.getLevel(),
            "✨ Exp: " + player.getExp() + "/" + player.getExpToNextLevel());
        System.out.printf("║  %-27s %-27s   ║%n",
            "🛡️  Def: " + player.getDefense(),
            "🧠 Int: " + player.getIntelligence());
        
        System.out.println("║                                                          ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        System.out.println("║                                                          ║");
        
        // WEAPON INFO
        System.out.printf("║  ⚔️  Weapon: %-44s ║%n", truncate(weaponName, 44));
        
        System.out.println("║                                                          ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        System.out.println("║                                                          ║");
        
        // SKILLS STATUS
        if (player.getLevel() >= 2 && player.getSkills().length > 0) {
            System.out.println("║  🔮 Skills: UNLOCKED                                     ║");
        } else {
            System.out.println("║  🔮 Skills: LOCKED (Unlock at Level 2)                   ║");
        }
        
        System.out.println("║                                                          ║");
        System.out.println("║  ────────────────────────────────────────────────────    ║");
        System.out.println("║                                                          ║");
        
        // PROGRESS BAR
        System.out.printf("║  Progress: %-45s ║%n", progressBar);
        
        System.out.println("║                                                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  💡 Tip: Type 'bag' to view your inventory               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    /**
     * Create progress bar for status screen
     */
    private static String createProgressBarForStatus(GameState state) {
        if (state.bossGateDiscovered) {
            return "[S#####B] (BOSS GATE DISCOVERED)";
        }
        StringBuilder bar = new StringBuilder("[S");
        for (int i = 0; i < 5; i++) {
            bar.append(i < state.forwardSteps ? "#" : "-");
        }
        bar.append("B] Step ").append(state.forwardSteps).append("/5");
        return bar.toString();
    }

    // ═══════════════════════════════════════════════════════════
    // SAFE ZONE HUB UI
    // ═══════════════════════════════════════════════════════════

    /**
     * Display the safe zone hub menu with all available actions
     * @param zone Current zone number
     * @param shopUnlocked Whether the shop is available
     */
    public static void displaySafeZoneHub(int zone, boolean shopUnlocked) {
        String zoneName = getZoneName(zone);
        String zoneDescription = getZoneDescription(zone);
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.printf("║                  🏠 %-35s  ║%n", zoneName.toUpperCase());
        System.out.printf("║              (Zone %d Safe Haven)                         ║%n", zone);
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        
        // Display zone description (wrapped)
        wrapAndPrint(zoneDescription, 54);
        
        System.out.println("║                                                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Available Actions:                                      ║");
        System.out.println("║                                                          ║");
        System.out.println("║  [craft]     ⚒️  Crafting Bench                           ║");
        System.out.println("║  [search]    🔍 Search Area                              ║");
        System.out.println("║  [supporter] 👥 Supporter Hub                            ║");
        
        if (shopUnlocked) {
            System.out.println("║  [shop]      🛒 Visit Shop                               ║");
        }
        
        System.out.println("║  [status]    📊 View Status                              ║");
        System.out.println("║  [bag]       🎒 Open Bag                                 ║");
        System.out.println("║  [move]      🚪 Leave Safe Zone                          ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    /**
     * Display the safe zone hub but hide the supporter option (used for tutorial / Rooftop)
     */
    public static void displaySafeZoneHubNoSupporter(int zone, boolean shopUnlocked) {
        String zoneName = getZoneName(zone);
        String zoneDescription = getZoneDescription(zone);
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.printf("║                  🏠 %-35s  ║%n", zoneName.toUpperCase());
        System.out.printf("║              (Zone %d Safe Haven)                         ║%n", zone);
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        
        // Display zone description (wrapped)
        wrapAndPrint(zoneDescription, 54);
        
        System.out.println("║                                                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Available Actions:                                      ║");
        System.out.println("║                                                          ║");
        System.out.println("║  [craft]     ⚒️  Crafting Bench                           ║");
        System.out.println("║  [search]    🔍 Search Area                              ║");
        // NOTE: Supporter option intentionally omitted here
        
        if (shopUnlocked) {
            System.out.println("║  [shop]      🛒 Visit Shop                               ║");
        }
        
        System.out.println("║  [status]    📊 View Status                              ║");
        System.out.println("║  [bag]       🎒 Open Bag                                 ║");
        System.out.println("║  [move]      🚪 Leave Safe Zone                          ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════════════════════
    // HELPER METHODS - Health/Mana Bars
    // ═══════════════════════════════════════════════════════════

    /**
     * Create a health bar with filled/empty blocks
     * @param current Current HP
     * @param max Maximum HP
     * @param barLength Length of the bar in characters
     * @return Formatted health bar string
     */
    private static String createHealthBar(int current, int max, int barLength) {
        if (max <= 0) max = 1; // Prevent division by zero
        if (current < 0) current = 0;
        if (current > max) current = max;
        
        int filled = (int) ((double) current / max * barLength);
        StringBuilder bar = new StringBuilder("[");
        
        for (int i = 0; i < barLength; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        
        bar.append("] ").append(current).append("/").append(max);
        return bar.toString();
    }

    /**
     * Create a mana bar with filled/empty blocks
     * @param current Current Mana
     * @param max Maximum Mana
     * @param barLength Length of the bar in characters
     * @return Formatted mana bar string
     */
    private static String createManaBar(int current, int max, int barLength) {
        if (max <= 0) max = 1;
        if (current < 0) current = 0;
        if (current > max) current = max;
        
        int filled = (int) ((double) current / max * barLength);
        StringBuilder bar = new StringBuilder("[");
        
        for (int i = 0; i < barLength; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        
        bar.append("] ").append(current).append("/").append(max);
        return bar.toString();
    }

    // ═══════════════════════════════════════════════════════════
    // HELPER METHODS - Zone Information
    // ═══════════════════════════════════════════════════════════

    /**
     * Get zone name by zone number
     */
    private static String getZoneName(int zone) {
        switch (zone) {
            case 1: return "School Rooftop";
            case 2: return "Ruined Lab";
            case 3: return "City Ruins";
            case 4: return "Observation Deck";
            default: return "Unknown Zone";
        }
    }

    /**
     * Get zone description for safe zone display
     */
    private static String getZoneDescription(int zone) {
        switch (zone) {
            case 1: return "The wind is calm here. Vines creep across shattered tiles.";
            case 2: return "The air smells of rust and chemicals. Emergency lights flicker.";
            case 3: return "A makeshift shelter built from rubble. The fire crackles softly.";
            case 4: return "The observation deck sways gently. Storm clouds part overhead.";
            default: return "You feel safe here. Your wounds begin to heal.";
        }
    }

    // ═══════════════════════════════════════════════════════════
    // HELPER METHODS - Text Formatting
    // ═══════════════════════════════════════════════════════════

    /**
     * Truncate text to fit within UI boundaries
     */
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Wrap and print text within the UI frame
     */
    // private static void wrapAndPrint(String text, int maxWidth) {
    //     if (text == null || text.isEmpty()) return;
        
    //     String[] words = text.split(" ");
    //     StringBuilder line = new StringBuilder("║  ");
        
    //     for (String word : words) {
    //         if (line.length() + word.length() + 1 > maxWidth + 3) {
    //             // Pad and print current line
    //             while (line.length() < 60) line.append(" ");
    //             line.append("║");
    //             System.out.println(line);
    //             line = new StringBuilder("║  " + word + " ");
    //         } else {
    //             line.append(word).append(" ");
    //         }
    //     }
        
    //     // Print remaining line
    //     if (line.length() > 3) {
    //         while (line.length() < 60) line.append(" ");
    //         line.append("║");
    //         System.out.println(line);
    //     }
    // }
    /**
 * Wrap and print text within the UI frame
 */
private static void wrapAndPrint(String text, int maxWidth) {
    if (text == null || text.isEmpty()) return;
    
    String[] words = text.split(" ");
    StringBuilder line = new StringBuilder("║  ");
    
    for (String word : words) {
        // Check if adding this word would exceed the width
        if (line.length() + word.length() + 1 > maxWidth + 3) {
            // Pad current line to exactly 60 characters before the final ║
            while (line.length() < 59) line.append(" ");
            line.append("║");
            System.out.println(line);
            // Start new line
            line = new StringBuilder("║  " + word + " ");
        } else {
            line.append(word).append(" ");
        }
    }
    
    // Print the last line if it has content
    if (line.length() > 3) {  // More than just "║  "
        while (line.length() < 59) line.append(" ");
        line.append("║");
        System.out.println(line);
    }
}
    // ═══════════════════════════════════════════════════════════
    // ITEM MENU UI (During Combat)
    // ═══════════════════════════════════════════════════════════

    /**
     * Display the item selection menu during combat
     * @param state Game state containing inventory items
     */
    public static void displayItemMenu(GameState state) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎒 USE ITEM                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        
        boolean hasItems = false;
        int optionNum = 1;
        
        // Show meat if available
        if (state.meat > 0) {
            System.out.printf("║  [%d] 🍖 Meat                    Heal 10 HP      x%-4d    ║%n", 
                optionNum++, state.meat);
            hasItems = true;
        }
        
        // Show revival potions (but can't use in combat)
        if (state.revivalPotions > 0) {
            System.out.printf("║  [%d] 💊 Revival Potion          (Combat Lock)  x%-4d     ║%n", 
                optionNum++, state.revivalPotions);
            System.out.println("║      └─ Can only be used outside of combat               ║");
            hasItems = true;
        }
        
        if (!hasItems) {
            System.out.println("║  Your bag is empty! No consumables available.            ║");
        }
        
        System.out.println("║                                                          ║");
        System.out.println("║  [0] ← Back to Combat                                    ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

}