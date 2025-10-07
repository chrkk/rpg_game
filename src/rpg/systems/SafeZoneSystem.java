package rpg.systems;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class SafeZoneSystem {
    public static void enterSafeZone(Player player, GameState state) {
        player.healFull();
        TextEffect.typeWriter("You feel renewed as you enter Safe Zone " + state.zone + ".", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);
    }
}
