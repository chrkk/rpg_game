package rpg.systems;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import rpg.characters.Supporter;
import rpg.characters.Player;
import rpg.game.GameState;
import rpg.utils.TextEffect;

/**
 * Encapsulates per-combat supporter behaviors and state (cooldowns/durations).
 * Keeps CombatSystem focused and makes supporter logic extensible.
 */
public class SupporterSystem {

    private final Map<Supporter, SupporterCombatState> combatState = new ConcurrentHashMap<>();

    private static class SupporterCombatState {
        int intBuffRemaining;
        int intBuffAmount;
        int defBuffRemaining;
        int defBuffAmount;
        int critBuffRemaining;
        double critBonus;
        boolean blockNextAttack;
        boolean dampenNextSpecial;
    }

    public SupporterSystem() {
    }

    // Called once at combat start to apply start-of-combat buffs
    public void applyStartOfCombat(GameState state, Player player) {
        try {
            if (state.supporters == null) return;
            // Ensure only one supporter is equipped — defensive guard in case other code set multiple
            boolean seenEquipped = false;
            for (Supporter sCheck : state.supporters) {
                if (sCheck == null) continue;
                if (sCheck.isEquipped()) {
                    if (!seenEquipped) {
                        seenEquipped = true;
                    } else {
                        sCheck.setEquipped(false);
                    }
                }
            }
            for (Supporter s : state.supporters) {
                if (s == null || !s.isRevived() || !s.isEquipped()) continue;

                SupporterCombatState st = stateFor(s);

                String lname = normalize(s.getName());
                if (lname.contains("sir khai") || lname.contains("khai")) {
                    TextEffect.typeWriter(s.getName() + " offers guidance from the sidelines — you feel steadier. (DEF +5 for this fight)", 40);
                    player.addTemporaryDefense(5);
                    st.defBuffAmount += 5;
                }
            }
        } catch (Exception e) {
            System.err.println("SupporterSystem.applyStartOfCombat error -> " + e.getMessage());
        }
    }

