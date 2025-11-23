package rpg.world;

import java.util.*;
import java.util.stream.Collectors;
import rpg.characters.Supporter;
import rpg.game.GameState;

public class SupporterPool {
    private static final Map<Integer, List<Supporter>> pool = new HashMap<>();

    static {
        // Stage 1 – School Rooftop (only Sir Khai; others introduced later)
        pool.put(1, Arrays.asList(
            new Supporter("Sir Khai", "Teacher", "Guidance") // scripted, kept
        ));

        // Stage 2 – Ruined Lab
        pool.put(2, Arrays.asList(
            new Supporter("School Security Guard", "Guardian", "Protect"),
            new Supporter("Canteen Staff", "Merchant", "Snack Aid"),
            new Supporter("Ma'am Dean", "Discipline", "Command")
        ));

        // Stage 3 – City Ruins
        pool.put(3, Arrays.asList(
            new Supporter("Jeepney Philosopher", "Philosopher", "Musing"),
            new Supporter("Mr. CodeChum", "Coder", "Debug")
        ));

        // Stage 4 – Fractured Sky
        pool.put(4, Arrays.asList(
            new Supporter("Skybridge Surveyor", "Scout", "Updraft"),
            new Supporter("Glass Elevator Operator", "Engineer", "Stabilize")
        ));
    }

    public static Supporter getRandomSupporter(int zone, Random rand) {
        List<Supporter> supporters = pool.get(zone);
        if (supporters == null || supporters.isEmpty()) return null;
        return supporters.get(rand.nextInt(supporters.size()));
    }

    // Return a random supporter from the pool that hasn't already been revived (by instance or name)
    public static Supporter getRandomUnrevivedSupporter(int zone, Random rand, GameState state) {
        List<Supporter> supporters = pool.get(zone);
        if (supporters == null || supporters.isEmpty()) return null;

        List<Supporter> candidates = supporters.stream()
            .filter(s -> s != null)
            .filter(s -> !s.isRevived())
            .filter(s -> {
                // also check by name against state.supporters to catch scripted/new instances
                for (Supporter ss : state.supporters) {
                    if (ss != null && ss.getName() != null && s.getName() != null && ss.getName().equalsIgnoreCase(s.getName()))
                        return false;
                }
                return true;
            })
            .collect(Collectors.toList());

        if (candidates.isEmpty()) return null;
        return candidates.get(rand.nextInt(candidates.size()));
    }
}
