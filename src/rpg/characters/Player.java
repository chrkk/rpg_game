package rpg.characters;

import rpg.items.Weapon;

// 🆕 Newly added - Skills
import rpg.skills.Skill;
import rpg.skills.ScientistSkills;
import rpg.skills.FighterSkills;
import rpg.skills.ArchmageSkills;

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
    // store base intelligence for temporary buffs
    private int baseIntelligence;

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

        // Apply trait bonuses (but DO NOT assign skills yet)
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
        }

        // Skills locked until Level 2
        this.skills = new Skill[0];

        this.baseDefense = this.defense;
        this.baseIntelligence = this.intelligence;
    }

    // 🆕 Unlock skills when reaching Level 2
    private void unlockSkills() {
        switch (trait.toLowerCase()) {
            case "scientist":
                this.skills = new Skill[] {
                        ScientistSkills.chemicalStrike,
                        ScientistSkills.plasmaField,
                        ScientistSkills.nuclearBlast
                };
                break;
            case "fighter":
                this.skills = new Skill[] {
                        FighterSkills.powerPunch,
                        FighterSkills.warCry,
                        FighterSkills.earthBreaker
                };
                break;
            case "archmage":
                this.skills = new Skill[] {
                        ArchmageSkills.fireBolt,
                        ArchmageSkills.arcaneShield,
                        ArchmageSkills.meteorStorm
                };
                break;
            default:
                this.skills = new Skill[0];
                break;
        }
        System.out.println("[System] > ⚡ A surge of power awakens... Your class skills are now available!");
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
            System.out.println("[System] > You have no skills assigned!");
            return;
        }

        if (skillIndex < 0 || skillIndex >= skills.length) {
            System.out.println("[System] > Invalid skill choice!");
            return;
        }

        Skill chosenSkill = skills[skillIndex];

        // Check if player has enough mana
        if (mana < chosenSkill.getManaCost()) {
            System.out.println("[System] > Not enough mana to use " + chosenSkill.getName() + "!");
            return;
        }

        // Deduct mana cost
        useMana(chosenSkill.getManaCost());

        // Calculate damage based on skill power + intelligence scaling
        int damage = chosenSkill.getPower() + (intelligence / 2);
        enemy.takeDamage(damage);

        // Display skill use text and result
        System.out.println("[System] > " + chosenSkill.useSkill());
        System.out.println("[System] > It dealt " + damage + " damage!");
    }

    // Combat methods
    public int takeDamage(int dmg) {
        int effectiveDamage;

        if (defense > 0) {
            effectiveDamage = Math.max(0, dmg - defense);
            System.out.println("[System] > 🛡️ Your defense absorbed " + (dmg - effectiveDamage) + " damage, but broke!");
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

    public int restoreMana(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int beforeMana = mana;
        mana = Math.min(maxMana, mana + amount);
        return mana - beforeMana;
    }

    public void healFull() {
        hp = maxHp;
        mana = maxMana;
        defense = baseDefense;
    }

    // Add temporary defense for the duration of a combat or event
    public void addTemporaryDefense(int amount) {
        this.defense += amount;
    }

    // Restore defense back to base value (use after combat)
    public void resetDefense() {
        this.defense = this.baseDefense;
    }

    // Add temporary intelligence for the duration of a combat or event
    public void addTemporaryIntelligence(int amount) {
        this.intelligence += amount;
    }

    // Restore intelligence back to base value
    public void resetIntelligence() {
        this.intelligence = this.baseIntelligence;
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
            // Level increases
            exp -= expToNextLevel;
            level++;

            // Increase EXP requirement slowly
            expToNextLevel += 50;

            // Stats level up   
            maxHp += 20;
            maxMana += 10;
            defense += 1;         // 🆕 Defense +1
            intelligence += 2;    // 🆕 Intelligence +2

            // Restore HP and Mana to new max
            hp = maxHp;
            mana = maxMana;

            // 🆕 Enhanced Level Up UI
            String levelUpBanner = 
                "\n" +
                "╔══════════════════════════════════════════════════════════╗\n" +
                "║                   🌟  L E V E L   U P !  🌟              ║\n" +
                "╠══════════════════════════════════════════════════════════╣\n" +
                "║                                                          ║\n" +
                String.format("║  Congratulations! You reached Level %-3d                  ║\n", level) +
                "║                                                          ║\n" +
                "║  📈 STATS INCREASED:                                     ║\n" +
                "║  ────────────────────────────────────────────────────    ║\n" +
                "║     ❤️  Max HP        +20                                ║\n" +
                "║     🔮 Max Mana      +10                                ║\n" +
                "║     🛡️  Defense       +1                                 ║\n" +
                "║     🧠 Intelligence  +2                                 ║\n" +
                "║                                                          ║\n" +
                "╚══════════════════════════════════════════════════════════╝\n";

            System.out.println(levelUpBanner);

            // Unlock skills at Level 2
            if (level == 2 && (skills == null || skills.length == 0)) {
                unlockSkills();
            }
        }
    }

    // Partial heal method
    public void heal(int amount) {
        if (amount <= 0)
            return; // ignore invalid heals
        int beforeHp = hp;
        hp = Math.min(maxHp, hp + amount);
        System.out.println("[System] > ❤️ " + name + " healed for " + (hp - beforeHp) + " HP!");
    }

    // Developer helper to recalculate stats for a desired level
    public void developerSetLevel(int targetLevel) {
        if (targetLevel < 1)
            targetLevel = 1;

        int baseHp = 100;
        int baseMana = 50;
        int baseDefenseStat = 5;
        int baseIntStat = 5;

        switch (trait.toLowerCase()) {
            case "scientist":
                baseIntStat += 5;
                baseDefenseStat += 3;
                break;
            case "fighter":
                baseHp += 30;
                baseDefenseStat += 5;
                break;
            case "archmage":
                baseIntStat += 7;
                baseMana += 30;
                break;
        }

        int extraLevels = targetLevel - 1;
        if (extraLevels > 0) {
            baseHp += extraLevels * 20;
            baseMana += extraLevels * 10;
            baseDefenseStat += extraLevels;
            baseIntStat += extraLevels * 2;
        }

        this.maxHp = baseHp;
        this.hp = maxHp;
        this.maxMana = baseMana;
        this.mana = maxMana;
        this.defense = baseDefenseStat;
        this.intelligence = baseIntStat;
        this.baseDefense = baseDefenseStat;
        this.baseIntelligence = baseIntStat;

        this.level = targetLevel;
        this.exp = 0;
        this.expToNextLevel = 100 + extraLevels * 50;

        if (targetLevel >= 2) {
            unlockSkills();
        } else {
            this.skills = new Skill[0];
        }
    }

}