package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class SafeZoneSystem {
    public static void enterSafeZone(Player player, GameState state) {
        player.healFull();
        TextEffect.typeWriter("You feel renewed as you enter Safe Zone " + state.zone + ".", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);

        // Continue exploration
        ExplorationSystem.explorationLoop(player, new java.util.Scanner(System.in), new java.util.Random(), state);
    }
}