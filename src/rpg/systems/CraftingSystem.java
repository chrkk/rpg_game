package rpg.systems;

import rpg.characters.Player;
import rpg.items.Weapon;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class CraftingSystem {
    public static int craftWeapon(Player player, int crystals, GameState state) {
        // Upgrade Pencil Blade → Crystal Sword
        if (player.getWeapon() != null && player.getWeapon().getName().equals("Pencil Blade") && crystals >= 5) {
            crystals -= 5;
            player.equipWeapon(new Weapon("Crystal Sword", 35));
            state.stage2WeaponCrafted = true; // ✅ mark stage 2 weapon crafted
            TextEffect.typeWriter("You forged a Crystal Sword! Its edge gleams with power.", 60);
        } else {
            TextEffect.typeWriter("You don’t have enough crystals or the right base weapon.", 60);
        }
        return crystals;
    }
}
