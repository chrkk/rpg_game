package rpg.systems;

import java.util.Random;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class LootSystem {
    private static Random rand = new Random();

    public static void dropLoot(GameState state) {
        int crystalDrop = (rand.nextInt(100) < 10) ? 1 : 0;
        int meatDrop = rand.nextInt(2);
        int shardDrop = 1 + rand.nextInt(3);

        state.crystals += crystalDrop;
        state.meat += meatDrop;
        state.shards += shardDrop;

        TextEffect.typeWriter("📦 Loot acquired:", 50);

        // Always show shards
        TextEffect.typeWriter("  + " + shardDrop + " Shards", 50);

        // Show crystals only if dropped
        if (crystalDrop > 0) {
            TextEffect.typeWriter("  + " + crystalDrop + " Crystal", 50);
        }

        // Show meat only if dropped
        if (meatDrop > 0) {
            TextEffect.typeWriter("  + " + meatDrop + " Meat", 50);
        }

        // ✅ Check for recipe item drop
        for (String recipe : state.unlockedRecipes) {
            if (!state.recipeItems.getOrDefault(recipe, false)) {
                if (rand.nextInt(100) < 15) { // 15% chance
                    state.recipeItems.put(recipe, true);
                    TextEffect.typeWriter("  ✨ " + recipe + " Recipe", 50);
                }
            }
        }
    }
}
