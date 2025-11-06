package rpg.systems;

import rpg.utils.TextEffect;
import rpg.game.GameState;

import java.util.Scanner;

import rpg.characters.Supporter;

public class ReviveSystem {

    // Scripted revival (e.g., Sir Khai after Stage 1 miniboss)
    public static void scriptedRevive(GameState state, Supporter sirKhai, Scanner scanner) {
        TextEffect.typeWriter("\n🗿 A statue stands before you... Sir Khai, frozen in stone.", 60);
        TextEffect.typeWriter("A glowing aura surrounds him. Do you wish to revive? (yes/no)", 60);

        System.out.print("> ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            if (state.revivalPotions > 0) {
                state.revivalPotions--;
                sirKhai.setRevived(true);
                state.supporters.add(sirKhai);
                state.metSirKhai = true;
                TextEffect.typeWriter("✨ Sir Khai awakens, joining you as your first supporter!", 60);
            } else {
                TextEffect.typeWriter("❌ You lack a Revival Potion. The statue remains silent.", 60);
            }
        } else {
            // Tutorial enforcement: must revive at least one supporter
            TextEffect.typeWriter("❌ You cannot proceed without reviving a supporter.", 60);
            scriptedRevive(state, sirKhai, scanner); // re‑prompt until valid
        }
    }

    // Random statue revival outside safe zones
    public static void randomRevive(GameState state, Supporter supporter) {
        if (state.revivalPotions > 0) {
            state.revivalPotions--;
            supporter.setRevived(true);
            TextEffect.typeWriter("✨ You used a Revival Potion to awaken " + supporter.getName() + "!", 60);
            state.supporters.add(supporter); // track revived allies
        } else {
            TextEffect.typeWriter("You sense a presence... but you lack a Revival Potion.", 60);
        }
    }
}
