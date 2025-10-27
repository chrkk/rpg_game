package rpg.characters;

import java.util.Random;

public class Enemy {
    private String name;
    private int hp;
    private int attack;
    private int specialAttack;
    private int expReward; // ---> new added

    public Enemy(String name, int hp, int attack, int specialAttack, int expReward) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.specialAttack = specialAttack;
        this.expReward = expReward; // ---> new added
    }

    //Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
    public int getSpecialAttack() { return specialAttack; }
    public int getExpReward() { return expReward; } // ---> new added

    public void takeDamage(int dmg) {
        hp = Math.max(0, hp - dmg);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public int enemyAction() {
        Random rand = new Random();
        // 70% chance normal attack, 30% chance special
        return rand.nextInt(100) < 70 ? attack : specialAttack;
    }
}
