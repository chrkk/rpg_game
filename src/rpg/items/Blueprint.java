package rpg.items;

public class Blueprint {
    private String name;
    private String unlocksRecipe;
    private int price;

    public Blueprint(String name, String unlocksRecipe, int price) {
        this.name = name;
        this.unlocksRecipe = unlocksRecipe;
        this.price = price;
    }

    public String getName() { return name; }
    public String getUnlocksRecipe() { return unlocksRecipe; }
    public int getPrice() { return price; }

    @Override
    public String toString() {
        return "Blueprint: " + name + " (Unlocks: " + unlocksRecipe + ", Price: " + price + " Shards)";
    }
}
