package rpg.game;

import java.util.HashSet;
import java.util.Set;
import rpg.characters.Enemy;

public class GameState {
    public int zone = 1;
    public int forwardSteps = 0;
    public int crystals = 0;
    public int shards = 0;         
    public int meat = 0;
    public int revivalPotions = 0;
    public Enemy currentZoneBoss; 
    public boolean inSafeZone = false;

    // ✅ Track crafted stage weapons
    public boolean hasStageWeapon1 = false;
    public boolean hasStageWeapon2 = false;\


    // ✅ Track unlocked blueprints
    public Set<String> unlockedRecipes = new HashSet<>();

    public boolean hasCrystalSwordRecipeItem = false; // obtained via drop/search

}
