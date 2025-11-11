package rpg.world;

import rpg.characters.Enemy;
import rpg.items.Weapon;

public class WorldData {
    public static ZoneConfig getZone(int zone) {
        switch (zone) {
            case 1:
                return new ZoneConfig(
                        "School Rooftop", // Safe Zone 1
                        "The rooftop safe zone, overlooking the frozen campus.",
                        new Enemy[] {
                                new Enemy("Stray Wolf", 40, 8, 12, 15),
                                new Enemy("Feral Boar", 50, 10, 14, 18),
                                new Enemy("Crystal-Scarred Crow", 45, 9, 13, 16),
                                new Enemy("Lesser Crystal Golem", 60, 12, 16, 20)
                        },
                        new Enemy("CITU Logo (Fractured)", 120, 15, 25, 40), // miniboss
                        new Weapon("Pencil Blade", 8, 12, 0.05, 1.5));

            case 2:
                return new ZoneConfig(
                        "Ruined Lab", // Safe Zone 2
                        "Shattered corridors filled with failed experiments.",
                        new Enemy[] {
                                new Enemy("Fractured Homunculus", 55, 10, 14, 20),
                                new Enemy("Shattered Automaton", 80, 14, 20, 30),
                                new Enemy("Toxic Slime Residue", 65, 12, 18, 25)
                        },
                        new Enemy("Aberrant Chimera", 180, 20, 30, 60), // boss
                        new Weapon("Prototype Blade", 12, 18, 0.08, 1.5));

            case 3:
                return new ZoneConfig(
                        "City Ruins", // Safe Zone 3
                        "Collapsed streets and burning wreckage, a fragile refuge among chaos.",
                        new Enemy[] {
                                new Enemy("Ash-Walker", 90, 18, 25, 35),
                                new Enemy("Concrete Husk", 100, 20, 28, 40),
                                new Enemy("Burnt Effigy", 85, 17, 24, 34)
                        },
                        new Enemy("Screaming Billboard", 220, 25, 35, 70), // boss
                        new Weapon("Rebar Blade", 16, 24, 0.10, 1.6));

            default:
                return new ZoneConfig(
                        "Unknown Wasteland", // Zone 4 placeholder
                        "A desolate expanse scarred by silence and danger.",
                        new Enemy[] {
                                new Enemy("Shadow Spawn", 70, 15, 20, 30),
                                new Enemy("Ash Phantom", 95, 20, 28, 40)
                        },
                        new Enemy("Ancient Horror", 250, 25, 35, 70),
                        null);
        }
    }
}
