package rpg.world;

import java.util.*;
import rpg.characters.Supporter;

public class SupporterPool {
    private static final Map<Integer, List<Supporter>> pool = new HashMap<>();

    static {
        // Stage 1 – School Rooftop
        pool.put(1, Arrays.asList(
            new Supporter("Sir Khai", "Teacher", "Guidance"), // scripted, but kept here for consistency
            new Supporter("Aya", "Strategist", "Encouragement")
        ));

        // Stage 2 – Ruined Lab
        pool.put(2, Arrays.asList(
            new Supporter("Lira", "Scientist", "Chemical Aid"),
            new Supporter("Ryo", "Inventor", "Repair")
        ));

        // Stage 3 – City Ruins
        pool.put(3, Arrays.asList(
            new Supporter("Hana", "Medic", "First Aid"),
            new Supporter("Daisuke", "Guardian", "Shield Wall"),
            new Supporter("Kael", "Merchant", "Resourceful")
        ));
    }

    public static Supporter getRandomSupporter(int zone, Random rand) {
        List<Supporter> supporters = pool.get(zone);
        if (supporters == null || supporters.isEmpty()) return null;
        return supporters.get(rand.nextInt(supporters.size()));
    }
}
