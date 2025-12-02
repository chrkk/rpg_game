package rpg.characters;

public class Supporter {
    private final String name;
    private final String ability;   // unique ability or buff they provide
    private boolean revived;        // whether this supporter has been revived
    private int hp;                 // optional: health if they can fight
    private int power;              // optional: support strength (buff/heal amount)
    private boolean equipped;       // whether the player has equipped this supporter

    public Supporter(String name, String ability) {
        this.name = name;
        this.ability = ability;
        this.revived = false;
        this.hp = 100;   // default values, can be tuned
        this.power = 10;
        this.equipped = false;
    }

    // --- Getters ---
    public String getName() { return name; }
    public String getAbility() { return ability; }
    public boolean isRevived() { return revived; }
    public int getHp() { return hp; }
    public int getPower() { return power; }

    public boolean isEquipped() { return equipped; }

    public void setEquipped(boolean equipped) { this.equipped = equipped; }

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
        String rev = revived ? "✅" : "❌";
        String eq = equipped ? " [E]" : "";
        return rev + " " + name + " (" + ability + ")" + eq;
    }
}
