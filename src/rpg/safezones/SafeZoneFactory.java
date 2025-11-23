package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;

public class SafeZoneFactory {
    public static SafeZone getZone(int zone) {
        switch (zone) {
            case 1: return new RooftopSafeZone();
            case 2: return new RuinedLabSafeZone();
            case 3: return new CityRuinsSafeZone();
            case 4: return new ObservationDeckSafeZone();
            default: return (player, state, scanner) -> {
                player.healFull();
                state.inSafeZone = true;
                System.out.println("You rest safely here. Your stats are restored.");
            };
        }
    }
}
