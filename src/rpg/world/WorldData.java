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
                                // 🆕 ZONE 1 MOBS: General Student Struggles
                                new Enemy("Roaming Deadline", 40, 8, 12, 15),
                                new Enemy("Friends asking you to Drink", 50, 10, 14, 18),
                                new Enemy("Distracting Notification", 45, 9, 13, 16),
                                new Enemy("Tower of Textbooks", 60, 12, 16, 20)
                        },
                        new Enemy("CITU Logo (Fractured)", 120, 15, 25, 40),
                        new Weapon("Pencil Blade", 10, 12, 0.05, 1.5));

            case 2:
                return new ZoneConfig(
                        "Ruined Lab",
                        "Shattered corridors filled with failed experiments.",
                        new Enemy[] {
                                // 🆕 ZONE 2 MOBS: Lab/Tech Failures
                                new Enemy("Failed Prototype", 55, 10, 14, 20),
                                new Enemy("Malfunctioning PC", 80, 14, 20, 30),
                                new Enemy("Spilled Matcha Drink", 65, 12, 18, 25)
                        },
                        new Enemy("The Thesis Defense Panel (Fused)", 180, 20, 30, 60),
                        new Weapon("Logic Blade", 14, 18, 0.08, 1.5));

            case 3:
                return new ZoneConfig(
                        "City Ruins",
                        "Collapsed streets and burning wreckage, a fragile refuge among chaos.",
                        new Enemy[] {
                                // 🆕 ZONE 3 MOBS: Mental Stress & Burnout
                                new Enemy("Rising Anxiety", 90, 18, 25, 35),
                                new Enemy("Mental Block", 100, 20, 28, 40),
                                new Enemy("Academic Burnout", 85, 17, 24, 34)
                        },
                        new Enemy("Ang Lindol, the Academic-Shaker", 220, 25, 35, 70), 
                        new Weapon("Aftershock Hammer", 16, 24, 0.10, 1.6));

            case 4:
                return new ZoneConfig(
                        "Fractured Sky",
                        "Shattered sky bridges spiral around a broken skyscraper exposed to fierce winds.",
                        new Enemy[] {
                                // 🆕 ZONE 4 MOBS: Connectivity & External Chaos
                                new Enemy("Unstable Connection", 115, 24, 32, 45),
                                new Enemy("Power Outage", 130, 27, 35, 50),
                                new Enemy("Flood Terror", 145, 30, 38, 55)
                        },
                        new Enemy("Bagyong Tino", 320, 35, 45, 95), 
                        new Weapon("Trident of Storms", 20, 32, 0.15, 1.9));

            // ✅ Case 5: The Source Root
            case 5:
                return new ZoneConfig(
                        "The Source Root",
                        "The physical world dissolves into streams of raw white code. The server hum is deafening.",
                        new Enemy[] {
                                // 🆕 ZONE 5 MOBS: The Final Errors
                                new Enemy("Syntax Error", 160, 36, 42, 70),
                                new Enemy("Logic Fallacy", 180, 40, 48, 80)
                        },
                        new Enemy("The Choice", 380, 45, 60, 120),
                        null);

            default:
                return null;
        }
    }
}