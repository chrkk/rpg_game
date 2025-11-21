package rpg.systems;

import java.util.Random;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class LootSystem {
    private static Random rand = new Random();

    public static void dropLoot(GameState state) {
        if (state.lootDisplayed) {
            return; // ✅ already shown, skip
        }
        state.lootDisplayed = true;

        int crystalDrop = (rand.nextInt(100) < 50) ? 1 : 0;
        int meatDrop = rand.nextInt(2);
        int shardDrop = 1 + rand.nextInt(3);

        state.crystals += crystalDrop;
        state.meat += meatDrop;
        state.shards += shardDrop;

        TextEffect.typeWriter("\n📦 Loot acquired:", 50);
        TextEffect.typeWriter("  + " + shardDrop + " Shards", 50);

        if (crystalDrop > 0) {
            TextEffect.typeWriter("  + " + crystalDrop + " Crystal", 50);
        }
        if (meatDrop > 0) {
            TextEffect.typeWriter("  + " + meatDrop + " Meat", 50);
        }

        for (String recipe : state.unlockedRecipes) {
            if (!state.recipeItems.getOrDefault(recipe, false)) {
                if (rand.nextInt(100) < 15) {
                    state.recipeItems.put(recipe, true);
                    TextEffect.typeWriter("  ✨ " + recipe + " Recipe", 50);
                }
            }
        }
    }

}
