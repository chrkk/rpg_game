package rpg.ui_design;

import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.game.GameState;
import rpg.utils.TextEffect;
import rpg.ui.UIDesign;

public class Intro {
    
    // public static void displayMainMenu() {
    //     System.out.println("\n╔══════════════════════════════════════════════════════════╗");
    //     System.out.println("║                                                          ║");
    //     System.out.println("║                    DR. CAPSTONE                          ║");
    //     System.out.println("║               A Simulation of Survival                   ║");
    //     System.out.println("║                                                          ║");
    //     System.out.println("╠══════════════════════════════════════════════════════════╣");
    //     System.out.println("║                                                          ║");
    //     System.out.println("║  [1] 🎮 START GAME                                       ║");
    //     System.out.println("║  [2] 🚪 EXIT                                             ║");
    //     System.out.println("║                                                          ║");
    //     System.out.println("║  💡 Developer: Type 'devmenu' for debug options          ║");
    //     System.out.println("║                                                          ║");
    //     System.out.println("╚══════════════════════════════════════════════════════════╝");
    //     System.out.print("\n> INPUT: ");
    // }

    //initial idea -> open for suggestions
    public static void displayMainMenu() {
        System.out.println();
        System.out.println("    ═══════════════════════════════════════════════════");
        System.out.println("    ║                                                 ║");
        System.out.println("    ║    ██░░░░░▒▒▒▒▒▓▓▓▓▓  R̷E̴A̵L̴I̵T̶Y̴  ▓▓▓▓▒▒▒░░░░██    ║");
        System.out.println("    ║    ██░░░░░▒▒▒▒▒▓▓▓▓▓  E̸R̶R̶O̴R̷  ▓▓▓▓▒▒▒░░░░██      ║");
        System.out.println("    ║                                                 ║");
        System.out.println("    ║         「 Something is wrong here... 」        ║");
        System.out.println("    ║                                                 ║");
        System.out.println("    ║    The world around you f̶r̶a̶c̶t̶u̶r̶e̶s̶               ║");
        System.out.println("    ║    Your memories s̶h̶a̶t̶t̶e̶r̶                        ║");
        System.out.println("    ║    But you can still... c̶h̶o̶o̶s̶e̶?                 ║");
        System.out.println("    ║                                                 ║");
        System.out.println("    ═══════════════════════════════════════════════════");
        System.out.println("    ║                                                 ║");
        System.out.println("    ║    ▸ 1  —  E̸N̷T̴E̷R̴ ̶T̵H̷E̴ ̷S̸I̵M̷U̸L̷A̵T̴I̷O̴N̷                 ║");
        System.out.println("    ║    ▸ 2  —  R̶E̷F̸U̴S̶E̷ ̴/̷ ̵E̶S̴C̴A̷P̸E̴                      ║");
        System.out.println("    ║                                                 ║");
        System.out.println("    ║         [devmenu = access debug console]        ║");
        System.out.println("    ║                                                 ║");
        System.out.println("    ═══════════════════════════════════════════════════");
        System.out.println();
        System.out.print(" > Enter your choice: ");
    }

    public static void displayClassSelection() {
        String selection = 
        "\n╔══════════════════════════════════════════════════════════════════════════╗\n" +
        "║                                                                          ║\n" +
        "║                  🎭  C H O O S E   Y O U R   C L A S S  🎭               ║\n" +
        "║                                                                          ║\n" +
        "╠══════════════════════════════════════════════════════════════════════════╣\n" +
        "║                                                                          ║\n" +
        "║  [1] 🧪 SCIENTIST                                                        ║\n" +
        "║      ┌────────────────────────────────────────────────────────────┐      ║\n" +
        "║      │ Bonuses: +Intelligence, +Defense                           │      ║\n" +
        "║      │                                                            │      ║\n" +
        "║      │ Skills:                                                    │      ║\n" +
        "║      │  • Chemical Strike (Basic)    - Corrosive damage           │      ║\n" +
        "║      │  • Plasma Field (Secondary)   - Area charged attack        │      ║\n" +
        "║      │  • Nuclear Blast (Ultimate)   - Massive explosion          │      ║\n" +
        "║      └────────────────────────────────────────────────────────────┘      ║\n" +
        "║                                                                          ║\n" +
        "║  [2] ⚔️  FIGHTER                                                          ║\n" +
        "║      ┌────────────────────────────────────────────────────────────┐      ║\n" +
        "║      │ Bonuses: +HP, +Defense                                     │      ║\n" +
        "║      │                                                            │      ║\n" +
        "║      │ Skills:                                                    │      ║\n" +
        "║      │  • Pacman Punch (Basic)       - Just like a shotgun        │      ║\n" +
        "║      │  • Deadly Roar (Secondary)    - Roar so deadly             │      ║\n" +
        "║      │  • Earth Shaker (Ultimate)    - Ground-slam shockwave      │      ║\n" +
        "║      └────────────────────────────────────────────────────────────┘      ║\n" +
        "║                                                                          ║\n" +
        "║  [3] 🔮 ARCHMAGE                                                         ║\n" +
        "║      ┌────────────────────────────────────────────────────────────┐      ║\n" +
        "║      │ Bonuses: +Intelligence, +Mana                              │      ║\n" +
        "║      │                                                            │      ║\n" +
        "║      │ Skills:                                                    │      ║\n" +
        "║      │  • Fire Bolt (Basic)          - Blazing fire attack        │      ║\n" +
        "║      │  • Arcane Pulse (Secondary)   - Pulse of the arcane        │      ║\n" +
        "║      │  • Meteor Storm (Ultimate)    - Cataclysmic meteor rain    │      ║\n" +
        "║      └────────────────────────────────────────────────────────────┘      ║\n" +
        "║                                                                          ║\n" +
        "╠══════════════════════════════════════════════════════════════════════════╣\n" +
        "║  💡 Note: Skills unlock at Level 2                                       ║\n" +
        "╚══════════════════════════════════════════════════════════════════════════╝\n" +
        "\n> Enter your choice (1-3): ";
        
        System.out.print(selection);
    }

