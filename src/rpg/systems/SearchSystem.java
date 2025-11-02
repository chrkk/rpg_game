package rpg.systems;

import java.util.Random;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class SearchSystem {
    private static Random rand = new Random();

    public static void search(GameState state) {
        int roll = rand.nextInt(100);

        if (roll < 40) {
            state.shards++;
            TextEffect.typeWriter("You scavenge the area and find a Shard!", 50);

        } else if (roll < 70) {
            state.meat++;
            TextEffect.typeWriter("You hunt around and find some Meat.", 50);

        } else if (roll < 90) {
            TextEffect.typeWriter("You discover a small vial of medicine — it might help later.", 50);

        } else {
            TextEffect.typeWriter("You search the area but find nothing of value.", 50);
        }

        // ✅ Chance to find recipe item if blueprint unlocked
        if (state.unlockedRecipes.contains("Crystal Sword") && !state.hasCrystalSwordRecipeItem) {
            if (rand.nextInt(100) < 10) { // 10% chance from searching
                state.hasCrystalSwordRecipeItem = true;
                TextEffect.typeWriter("✨ While searching, you uncover the Crystal Sword Recipe!", 60);
            }
        }
    }
}
