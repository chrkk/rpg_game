package rpg.items;

import java.util.Random;

public class Weapon {
    private String name;
    private int minDamage;
    private int maxDamage;
    private double critChance;   // e.g. 0.1 = 10% chance
    private double critMultiplier; // e.g. 2.0 = double damage

    public Weapon(String name, int minDamage, int maxDamage, double critChance, double critMultiplier) {
        this.name = name;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.critChance = critChance;
        this.critMultiplier = critMultiplier;
    }

    public String getName() { return name; }
    public int getMinDamage() { return minDamage; }
    public int getMaxDamage() { return maxDamage; }
    public double getCritChance() { return critChance; }
    public double getCritMultiplier() { return critMultiplier; }

    // 🎲 Roll damage dynamically
    public int rollDamage(Random rand) {
        int dmg = rand.nextInt(maxDamage - minDamage + 1) + minDamage;
        if (rand.nextDouble() < critChance) {
            System.out.println("💥 Critical Hit!");
            dmg = (int)(dmg * critMultiplier);
        }
        return dmg;
    }

    @Override
    public String toString() {
        return name + " (Damage: " + minDamage + "-" + maxDamage + 
               ", Crit: " + (int)(critChance * 100) + "% x" + critMultiplier + ")";
    }
}