    // ✅ FIXED: Now uses the same wrapping logic as UIDesign
    public static void displayTutorialSafeZone() {
        String zoneName = "School Rooftop";
        String zoneDescription = "The wind is calm here. Vines creep across shattered tiles.";

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.printf("║                  🏠 %-35s  ║%n", zoneName.toUpperCase());
        System.out.println("║              (Zone 1 Safe Haven)                         ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");

        // ✅ USE THE SAME WRAPPING METHOD AS UIDesign
        wrapAndPrint(zoneDescription, 54);

        System.out.println("║                                                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Available Actions:                                      ║");
        System.out.println("║                                                          ║");
        System.out.println("║  [craft]     ⚒️  Crafting Bench                           ║");
        System.out.println("║  [search]    🔍 Search Area                              ║");
        System.out.println("║  [status]    📊 View Status                              ║");
        System.out.println("║  [bag]       🎒 Open Bag                                 ║");
        System.out.println("║  [move]      🚪 Leave Safe Zone                          ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    // ✅ ADDED: Copy of the wrapping method from UIDesign to ensure consistency
    private static void wrapAndPrint(String text, int maxWidth) {
        if (text == null || text.isEmpty()) return;
        
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder("║  ");
        
        for (String word : words) {
            if (line.length() + word.length() + 1 > maxWidth + 3) {
                while (line.length() < 59) line.append(" ");
                line.append("║");
                System.out.println(line);
                line = new StringBuilder("║  " + word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        
        if (line.length() > 3) {
            while (line.length() < 59) line.append(" ");
            line.append("║");
            System.out.println(line);
        }
    }

    // Tutorial combat UI helper method
    private static String createBar(int current, int max, int length, char filled, char empty) {
        int filledLength = (int) ((double) current / max * length);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            bar.append(i < filledLength ? filled : empty);
        }
        return bar.toString();
    }

    // Tutorial combat display
    public static void displayTutorialCombat(Player player, Enemy enemy) {
        String pHpBar = createBar(player.getHp(), player.getMaxHp(), 20, '█', '░');
        String eMpBar = createBar(enemy.getHp(), 20, 20, '█', '░');
        
        String combat = 
        "\n╔═══════════════════════════════════════════════════════════════════════╗\n" +
        "║                                                                       ║\n" +
        "║                      ⚔️  T U T O R I A L   C O M B A T  ⚔️            ║\n" +
        "║                                                                       ║\n" +
        "╠═══════════════════════════════════════════════════════════════════════╣\n" +
        "║                                                                       ║\n" +
        String.format("║  👤 %-20s                                           ║\n", player.getName()) +
        String.format("║     ❤️  HP:   %s  %3d/%-3d                         ║\n", 
            pHpBar, player.getHp(), player.getMaxHp()) +
        String.format("║     🔮 MANA:  [%-20s]  %3d/%-3d                         ║\n",
            createBar(player.getMana(), player.getMaxMana(), 20, '▓', '░'),
            player.getMana(), player.getMaxMana()) +
        "║                                                                       ║\n" +
        "║                            ⚔️  VS  ⚔️                                 ║\n" +
        "║                                                                       ║\n" +
        String.format("║  👹 %-20s                                           ║\n", enemy.getName()) +
        String.format("║     💀 HP:   %s  %3d HP                             ║\n", 
            eMpBar, enemy.getHp()) +
        "║                                                                       ║\n" +
        "╠═══════════════════════════════════════════════════════════════════════╣\n" +
        "║                                                                       ║\n" +
        "║  💡 Tutorial Tips:                                                    ║\n" +
        "║     • ATTACK deals weapon damage                                     ║\n" +
        "║     • DEFEND reduces incoming damage by 50%                          ║\n" +
        "║     • ITEM lets you use consumables (Meat heals 10 HP)               ║\n" +
        "║     • Skills unlock at Level 2                                       ║\n" +
        "║                                                                       ║\n" +
        "╠═══════════════════════════════════════════════════════════════════════╣\n" +
        "║                                                                       ║\n" +
        "║  [1] ⚔️  ATTACK      [2] 🛡️  DEFEND      [3] 🎒 ITEM                 ║\n" +
        "║  [4] 🏃 RUN                                                           ║\n" +
        "║                                                                       ║\n" +
        "╚═══════════════════════════════════════════════════════════════════════╝\n" +
        "\n> Your action: ";
        
        System.out.print(combat);
    }
}