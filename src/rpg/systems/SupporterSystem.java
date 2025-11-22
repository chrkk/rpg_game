package rpg.systems;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import rpg.characters.Supporter;
import rpg.characters.Player;
import rpg.characters.Enemy;
import rpg.game.GameState;
import rpg.utils.TextEffect;

/**
 * Encapsulates per-combat supporter behaviors and state (cooldowns/durations).
 * Keeps CombatSystem focused and makes supporter logic extensible.
 */
public class SupporterSystem {

    private final Map<Supporter, SupporterCombatState> combatState = new ConcurrentHashMap<>();

    private static class SupporterCombatState {
        int inspireRemaining = 0;
        int defRemaining = 0;
        int cooldown = 0;
    }

    public SupporterSystem() {
    }

    // Called once at combat start to apply start-of-combat buffs
    public void applyStartOfCombat(GameState state, Player player) {
        try {
            if (state.supporters == null) return;
            for (Supporter s : state.supporters) {
                if (s == null || !s.isRevived() || !s.isEquipped()) continue;

                // ensure a combat state exists
                combatState.putIfAbsent(s, new SupporterCombatState());

                // For the small unique set of supporters we check by name/trait
                String lname = s.getName() == null ? "" : s.getName().toLowerCase();
                if (lname.contains("sir khai") || lname.contains("khai")) {
                    TextEffect.typeWriter(s.getName() + " offers guidance from the sidelines — you feel steadier. (DEF +5 for this fight)", 40);
                    player.addTemporaryDefense(5);
                }
            }
        } catch (Exception e) {
            System.err.println("SupporterSystem.applyStartOfCombat error -> " + e.getMessage());
        }
    }

    // Called each round (from CombatSystem) to let supporters act
    public void performSupporterTurns(GameState state, Player player, Enemy enemy, int turnCount, Random rand) {
        try {
            if (state.supporters == null) return;
            for (Supporter s : state.supporters) {
                if (s == null || !s.isRevived() || !s.isEquipped()) continue;

                SupporterCombatState st = combatState.computeIfAbsent(s, k -> new SupporterCombatState());

                int speakChance = 30; // percent

                // Use name/ability-based unique supporter behaviors for the small fixed set
                String lname = s.getName() == null ? "" : s.getName().toLowerCase();
                if (lname.contains("sir khai") || lname.contains("khai")) {
                    String[] khaiLines = new String[] {
                        "Sir Khai: 'Breathe, and strike with purpose.'",
                        "Sir Khai: 'Focus your mind, not just your fists.'",
                        "Sir Khai: 'Small steps, steady hands.'",
                        "Sir Khai: 'Remember why you started.'"
                    };
                    if (rand.nextInt(100) < speakChance) TextEffect.typeWriter(khaiLines[rand.nextInt(khaiLines.length)], 25);

                    if (st.inspireRemaining == 0 && turnCount % 3 == 0 && rand.nextInt(100) < 40) {
                        TextEffect.typeWriter("✨ The air stills as " + s.getName() + " steps forward.", 40);
                        TextEffect.typeWriter(s.getName() + " intones: 'Center yourself — clarity follows.'", 40);
                        TextEffect.typeWriter("A warm clarity fills your head. You feel sharper. (INT +3 for 2 turns)", 40);
                        player.addTemporaryIntelligence(3);
                        st.inspireRemaining = 2;
                    }
                } else if (lname.contains("dean")) {
                    String[] deanLines = new String[] {
                        "Ma'am Dean: 'Stay disciplined — control the fight.'",
                        "Ma'am Dean: 'Maintain your stance!'",
                        "Ma'am Dean: 'Don't let your guard drop.'"
                    };
                    if (rand.nextInt(100) < speakChance) TextEffect.typeWriter(deanLines[rand.nextInt(deanLines.length)], 25);

                    if (st.defRemaining == 0 && turnCount % 4 == 0 && rand.nextInt(100) < 30) {
                        TextEffect.typeWriter(s.getName() + " barks an order — you plant your feet. (+DEF +4 for 1 turn)", 40);
                        player.addTemporaryDefense(4);
                        st.defRemaining = 1;
                    }
                } else if (lname.contains("canteen") || (s.getTrait() != null && s.getTrait().toLowerCase().contains("merchant")) || lname.contains("kael")) {
                    String[] canteenLines = new String[] {
                        "Canteen Staff: 'You need energy? I've got something warm.'",
                        "Canteen Staff: 'Eat up — this'll keep you going.'",
                        "Canteen Staff: 'A little snack goes a long way.'"
                    };
                    if (rand.nextInt(100) < speakChance) TextEffect.typeWriter(canteenLines[rand.nextInt(canteenLines.length)], 25);

                    if (st.cooldown == 0 && player.getHp() < player.getMaxHp() && rand.nextInt(100) < 25) {
                        int healAmt = 3 + rand.nextInt(3); // 3-5
                        player.heal(healAmt);
                        TextEffect.typeWriter(s.getName() + " slips you a snack and heals " + healAmt + " HP.", 40);
                        st.cooldown = 2;
                    }
                } else {
                    String[] genericLines = new String[] {
                        s.getName() + ": 'I'm here if you need me.'",
                        s.getName() + ": 'Standing by.'",
                        s.getName() + ": 'I've got your back.'"
                    };
                    if (rand.nextInt(100) < speakChance) TextEffect.typeWriter(genericLines[rand.nextInt(genericLines.length)], 20);
                }
            }

            // After processing all supporters this turn, decrement their timers
            decrementSupporterStates();

        } catch (Exception e) {
            System.err.println("SupporterSystem.performSupporterTurns error -> " + e.getMessage());
        }
    }

    private void decrementSupporterStates() {
        for (Map.Entry<Supporter, SupporterCombatState> entry : combatState.entrySet()) {
            SupporterCombatState st = entry.getValue();
            if (st.inspireRemaining > 0) {
                st.inspireRemaining--;
            }
            if (st.defRemaining > 0) st.defRemaining--;
            if (st.cooldown > 0) st.cooldown--;
        }
    }

    // Reset buffs and clear per-combat state
    public void resetAfterCombat(Player player) {
        try {
            // Clear temporary intelligence and defense
            player.resetIntelligence();
            player.resetDefense();
            combatState.clear();
        } catch (Exception e) {
            System.err.println("SupporterSystem.resetAfterCombat error -> " + e.getMessage());
        }
    }
}