    // Called each round (from CombatSystem) to let supporters act
    public void performSupporterTurns(GameState state, Player player, int turnCount, Random rand) {
        try {
            if (state.supporters == null) return;
            for (Supporter s : state.supporters) {
                if (s == null || !s.isRevived() || !s.isEquipped()) continue;

                SupporterCombatState st = stateFor(s);

                int speakChance = 30; // percent
                String lname = normalize(s.getName());
                String lability = normalize(s.getAbility());
                if (rand.nextInt(100) < speakChance) {
                    speakLine(s, lname, rand);
                }

                if (!isAbilityTurn(turnCount)) {
                    continue;
                }

                if (lname.contains("sir khai") || lname.contains("khai")) {
                    if (rand.nextInt(100) < 40) {
                        applyIntBuff(player, st, 3, 2);
                        TextEffect.typeWriter("✨ " + s.getName() + " whispers guidance. Your thoughts sharpen! (+3 INT, 2 turns)", 40);
                    }
                } else if (lname.contains("cath") || lability.contains("command")) {
                    if (rand.nextInt(100) < 35) {
                        applyDefBuff(player, st, 4, 1);
                        TextEffect.typeWriter(s.getName() + " snaps: 'Hold formation!' (+4 DEF for the next hit)", 40);
                    }
                } else if (lname.contains("security") || lability.contains("protect")) {
                    if (rand.nextInt(100) < 30) {
                        st.blockNextAttack = true;
                        TextEffect.typeWriter(s.getName() + " steps in front — next attack damage reduced by 50%.", 40);
                    }
                } else if (lname.contains("canteen") || lname.contains("kael") || lability.contains("snack")) {
                    if (player.getHp() < player.getMaxHp() && rand.nextInt(100) < 40) {
                        int healAmt = 4 + rand.nextInt(3); // 4-6
                        player.heal(healAmt);
                        TextEffect.typeWriter(s.getName() + " hands you a snack. (+" + healAmt + " HP)", 40);
                    }
                } else if (lname.contains("philosopher") || lability.contains("musing")) {
                    if (rand.nextInt(100) < 35) {
                        applyIntBuff(player, st, 2, 2);
                        TextEffect.typeWriter(s.getName() + " shares a quiet musing. (+2 INT, 2 turns)", 40);
                    }
                } else if (lname.contains("codechum") || lability.contains("debug")) {
                    if (rand.nextInt(100) < 30) {
                        st.critBonus = 0.10;
                        st.critBuffRemaining = 2;
                        TextEffect.typeWriter(s.getName() + " optimizes your targeting routines. (+10% crit chance, 2 turns)", 40);
                    }
                } else if (lname.contains("kuya kim") || lability.contains("updraft")) {
                    if (rand.nextInt(100) < 35) {
                        st.dampenNextSpecial = true;
                        TextEffect.typeWriter(s.getName() + " summons an updraft — next special attack reduced by 40%.", 40);
                    }
                } else if (lname.contains("topnotcher") || lability.contains("stabilize")) {
                    if (rand.nextInt(100) < 40) {
                        int restored = 5 + rand.nextInt(4); // 5-8
                        int gained = player.restoreMana(restored);
                        if (gained > 0) {
                            TextEffect.typeWriter(s.getName() + " stabilizes your mana flow. (+" + gained + " MP)", 40);
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("SupporterSystem.performSupporterTurns error -> " + e.getMessage());
        }
    }

    public void endOfTurn(Player player) {
        decrementSupporterStates(player);
    }

    private void decrementSupporterStates(Player player) {
        for (Map.Entry<Supporter, SupporterCombatState> entry : combatState.entrySet()) {
            SupporterCombatState st = entry.getValue();
            if (st.intBuffRemaining > 0) {
                st.intBuffRemaining--;
                if (st.intBuffRemaining == 0 && st.intBuffAmount != 0) {
                    player.addTemporaryIntelligence(-st.intBuffAmount);
                    st.intBuffAmount = 0;
                    TextEffect.typeWriter(entry.getKey().getName() + "'s insight fades.", 30);
                }
            }
            if (st.defBuffRemaining > 0) {
                st.defBuffRemaining--;
                if (st.defBuffRemaining == 0 && st.defBuffAmount > 0) {
                    if (player.getDefense() >= st.defBuffAmount) {
                        player.addTemporaryDefense(-st.defBuffAmount);
                    }
                    st.defBuffAmount = 0;
                }
            }
            if (st.critBuffRemaining > 0) {
                st.critBuffRemaining--;
                if (st.critBuffRemaining == 0) {
                    st.critBonus = 0;
                    TextEffect.typeWriter(entry.getKey().getName() + "'s debug routine ends.", 30);
                }
            }
        }
    }

    private boolean isAbilityTurn(int turnCount) {
        return turnCount > 0 && turnCount % 3 == 0;
    }

    private void speakLine(Supporter supporter, String lname, Random rand) {
        String[] lines;
        if (lname.contains("sir khai") || lname.contains("khai")) {
            lines = new String[] {
                "Sir Khai: 'Breathe, and strike with purpose.'",
                "Sir Khai: 'Focus your mind, not just your fists.'",
                "Sir Khai: 'Small steps, steady hands.'",
                "Sir Khai: 'Remember why you started.'"
            };
        } else if (lname.contains("cath")) {
            lines = new String[] {
                "Ma'am Cath: 'Stay disciplined — control the fight.'",
                "Ma'am Cath: 'Maintain your stance!'",
                "Ma'am Cath: 'Don't let your guard drop.'"
            };
        } else if (lname.contains("security")) {
            lines = new String[] {
                "Security Guard: 'I've got front-line duty.'",
                "Security Guard: 'No one slips past me.'",
                "Security Guard: 'Keep pushing, I'll cover you.'"
            };
        } else if (lname.contains("canteen") || lname.contains("kael")) {
            lines = new String[] {
                "Canteen Staff: 'You need energy? I've got something warm.'",
                "Canteen Staff: 'Eat up — this'll keep you going.'",
                "Canteen Staff: 'A little snack goes a long way.'"
            };
        } else if (lname.contains("philosopher")) {
            lines = new String[] {
                "Philosopher: 'Even rubble has lessons.'",
                "Philosopher: 'Stillness sharpens the mind.'",
                "Philosopher: 'Breathe. Observe. Strike.'"
            };
        } else if (lname.contains("codechum")) {
            lines = new String[] {
                "CodeChum: 'Refactoring your focus.'",
                "CodeChum: 'Debugging your stance.'",
                "CodeChum: 'Optimizing threat response.'"
            };
        } else if (lname.contains("kuya kim")) {
            lines = new String[] {
                "Kuya Kim: 'Winds shifting — watch for gusts.'",
                "Kuya Kim: 'I see the storm path. Adjust now.'",
                "Kuya Kim: 'Humidity spike. Brace yourself.'"
            };
        } else if (lname.contains("topnotcher")) {
            lines = new String[] {
                "Topnotcher: 'Calibrate your breathing.'",
                "Topnotcher: 'Stability unlocks precision.'",
                "Topnotcher: 'I tuned your core systems.'"
            };
        } else {
            lines = new String[] {
                supporter.getName() + ": 'I'm here if you need me.'",
                supporter.getName() + ": 'Standing by.'",
                supporter.getName() + ": 'I've got your back.'"
            };
        }
        TextEffect.typeWriter(lines[rand.nextInt(lines.length)], 25);
    }

    private void applyIntBuff(Player player, SupporterCombatState st, int amount, int duration) {
        if (st.intBuffAmount != 0) {
            player.addTemporaryIntelligence(-st.intBuffAmount);
        }
        st.intBuffAmount = amount;
        st.intBuffRemaining = duration;
        player.addTemporaryIntelligence(amount);
    }

    private void applyDefBuff(Player player, SupporterCombatState st, int amount, int duration) {
        if (st.defBuffAmount > 0 && player.getDefense() >= st.defBuffAmount) {
            player.addTemporaryDefense(-st.defBuffAmount);
        }
        st.defBuffAmount = amount;
        st.defBuffRemaining = duration;
        player.addTemporaryDefense(amount);
    }

    public double getCritBonus(GameState state) {
        Supporter active = getEquippedSupporter(state);
        if (active == null) return 0;
        SupporterCombatState st = combatState.get(active);
        if (st == null || st.critBuffRemaining <= 0) return 0;
        return st.critBonus;
    }

    public int modifyIncomingDamage(GameState state, int incomingDamage, boolean specialAttack) {
        Supporter active = getEquippedSupporter(state);
        if (active == null) return incomingDamage;
        SupporterCombatState st = combatState.get(active);
        if (st == null) return incomingDamage;

        int dmg = incomingDamage;
        if (st.blockNextAttack) {
            dmg = (int)Math.ceil(dmg * 0.5);
            TextEffect.typeWriter(active.getName() + " intercepts the blow! Damage reduced by 50%.", 40);
            st.blockNextAttack = false;
        }
        if (specialAttack && st.dampenNextSpecial) {
            dmg = (int)Math.ceil(dmg * 0.6);
            TextEffect.typeWriter(active.getName() + " bends the winds — special attack reduced by 40%.", 40);
            st.dampenNextSpecial = false;
        }
        return Math.max(0, dmg);
    }

    private SupporterCombatState stateFor(Supporter supporter) {
        return combatState.computeIfAbsent(supporter, key -> new SupporterCombatState());
    }

    private Supporter getEquippedSupporter(GameState state) {
        if (state == null || state.supporters == null) return null;
        for (Supporter s : state.supporters) {
            if (s != null && s.isRevived() && s.isEquipped()) {
                return s;
            }
        }
        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase();
    }

    // Reset buffs and clear per-combat state
    public void resetAfterCombat(Player player) {
        try {
            player.resetIntelligence();
            player.resetDefense();
            combatState.clear();
        } catch (Exception e) {
            System.err.println("SupporterSystem.resetAfterCombat error -> " + e.getMessage());
        }
    }
}
