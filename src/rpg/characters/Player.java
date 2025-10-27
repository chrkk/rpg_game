package rpg.characters;

import rpg.items.Weapon;

public class Player {
    private Weapon weapon;
    private String name;
    private String trait;

    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int defense;
    private int intelligence;

    // 🆕 store base defense for resetting
    private int baseDefense;
    // Exp stats ---> new
    private int level;
    private int exp;
    private int expToNextLevel;

    public Player(String name, String trait) {
        this.name = name;
        this.trait = trait;

        // Exp stats ---> new
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = 100;

        // Base stats (before bonuses)
        this.maxHp = 100;
        this.hp = maxHp;
        this.maxMana = 50;
        this.mana = maxMana;
        this.defense = 5;
        this.intelligence = 5;

        // weapon
        this.weapon = null;

        // Apply trait bonuses
        switch (trait.toLowerCase()) {
            case "scientist":
                this.intelligence += 5;
                this.defense += 3;
                break;
            case "fighter":
                this.maxHp += 30;
                this.hp = maxHp;
                this.defense += 5;
                break;
            case "archmage":
                this.intelligence += 7;
                this.maxMana += 30;
                this.mana = maxMana;
                break;
            default:
                // fallback if invalid trait
                break;
        }

        this.baseDefense = this.defense;
    }

    // weapon
    public Weapon getWeapon() {
        return weapon;
    }

    public void equipWeapon(Weapon newWeapon) {
        this.weapon = newWeapon;
    }
    // weapon

    // Getters
    public String getName() {
        return name;
    }

    public String getTrait() {
        return trait;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMana() {
        return mana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getDefense() {
        return defense;
    }

    public int getIntelligence() {
        return intelligence;
    }

    // EXP and Level getters ---> new
    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    // Combat methods
    public int takeDamage(int dmg) {
        int effectiveDamage;

        if (defense > 0) {
            // Apply defense once
            effectiveDamage = Math.max(0, dmg - defense);
            System.out.println("🛡️ Your defense absorbed " + (dmg - effectiveDamage) + " damage, but broke!");
            defense = 0; // defense breaks after first use
        } else {
            // No defense left, take full damage
            effectiveDamage = dmg;
        }

        int beforeHp = hp;
        hp = Math.max(0, hp - effectiveDamage);
        return beforeHp - hp; // actual damage applied
    }

    public void useMana(int cost) {
        mana = Math.max(0, mana - cost);
    }

    public void healFull() {
        hp = maxHp;
        mana = maxMana;
        defense = baseDefense;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    // setter for healing from meat
    public void setHp(int hp) {
        this.hp = Math.min(hp, maxHp); // prevents overhealing
    }

    public void gainExp(int amount) {
        // System.out.println(name + " gained " + amount + " EXP!");
        exp += amount;

        while (exp >= expToNextLevel) {
            exp -= expToNextLevel;
            level++;
            expToNextLevel += 25; // makes it harder each level
            maxHp += 10;
            maxMana += 5;
            defense += 1;
            intelligence += 1;

            // 🆕 update baseDefense when leveling up
            baseDefense = defense;
            healFull();
            System.out.println("✨ Level Up! " + name + " is now Level " + level + "!");
        }
    }
}
