package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class TheSourceSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;

        TextEffect.typeWriter("\n[System] > Entering Sector: THE_SOURCE_ROOT.", 60);
        TextEffect.typeWriter("You stand at the edge of the simulation. The digital sky dissolves into pure white code.", 60);
        TextEffect.typeWriter("The hum of servers and the smell of ozone fill the air.", 60);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        TextEffect.typeWriter("\n[System] > Vital Signs Stabilized. HP and Mana Fully Restored.", 60);
        TextEffect.typeWriter("[System] > Warning: No merchants detected in this sector.", 60);
        TextEffect.typeWriter("There is no turning back now. Only the final decision remains.", 70);
    }
}