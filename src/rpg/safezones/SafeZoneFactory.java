package rpg.safezones;

//----> Remove Scanner
import rpg.characters.Player;
import rpg.game.GameState;

public class SafeZoneFactory {
    public static SafeZone getZone(int zone) {
        switch (zone) {
            case 1: return new RooftopSafeZone();
            case 2: return new RuinedLabSafeZone();
            case 3: return new CityRuinsSafeZone();
            case 4: return new ObservationDeckSafeZone();
            // ✅ UPDATED: Added specific file for Zone 5
            case 5: return new TheSourceSafeZone();
            
            default: return (player, state, scanner) -> {
                player.healFull();
                state.inSafeZone = true;
                System.out.println("[System] > Resting in Unknown Sector. Stats restored.");
            };
        }
    }
}