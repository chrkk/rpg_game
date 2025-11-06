package rpg.systems;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;
import rpg.safezones.SafeZone;
import rpg.safezones.SafeZoneFactory;
import rpg.utils.TextEffect;

public class SafeZoneSystem {
    public static void enterSafeZone(Player player, GameState state, Scanner scanner) {
        // 🆕 Guard clause: prevent re-entering
        if (state.inSafeZone) {
            TextEffect.typeWriter("You are already in the safe zone.", 60);
            return;
        }

        // Normal entry logic
        SafeZone zone = SafeZoneFactory.getZone(state.zone);
        state.inSafeZone = true; // mark as inside
        zone.enter(player, state, scanner);
    }

    public static void searchSafeZone(Player player, GameState state) {
        try {
            TextEffect.typeWriter("You cannot search while in a Safe Zone. Try moving out first.", 60);
        } catch (Exception e) {
            TextEffect.typeWriter("You stumble while trying to search here.", 40);
            System.err.println("Safe zone search error -> " + e.getMessage());
        }
    }
}
