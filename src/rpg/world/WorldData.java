package rpg.world;

import rpg.characters.Enemy;
import rpg.items.Weapon;

public class WorldData {
    public static ZoneConfig getZone(int zone) {
        switch (zone) {
            case 1:
                return new ZoneConfig(
                    "Ruined Classroom",
                    new Enemy[]{
                        new Enemy("Wild Beast", 40, 8, 12, 15),
                        new Enemy("Corrupted Guardian", 60, 12, 18, 20)
                    },
                    new Enemy("Stone Titan", 120, 15, 25, 40),
                    new Weapon("Pencil Blade", 10)
                );
            case 2:
                return new ZoneConfig(
                    "Forest Clearing",
                    new Enemy[]{
                        new Enemy("Forest Wolf", 55, 10, 14, 20),
                        new Enemy("Ent Guardian", 80, 14, 20, 30)
                    },
                    new Enemy("Forest Guardian", 180, 20, 30, 60),
                    new Weapon("Rusty Sword", 15)
                );
            default:
                return new ZoneConfig(
                    "Unknown Wasteland",
                    new Enemy[]{ new Enemy("Shadow Spawn", 70, 15, 20, 30) },
                    new Enemy("Ancient Horror", 250, 25, 35, 70),
                    null
                );
        }
    }
}
