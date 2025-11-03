package rpg.skills;

public abstract class Skill {
    protected String name;
    protected int manaCost;
    protected int power;

    public Skill(String name, int manaCost, int power) {
        this.name = name;
        this.manaCost = manaCost;
        this.power = power;
    }

    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public int getPower() { return power; }

    // Every skill must define what happens when used
    public abstract String useSkill();
}
    