package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class BossGateSystem {

    public static boolean canFightBoss(GameState state, Player player, Runnable safeZoneAction) {
        // Stage 2 requirement (Crystal Sword)
        if (state.zone == 2 && !state.stage2WeaponCrafted) {
            TextEffect.typeWriter("The path is blocked by a powerful aura. Only the Crystal Sword can pierce it.", 70);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }

        // ✅ Add more stages as needed
        return true;
    }
}
