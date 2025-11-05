package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class RooftopSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;
        TextEffect.typeWriter("You rest on the School Rooftop. The wind is calm, and your stats are restored.", 60);
    }
}
