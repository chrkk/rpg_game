package rpg.systems;

import rpg.characters.Player;
import rpg.items.Weapon;
import rpg.utils.TextEffect;

public class CraftingSystem {
public static int craftWeapon(Player player, int crystals, GameState state) {
    if (player.getWeapon() != null && player.getWeapon().getName().equals("Pencil Blade") && crystals >= 3) {
        crystals -= 3;
        player.equipWeapon(new Weapon("Crystal Dagger", 20));
        state.stage1WeaponCrafted = true; // ✅ mark stage 1 weapon crafted
        TextEffect.typeWriter("You forged a Crystal Dagger! Stronger than your Pencil Blade.", 60);
    } else if (player.getWeapon() != null && player.getWeapon().getName().equals("Crystal Dagger") && crystals >= 5) {
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

