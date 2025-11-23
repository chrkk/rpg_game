package rpg.systems;

import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class BossGateSystem {

    public static boolean canFightBoss(GameState state, Player player, Runnable safeZoneAction) {
        // Zone 2 requirement: Crystal Sword
        if (state.zone == 2 && !state.stage2WeaponCrafted) {
            TextEffect.typeWriter("⛔ The path is blocked by a powerful aura. Only the Crystal Sword can pierce it.", 70);
            TextEffect.typeWriter("💡 Hint: Buy the Crystal Sword Blueprint from the shop and craft it.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }
        
        // Zone 3 requirement: Flame Axe
        if (state.zone == 3 && !state.stage3WeaponCrafted) {
            TextEffect.typeWriter("⛔ A wall of flames blocks your path. Only the Flame Axe can extinguish it.", 70);
            TextEffect.typeWriter("💡 Hint: The blueprint should be available in the shop now.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }
        
        // Zone 4 requirement: Thunder Spear
        if (state.zone == 4 && !state.stage4WeaponCrafted) {
            TextEffect.typeWriter("⛔ Stormlight lashes across the gate. Only the Thunder Spear can tether the lightning and pierce it.", 70);
            TextEffect.typeWriter("💡 Hint: Buy the Thunder Spear blueprint from Kuro (25 shards) and forge it with 12 crystals.", 60);
            state.inSafeZone = true;
            safeZoneAction.run();
            return false;
        }

        return true;
    }
}