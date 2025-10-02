package rpg.characters;

public class Player {
    private String name;
    private String trait;
    private int hp;
    private int maxHp;
    private int stamina;
    private int hunger;

    public Player(String name, String trait) {
        this.name = name;
        this.trait = trait;
        this.maxHp = 100;
        this.hp = maxHp;
        this.stamina = 50;
        this.hunger = 20;
    }

    public String getName() { return name; }
    public String getTrait() { return trait; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getStamina() { return stamina; }
    public int getHunger() { return hunger; }

    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    public void healFull() {
        hp = maxHp;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
