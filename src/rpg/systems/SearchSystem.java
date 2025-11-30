package rpg.systems;

import java.util.Random;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.world.SupporterPool;

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
            state.mediumPotions++;
            TextEffect.typeWriter("You discover a Potion (Medium)! It should restore a good chunk of HP.", 50);
        } else {
            TextEffect.typeWriter("You search the area but find nothing of value.", 50);
        }

        // ✅ Chance to find recipe item if blueprint unlocked
        for (String recipe : state.unlockedRecipes) {
            if (!state.recipeItems.getOrDefault(recipe, false)) {
                if (rand.nextInt(100) < 10) { // 10% chance
                    state.recipeItems.put(recipe, true);
                    TextEffect.typeWriter("✨ While searching, you uncover the " + recipe + " Recipe!", 60);
                }
            }
        }

        // Small chance to stumble on a statue while searching (only meaningful outside zone 1)
        try {
            if (state.zone > 1 && rand.nextInt(100) < 12) { // 12% chance (increased)
                rpg.characters.Supporter statue = SupporterPool.getRandomUnrevivedSupporter(state.zone, rand, state);
                if (statue != null) {
                    TextEffect.typeWriter("🗿 While searching, you notice a strange statue nearby...", 60);
                    java.util.Scanner scanner = new java.util.Scanner(System.in);
                    ReviveSystem.randomRevive(state, statue, scanner);
                }
            }
        } catch (Exception e) {
            System.err.println("SearchSystem.statueCheck error -> " + e.getMessage());
        }
    }
}
