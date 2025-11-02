package rpg.systems;

import rpg.characters.Player;
import rpg.items.Weapon;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class CraftingSystem {
    public static int craftWeapon(Player player, int crystals, GameState state) {
        // Upgrade Pencil Blade → Crystal Sword (requires blueprint)
        if (player.getWeapon() != null
                && player.getWeapon().getName().equals("Pencil Blade")
                && crystals >= 5
                && state.unlockedRecipes.contains("Crystal Sword")
                && state.hasCrystalSwordRecipeItem) {

            crystals -= 5;
            player.equipWeapon(new Weapon("Crystal Sword", 35));
            state.hasStageWeapon2 = true;
            TextEffect.typeWriter("You forged a Crystal Sword! Its edge gleams with power.", 60);

        } else if (state.unlockedRecipes.contains("Crystal Sword") && !state.hasCrystalSwordRecipeItem) {
            TextEffect.typeWriter("You know the blueprint, but you haven’t found the recipe item yet.", 60);
        } else {
            TextEffect.typeWriter("You don’t have the requirements to craft this weapon.", 60);
        }

        return crystals;
    }
}
