package rpg.game;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
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

    // ✅ Track crafted stage weapons (consistent naming)
    public boolean stage1WeaponCrafted = false;
    public boolean stage2WeaponCrafted = false;
    public boolean stage3WeaponCrafted = false; // future-proofing

    // ✅ Track unlocked blueprints
    public Set<String> unlockedRecipes = new HashSet<>();

    // ✅ Track obtained recipe items
    public Map<String, Boolean> recipeItems = new HashMap<>();

    // ✅ Shop unlock flag
    public boolean shopUnlocked = false;

    public boolean zone2IntroShown = false;
    public boolean skillsUnlocked = false;

    public boolean lootDisplayed = false;   // ✅ already added, prevents duplicate loot text

    public boolean metSirKhai = false;

}
