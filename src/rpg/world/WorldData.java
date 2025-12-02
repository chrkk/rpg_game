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
                                new Enemy("Roaming Deadline", 40, 8, 12, 16),
                                new Enemy("Friends asking you to Drink", 50, 10, 14, 18),
                                new Enemy("Distracting Notification", 45, 9, 13, 20),
                                new Enemy("Tower of Textbooks", 60, 12, 16, 22)
                        },
                        new Enemy("CITU Logo (Fractured)", 120, 15, 25, 45),
                        new Weapon("Pencil Blade", 10, 12, 0.05, 1.5));

            case 2:
                return new ZoneConfig(
                        "Ruined Lab",
                        "Shattered corridors filled with failed computer experiments.",
                        new Enemy[] {
                                // 🆕 ZONE 2 MOBS: Lab/Tech Failures
                                new Enemy("Failed Thesis", 55, 10, 14, 25),
                                new Enemy("Malfunctioning PC", 80, 14, 20, 30),
                                new Enemy("Spilled Matcha Drink", 65, 12, 18, 28)
                        },
                        new Enemy("The Thesis Defense Panel (Fused)", 180, 20, 30, 70),
                        new Weapon("Logic Blade", 14, 18, 0.08, 1.5));

            case 3:
                return new ZoneConfig(
                        "City Ruins",
                        "Collapsed streets and burning wreckage, a fragile refuge among chaos.",
                        new Enemy[] {
                                // 🆕 ZONE 3 MOBS: Mental Stress & Burnout
                                new Enemy("Rising Anxiety", 90, 18, 25, 36),
                                new Enemy("Mental Block", 100, 20, 28, 42),
                                new Enemy("Academic Burnout", 85, 17, 24, 38)
                        },
                        new Enemy("Lindol Academic-Shaker", 220, 25, 35, 90), 
                        new Weapon("Aftershock Hammer", 16, 24, 0.10, 1.6));

            case 4:
                return new ZoneConfig(
                        "Fractured Sky",
                        "Shattered sky bridges spiral around a broken skyscraper exposed to fierce winds.",
                        new Enemy[] {
                                // 🆕 ZONE 4 MOBS: Connectivity & External Chaos
                                new Enemy("Unstable Connection", 115, 24, 32, 48),
                                new Enemy("Power Outage", 130, 27, 35, 54),
                                new Enemy("Flood Terror", 145, 30, 38, 58)
                        },
                        new Enemy("Bagyong Tino", 320, 35, 45, 115), 
                        new Weapon("Trident of Storms", 20, 32, 0.15, 1.9));

            case 5:
                return new ZoneConfig(
                        "The Source Root",
                        "The physical world dissolves into streams of raw white code. The server hum is deafening.",
                        new Enemy[] {
                                // 🆕 ZONE 5 MOBS: The Final Errors
                                new Enemy("Syntax Error", 160, 36, 42, 70),
                                new Enemy("Logic Fallacy", 180, 40, 48, 80)
                        },
                        new Enemy("The Choice", 380, 45, 60, 150),
                        null);

            default:
                return null;
        }
    }
}