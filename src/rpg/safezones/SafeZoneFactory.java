package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;

public class SafeZoneFactory {
    public static SafeZone getZone(int zone) {
        switch (zone) {
            case 1: return new RooftopSafeZone();
            case 2: return new RuinedLabSafeZone();
            // future zones: case 3 -> new CitySafeZone();
            default: return (player, state, scanner) -> {
                player.healFull();
                state.inSafeZone = true;
                System.out.println("You rest safely here. Your stats are restored.");
            };
        }
    }
}
