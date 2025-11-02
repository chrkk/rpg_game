package rpg.systems;

import java.util.Random;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class LootSystem {
    private static Random rand = new Random();

    public static void dropLoot(GameState state) {
        int crystalDrop = (rand.nextInt(100) < 10) ? 1 : 0; // 10% chance
        int meatDrop = rand.nextInt(2);                     // 0–1
        int shardDrop = 1 + rand.nextInt(3);                // 1–3

        state.crystals += crystalDrop;
        state.meat += meatDrop;
        state.shards += shardDrop;

        StringBuilder lootMsg = new StringBuilder("Loot: ");
        lootMsg.append("+" + shardDrop + " Shards");
        if (crystalDrop > 0) lootMsg.append(", +" + crystalDrop + " Crystal");
        if (meatDrop > 0) lootMsg.append(", +" + meatDrop + " Meat");

        // ✅ Check for recipe item drop (only if blueprint unlocked)
        if (state.unlockedRecipes.contains("Crystal Sword") && !state.hasCrystalSwordRecipeItem) {
            if (rand.nextInt(100) < 15) { // 15% chance from loot
                state.hasCrystalSwordRecipeItem = true;
                lootMsg.append(", ✨ Crystal Sword Recipe");
            }
        }

        TextEffect.typeWriter(lootMsg.toString(), 50);
    }
}
