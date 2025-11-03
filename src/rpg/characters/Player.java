package rpg.characters;

import rpg.items.Weapon;

// 🆕 Newly added - Skills
import rpg.skills.Skill;
import rpg.skills.ScientistSkills;
import rpg.skills.FighterSkills;
import rpg.skills.ArchmageSkills;
import rpg.characters.Enemy;

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

    // store base defense for resetting
    private int baseDefense;

    // Exp stats
    private int level;
    private int exp;
    private int expToNextLevel;

    // 🆕 Newly added - Skills
    private Skill[] skills; // store 3 class-specific skills

    public Player(String name, String trait) {
        this.name = name;
        this.trait = trait;

        // Exp stats
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

                // 🆕 Newly added - Skills
                this.skills = new Skill[] {
                    ScientistSkills.chemicalStrike,
                    ScientistSkills.plasmaField,
                    ScientistSkills.nuclearBlast
                };
                break;

            case "fighter":
                this.maxHp += 30;
                this.hp = maxHp;
                this.defense += 5;

                // 🆕 Newly added - Skills
                this.skills = new Skill[] {
                    FighterSkills.powerPunch,
                    FighterSkills.warCry,
                    FighterSkills.earthBreaker
                };
                break;

            case "archmage":
                this.intelligence += 7;
                this.maxMana += 30;
                this.mana = maxMana;

                // 🆕 Newly added - Skills
                this.skills = new Skill[] {
                    ArchmageSkills.fireBolt,
                    ArchmageSkills.arcaneShield,
                    ArchmageSkills.meteorStorm
                };
                break;

            default:
                // 🆕 Newly added - Skills
                this.skills = new Skill[0]; // fallback if no trait found
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

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    // 🆕 Newly added - Skills
    public Skill[] getSkills() {
        return skills;
    }

    // 🆕 Newly added - Skills
    public void useSkillOnEnemy(Enemy enemy, int skillIndex) {
        if (skills == null || skills.length == 0) {
            System.out.println("You have no skills assigned!");
            return;
        }

        if (skillIndex < 0 || skillIndex >= skills.length) {
            System.out.println("Invalid skill choice!");
            return;
        }

        Skill chosenSkill = skills[skillIndex];

        // Check if player has enough mana
        if (mana < chosenSkill.getManaCost()) {
            System.out.println("Not enough mana to use " + chosenSkill.getName() + "!");
            return;
        }

        // Deduct mana cost
        useMana(chosenSkill.getManaCost());

        // Calculate damage based on skill power + intelligence scaling
        int damage = chosenSkill.getPower() + (intelligence / 2);
        enemy.takeDamage(damage);

        // Display skill use text and result
        System.out.println(chosenSkill.useSkill());
        System.out.println("It dealt " + damage + " damage!");
    }

    // Combat methods
    public int takeDamage(int dmg) {
        int effectiveDamage;

        if (defense > 0) {
            effectiveDamage = Math.max(0, dmg - defense);
            System.out.println("🛡️ Your defense absorbed " + (dmg - effectiveDamage) + " damage, but broke!");
            defense = 0;
        } else {
            effectiveDamage = dmg;
        }

        int beforeHp = hp;
        hp = Math.max(0, hp - effectiveDamage);
        return beforeHp - hp;
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

    public void setHp(int hp) {
        this.hp = Math.min(hp, maxHp);
    }

    public void gainExp(int amount) {
        exp += amount;

        while (exp >= expToNextLevel) {
            exp -= expToNextLevel;
            level++;
            expToNextLevel += 25;
            maxHp += 10;
            maxMana += 5;
            defense += 1;
            intelligence += 1;

            baseDefense = defense;
            healFull();
            System.out.println("✨ Level Up! " + name + " is now Level " + level + "!");
        }
    }
}
