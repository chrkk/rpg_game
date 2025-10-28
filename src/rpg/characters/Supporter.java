package rpg.characters;

public class Supporter {
    private String name;
    private String trait;

    public Supporter(String name, String trait) {
        this.name = name;
        this.trait = trait;
    }

    public String getName() {
        return name;
    }

    public String getTrait() {
        return trait;
    }

    @Override
    public String toString() {
        return name + " the " + trait;
    }
}
