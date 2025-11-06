package rpg.characters;

public class Supporter {
    private String name;
    private String trait;
    private boolean revived;     // whether this supporter has been revived
    private String ability;      // unique ability or buff they provide
    private int hp;              // optional: health if they can fight
    private int power;           // optional: support strength (buff/heal amount)

    public Supporter(String name, String trait, String ability) {
        this.name = name;
        this.trait = trait;
        this.ability = ability;
        this.revived = false;
        this.hp = 100;   // default values, can be tuned
        this.power = 10;
    }

    // --- Getters ---
    public String getName() { return name; }
    public String getTrait() { return trait; }
    public String getAbility() { return ability; }
    public boolean isRevived() { return revived; }
    public int getHp() { return hp; }
    public int getPower() { return power; }

    // --- Setters / State changes ---
    public void setRevived(boolean revived) {
        this.revived = revived;
    }

    public void healPlayer(rpg.characters.Player player) {
        if (revived) {
            player.heal(power);
            System.out.println(name + " uses " + ability + " to heal you for " + power + " HP!");
        }
    }

    @Override
    public String toString() {
        return (revived ? "✅ " : "❌ ") + name + " the " + trait + " (" + ability + ")";
    }
}
