package rpg.world;

import rpg.characters.Enemy;
import rpg.items.Weapon;

public class ZoneConfig {
    public final String name;         // short label
    public final String description;  // longer flavor text
    public final Enemy[] mobs;
    public final Enemy boss;
    public final Weapon searchLoot;

    public ZoneConfig(String name, String description, Enemy[] mobs, Enemy boss, Weapon searchLoot) {
        this.name = name;
        this.description = description;
        this.mobs = mobs;
        this.boss = boss;
        this.searchLoot = searchLoot;
    }
}
