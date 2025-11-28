package rpg.systems;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;
import rpg.safezones.SafeZone;
import rpg.safezones.SafeZoneFactory;
import rpg.utils.TextEffect;

import rpg.ui.UIDesign;

public class SafeZoneSystem {
    public static void enterSafeZone(Player player, GameState state, Scanner scanner) {

        // Normal entry logic
        SafeZone zone = SafeZoneFactory.getZone(state.zone);
        state.inSafeZone = true; // mark as inside
        zone.enter(player, state, scanner);

        displaySafeZoneMenu(state); //new
    }

     
    // 🆕 NEW: Display the safe zone hub menu with fancy UI
    public static void displaySafeZoneMenu(GameState state) {
        boolean shopUnlocked = state.zone > 1; // Shop unlocks in Zone 2+
        UIDesign.displaySafeZoneHub(state.zone, shopUnlocked);
    }

    public static void searchSafeZone(Player player, GameState state) {
        try {
            TextEffect.typeWriter("You found nothing, try moving out.", 60);
        } catch (Exception e) {
            TextEffect.typeWriter("You stumble while trying to search here.", 40);
            System.err.println("Safe zone search error -> " + e.getMessage());
        }
    }

    // Open supporter management menu (view and toggle equip)
    public static void openSupporterMenu(Player player, GameState state, Scanner scanner) {
        if (!state.inSafeZone) {
            TextEffect.typeWriter("You must be inside a Safe Zone to manage supporters.", 40);
            return;
        }

        // Show a one-time Stage 2 intro the first time the player opens the supporter menu in Stage 2
        try {
            if (state.zone == 2 && !state.supporterMenuIntroStage2Shown) {
                TextEffect.typeWriter("You notice weathered statues scattered outside the lab — echoes of people who once stood guard. You can awaken a statue using a Revival Potion (available from the shop for 6 shards).\nRevived Supporters follow you and lend short, practical aid in combat. After Sir Khai's arrival, more statues may appear while you explore. Equip one Supporter in a Safe Zone to enable their perk.", 60);
                state.supporterMenuIntroStage2Shown = true;
            }
        } catch (Exception e) {
            // non-fatal; continue to menu
        }

        if (state.supporters.isEmpty()) {
            TextEffect.typeWriter("You have no supporters to manage.", 40);
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                TextEffect.typeWriter("Supporter Menu:\n1) View / Toggle Supporters\n0) Exit", 30);
                System.out.print("> ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        for (int i = 0; i < state.supporters.size(); i++) {
                            rpg.characters.Supporter s = state.supporters.get(i);
                            TextEffect.typeWriter((i + 1) + ") " + s.toString(), 20);
                        }
                        TextEffect.typeWriter("0) Back", 20);
                        System.out.print("> Toggle number: ");
                        String sel = scanner.nextLine().trim();
                        try {
                            int idx = Integer.parseInt(sel);
                            if (idx == 0) break;
                            if (idx < 1 || idx > state.supporters.size()) {
                                TextEffect.typeWriter("Invalid supporter number.", 40);
                                break;
                            }
                            rpg.characters.Supporter s = state.supporters.get(idx - 1);
                            if (!s.isRevived()) {
                                TextEffect.typeWriter("That supporter is not revived yet.", 40);
                                break;
                            }
                            boolean newState = !s.isEquipped();
                            if (newState) {
                                // Unequip all others when equipping this one (single-equipped behavior)
                                for (rpg.characters.Supporter other : state.supporters) {
                                    if (other != null && other != s) other.setEquipped(false);
                                }
                            }
                            s.setEquipped(newState);
                            TextEffect.typeWriter((s.isEquipped() ? "Equipped " : "Unequipped ") + s.getName(), 40);
                        } catch (NumberFormatException nfe) {
                            TextEffect.typeWriter("Invalid input.", 40);
                        }
                        break;

                    case "0":
                        done = true;
                        break;

                    default:
                        TextEffect.typeWriter("Invalid option.", 40);
                }
            } catch (Exception e) {
                TextEffect.typeWriter("An error occurred in the supporter menu.", 40);
                System.err.println("SafeZoneSystem.supporterMenu error -> " + e.getMessage());
                done = true;
            }
        }
    }
}
