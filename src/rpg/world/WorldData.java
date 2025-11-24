package rpg.world;

import rpg.characters.Enemy;
import rpg.items.Weapon;

public class WorldData {
    public static ZoneConfig getZone(int zone) {
        switch (zone) {
            case 1:
                return new ZoneConfig(
                        "School Rooftop",
                        "The rooftop safe zone, overlooking the frozen campus.",
                        new Enemy[] {
                                new Enemy("Stray Wolf", 40, 8, 12, 15),
                                new Enemy("Feral Boar", 50, 10, 14, 18),
                                new Enemy("Crystal-Scarred Crow", 45, 9, 13, 16),
                                new Enemy("Lesser Crystal Golem", 60, 12, 16, 20)
                        },
                        new Enemy("CITU Logo (Fractured)", 120, 15, 25, 40),
                        new Weapon("Pencil Blade", 8, 12, 0.05, 1.5));

            case 2:
                return new ZoneConfig(
                        "Ruined Lab",
                        "Shattered corridors filled with failed experiments.",
                        new Enemy[] {
                                new Enemy("Fractured Homunculus", 55, 10, 14, 20),
                                new Enemy("Shattered Automaton", 80, 14, 20, 30),
                                new Enemy("Toxic Slime Residue", 65, 12, 18, 25)
                        },
                        new Enemy("The Thesis Defense Panel (Fused)", 180, 20, 30, 60),
                        new Weapon("Prototype Blade", 12, 18, 0.08, 1.5));

            case 3:
                return new ZoneConfig(
                        "City Ruins",
                        "Collapsed streets and burning wreckage, a fragile refuge among chaos.",
                        new Enemy[] {
                                new Enemy("Ash-Walker", 90, 18, 25, 35),
                                new Enemy("Concrete Husk", 100, 20, 28, 40),
                                new Enemy("Burnt Effigy", 85, 17, 24, 34)
                        },
                        new Enemy("Yanig, the Earth-Shaker", 220, 25, 35, 70), 
                        new Weapon("Rebar Blade", 16, 24, 0.10, 1.6));

            case 4:
                return new ZoneConfig(
                        "Fractured Sky",
                        "Shattered sky bridges spiral around a broken skyscraper exposed to fierce winds.",
                        new Enemy[] {
                                new Enemy("Skyglass Stalker", 115, 24, 32, 45),
                                new Enemy("Cable-Wraith Diver", 130, 27, 35, 50),
                                new Enemy("Storm-Tethered Sentinel", 145, 30, 38, 55)
                        },
                        new Enemy("Bagyong Tino", 320, 35, 45, 95), 
                        new Weapon("Skybridge Saber", 20, 32, 0.15, 1.9));

            // ✅ UPDATED: Now specifically Case 5 instead of Default
            case 5:
                return new ZoneConfig(
                        "The Source Root",
                        "The physical world dissolves into streams of raw white code. The server hum is deafening.",
                        new Enemy[] {
                                // Updated mobs to fit the CS/IT theme
                                new Enemy("Compile Error", 160, 36, 42, 70),
                                new Enemy("Corrupted Memory", 180, 40, 48, 80)
                        },
                        new Enemy("The Choice", 380, 45, 60, 120),
                        null);

            default:
                return null;
        }
    }
}