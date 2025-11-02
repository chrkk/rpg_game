package rpg.items;

public class Blueprint {
    private String name;
    private String unlocksRecipe;

    public Blueprint(String name, String unlocksRecipe) {
        this.name = name;
        this.unlocksRecipe = unlocksRecipe;
    }

    public String getName() { return name; }
    public String getUnlocksRecipe() { return unlocksRecipe; }

    @Override
    public String toString() {
        return "Blueprint: " + name + " (Unlocks: " + unlocksRecipe + ")";
    }
}
