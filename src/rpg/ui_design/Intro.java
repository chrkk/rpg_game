package rpg.ui_design;

import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.game.GameState;
import rpg.utils.TextEffect;
import rpg.ui.UIDesign;

public class Intro {


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

//tutorial ui
public static void displayTutorialSafeZone() {
    // Render a safe-zone hub that mirrors the global design but omits the supporter option
    String zoneName = "School Rooftop";
    String zoneDescription = "The wind is calm here. Vines creep across shattered tiles.";

    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
    System.out.printf("║                  🏠 %-35s  ║%n", zoneName.toUpperCase());
    System.out.println("║              (Zone 1 Safe Haven)                         ║");
    System.out.println("╠══════════════════════════════════════════════════════════╣");
    System.out.println("║                                                          ║");

    // Simple wrap (short description)
    System.out.printf("║  %-54s ║%n", zoneDescription);

    System.out.println("║                                                          ║");
    System.out.println("╠══════════════════════════════════════════════════════════╣");
    System.out.println("║  Available Actions:                                      ║");
    System.out.println("║                                                          ║");
    System.out.println("║  [craft]     ⚒️  Crafting Bench                           ║");
    System.out.println("║  [search]    🔍 Search Area                              ║");
    // supporter option intentionally omitted for tutorial / rooftop
    System.out.println("║  [status]    📊 View Status                              ║");
    System.out.println("║  [bag]       🎒 Open Bag                                 ║");
    System.out.println("║  [move]      🚪 Leave Safe Zone                          ║");
    System.out.println("║                                                          ║");
    System.out.println("╚══════════════════════════════════════════════════════════╝");
}

private String createBar(int current, int max, int length, char filled, char empty) {
    int filledLength = (int) ((double) current / max * length);
    StringBuilder bar = new StringBuilder();
    for (int i = 0; i < length; i++) {
        bar.append(i < filledLength ? filled : empty);
    }
    return bar.toString();
}

//tutorial combat ui
private void displayTutorialCombat(Player player, Enemy enemy) {
    String pHpBar = createBar(player.getHp(), player.getMaxHp(), 20, '█', '░');
    String eMpBar = createBar(enemy.getHp(), 20, 20, '█', '░'); // Assume 20 max for tutorial
    
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