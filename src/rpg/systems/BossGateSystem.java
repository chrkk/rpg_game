package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class BossGateSystem {

    public static boolean canFightBoss(GameState state, Player player, Runnable safeZoneAction) {
        // Zone 2 requirement: Logic Blade
        if (state.zone == 2 && !state.stage2WeaponCrafted) {
            TextEffect.typeWriter("⛔ [System] > Access Denied. The path is blocked by a powerful aura.", 70);
            TextEffect.typeWriter("💡 Hint: Only the Logic Blade can pierce it. Buy the blueprint and craft it.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }
        
        // Zone 3 requirement: Aftershock Hammer
        if (state.zone == 3 && !state.stage3WeaponCrafted) {
            TextEffect.typeWriter("⛔ [System] > Obstacle Detected: Massive Debris Field.", 70);
            TextEffect.typeWriter("The path is blocked by concrete fused with rebar.", 70);
            TextEffect.typeWriter("💡 Hint: Extreme heat required to melt the structure. Craft the Aftershock Hammer.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }
        
        // Zone 4 requirement: Trident of Storms (Updated)
        if (state.zone == 4 && !state.stage4WeaponCrafted) {
            TextEffect.typeWriter("⛔ [System] > Warning: Cyclonic Barrier Detected.", 70);
            TextEffect.typeWriter("Wind speeds exceed 200 kph. Standard entry impossible.", 70);
            TextEffect.typeWriter("💡 Hint: You need a weapon to pierce the eye of the storm. Craft the Trident of Storms.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }

        // Zone 5: The Choice (Final Dilemma)
        if (state.zone == 5) {
            return true;
        }

        return true;
    }
}