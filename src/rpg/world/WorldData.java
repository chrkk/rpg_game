package rpg.world;

import rpg.characters.Enemy;
import rpg.items.Weapon;

public class WorldData {
    public static ZoneConfig getZone(int zone) {
        switch (zone) {
            case 1:
                return new ZoneConfig(
                        "School Rooftop", // ✅ updated name
                        new Enemy[] {
                                new Enemy("Stray Wolf", 40, 8, 12, 15), // feral, scavenging predator
                                new Enemy("Feral Boar", 50, 10, 14, 18), // aggressive, territorial beast
                                new Enemy("Crystal-Scarred Crow", 45, 9, 13, 16), // rooftop scavenger mutated by shards
                                new Enemy("Lesser Crystal Golem", 60, 12, 16, 20) // early hint of lab crystal //
                                                                                  // experiments
                        },
                        // Mini‑boss for Zone 1
                        new Enemy("CITU Logo (Fractured)", 120, 15, 25, 40),
                        // Starter weapon reward
                        new Weapon("Pencil Blade", 8, 12, 0.05, 1.5));
            case 2:
                return new ZoneConfig(
                        "Ruined Lab", // ✅ updated name
                        new Enemy[] {
                                new Enemy("Fractured Homunculus", 55, 10, 14, 20), // twisted bio‑experiment
                                new Enemy("Shattered Automaton", 80, 14, 20, 30) // broken lab robot
                        },
                        new Enemy("Aberrant Chimera", 180, 20, 30, 60), // zone boss: grotesque fusion
                        new Weapon("Prototype Blade", 12, 18, 0.08, 1.5));
            default:
                return new ZoneConfig(
                        "Unknown Wasteland",
                        new Enemy[] { new Enemy("Shadow Spawn", 70, 15, 20, 30) },
                        new Enemy("Ancient Horror", 250, 25, 35, 70),
                        null);
        }
    }
}
