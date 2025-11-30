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
                // Zone 2: Logic Blade
                case "Logic Blade":
                    if (state.stage2WeaponCrafted) {
                        TextEffect.typeWriter("You've already crafted the Logic Blade!", 60);
                    } else if (crystals >= 5) {
                        crystals -= 5;
                        player.equipWeapon(new Weapon("Logic Blade", 20, 35, 0.10, 2.0));
                        state.stage2WeaponCrafted = true;
                        TextEffect.typeWriter("⚔️ [System] > Logic Blade compiled successfully. Cutting edge: Sharp.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 5, Have: " + crystals + ")", 60);
                    }
                    break;

                // Zone 3: Aftershock Hammer
                case "Aftershock Hammer":
                    if (state.stage3WeaponCrafted) {
                        TextEffect.typeWriter("You've already crafted the Aftershock Hammer!", 60);
                    } else if (crystals >= 8) {
                        crystals -= 8;
                        player.equipWeapon(new Weapon("Aftershock Hammer", 25, 40, 0.15, 1.8));
                        state.stage3WeaponCrafted = true;
                        TextEffect.typeWriter("🔨 [System] > Aftershock Hammer forged. Heavy impact detected.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 8, Have: " + crystals + ")", 60);
                    }
                    break;

                // ✅ UPDATED: Trident of Storms (Zone 4)
                case "Trident of Storms":
                    if (state.stage4WeaponCrafted) {
                        TextEffect.typeWriter("You've already crafted the Trident of Storms!", 60);
                    } else if (!state.stage3WeaponCrafted) {
                        TextEffect.typeWriter("The forge rejects your attempt — the Trident needs the Aftershock Hammer as its base.", 60);
                    } else if (crystals >= 12) {
                        crystals -= 12;
                        player.equipWeapon(new Weapon("Trident of Storms", 30, 50, 0.25, 2.2));
                        state.stage4WeaponCrafted = true;
                        TextEffect.typeWriter("⚡ [System] > Trident of Storms stabilized. Power output: Critical.", 60);
                    } else {
                        TextEffect.typeWriter("You don't have enough crystals. (Need: 12, Have: " + crystals + ")", 60);
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