package rpg.systems;

import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.characters.Supporter;

public class ReviveSystem {

    // Scripted revival (e.g., Sir Khai after Stage 1 miniboss)
    public static void scriptedRevive(GameState state, String npcName) {
        TextEffect.typeWriter("\n👨‍🏫 [" + npcName + "] You fought bravely... I’ve been waiting for you here.", 60);
        TextEffect.typeWriter("Objective Updated: Speak with " + npcName + " to learn your next path.", 60);
        state.metSirKhai = true; // flag in GameState
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
