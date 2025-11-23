package rpg.game;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import rpg.characters.Enemy;
import rpg.characters.Supporter;

public class GameState {
    public boolean fastMode = false;
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
    public boolean stage3WeaponCrafted = false;
    public boolean stage4WeaponCrafted = false;

    // ✅ Track unlocked blueprints
    public Set<String> unlockedRecipes = new HashSet<>();

    // ✅ Track obtained recipe items
    public Map<String, Boolean> recipeItems = new HashMap<>();

    // ✅ Shop unlock flag
    public boolean shopUnlocked = false;

    public boolean zone2IntroShown = false;
    // Track whether the player has seen the stage-2 supporter menu intro
    public boolean supporterMenuIntroStage2Shown = false;
    public boolean skillsUnlocked = false;

    public boolean lootDisplayed = false; // ✅ already added, prevents duplicate loot text
    public boolean metSirKhai = false;
    public List<Supporter> supporters = new ArrayList<>();

    public boolean bossGateDiscovered = false;
    public boolean zone3IntroShown = false;


}
