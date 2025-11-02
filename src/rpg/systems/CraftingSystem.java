package rpg.systems;

import rpg.characters.Player;
import rpg.items.Weapon;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class CraftingSystem {
    public static int craftWeapon(Player player, int crystals, GameState state) {
        // Example: Pencil Blade → Crystal Sword
        String target = "Crystal Sword";

        if (player.getWeapon() != null 
            && player.getWeapon().getName().equals("Pencil Blade") 
            && crystals >= 5 
            && state.unlockedRecipes.contains(target) 
            && state.recipeItems.getOrDefault(target, false)) {

            crystals -= 5;
            player.equipWeapon(new Weapon("Crystal Sword", 20, 35, 0.10, 2.0));
            state.stage2WeaponCrafted = true;
            TextEffect.typeWriter("You forged a Crystal Sword! Its edge gleams with power.", 60);

        } else if (state.unlockedRecipes.contains(target) && !state.recipeItems.getOrDefault(target, false)) {
            TextEffect.typeWriter("You know the blueprint, but you haven’t found the recipe item yet.", 60);
        } else {
            TextEffect.typeWriter("You don’t have the requirements to craft this weapon.", 60);
        }
        return crystals;
    }
}
