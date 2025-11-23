package rpg.systems;

import rpg.characters.Player;
import rpg.items.Weapon;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class CraftingSystem {
    public static int craftWeapon(Player player, int crystals, GameState state, String target) {
        // Check if recipe is unlocked and discovered
        if (state.unlockedRecipes.contains(target) && state.recipeItems.getOrDefault(target, false)) {
            // Example requirements per weapon
            switch (target) {
                case "Crystal Sword":
                    if (crystals >= 5) {
                        crystals -= 5;
                        player.equipWeapon(new Weapon("Crystal Sword", 20, 35, 0.10, 2.0));
                        state.stage2WeaponCrafted = true;
                        TextEffect.typeWriter("⚔️ You forged a Crystal Sword! Its edge gleams with power.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 5, Have: " + crystals + ")", 60);
                    }
                    break;

                case "Flame Axe":
                    if (crystals >= 8) {
                        crystals -= 8;
                        player.equipWeapon(new Weapon("Flame Axe", 25, 40, 0.15, 1.8));
                        state.stage3WeaponCrafted = true;
                        TextEffect.typeWriter("🔥 You forged a Flame Axe! It radiates intense heat.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 8, Have: " + crystals + ")", 60);
                    }
                    break;

                case "Shadow Bow":
                    if (crystals >= 10) {
                        crystals -= 10;
                        player.equipWeapon(new Weapon("Shadow Bow", 15, 30, 0.20, 2.5));
                        state.stage4WeaponCrafted = true;
                        TextEffect.typeWriter("🌑 You forged a Shadow Bow! Shadows bend to your will.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 10, Have: " + crystals + ")", 60);
                    }
                    break;

                default:
                    TextEffect.typeWriter("This recipe isn't implemented yet.", 60);
            }
        } else if (state.unlockedRecipes.contains(target) && !state.recipeItems.getOrDefault(target, false)) {
            TextEffect.typeWriter("You know the blueprint, but you haven't found the recipe item yet.", 60);
        } else {
            TextEffect.typeWriter("You don't have the requirements to craft this weapon.", 60);
        }
        return crystals;
    }
}
