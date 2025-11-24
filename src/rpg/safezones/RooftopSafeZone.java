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
        TextEffect.typeWriter("[System] > Checkpoint Reached: School Rooftop.", 60);
        TextEffect.typeWriter("The wind is calm here. Simulation parameters stabilized. HP/Mana restored.", 60);
    }
}