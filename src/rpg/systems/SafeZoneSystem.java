package rpg.systems;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;
import rpg.safezones.SafeZone;
import rpg.safezones.SafeZoneFactory;
import rpg.utils.TextEffect;
import rpg.ui.UIDesign;

public class SafeZoneSystem {
    // Width for supporter list content so borders line up with the frame (60 chars total)
    private static final int SUPPORTER_INNER_WIDTH = 55;
    public static void enterSafeZone(Player player, GameState state, Scanner scanner) {
        SafeZone zone = SafeZoneFactory.getZone(state.zone);
        state.inSafeZone = true;
        state.safeZoneMenuShown = false;
        zone.enter(player, state, scanner);
    }

    public static void displaySafeZoneMenu(GameState state) {
        boolean shopUnlocked = state.zone > 1;
        if (state.zone == 1) {
            UIDesign.displaySafeZoneHubNoSupporter(state.zone, shopUnlocked);
        } else {
            UIDesign.displaySafeZoneHub(state.zone, shopUnlocked);
        }
    }

    public static void searchSafeZone(Player player, GameState state) {
        try {
            TextEffect.typeWriter("You found nothing, try moving out.", 60);
        } catch (Exception e) {
            TextEffect.typeWriter("You stumble while trying to search here.", 40);
            System.err.println("Safe zone search error -> " + e.getMessage());
        }
    }

    public static void openSupporterMenu(Player player, GameState state, Scanner scanner) {
        if (!state.inSafeZone) {
            TextEffect.typeWriter("You must be inside a Safe Zone to manage supporters.", 40);
            return;
        }

        // Show intro for Stage 2
        try {
            if (state.zone == 2 && !state.supporterMenuIntroStage2Shown) {
                TextEffect.typeWriter("You notice weathered statues scattered outside the lab — echoes of people who once stood guard. \n[SYSTEM] > You can awaken a statue using a Revival Potion (available from the shop for 6 shards).\n[SYSTEM] > Revived Supporters follow you and lend short, practical aid in combat.\n[SYSTEM] > After Sir Khai's arrival, more statues may appear while you explore.\n[SYSTEM] > Equip one Supporter in a Safe Zone to enable their perk.", 60);
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
                displaySupporterMenuUI(state);
                System.out.print("> ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        displaySupporterList(state);
                        System.out.print("> Toggle number (0 to cancel): ");
                        String sel = scanner.nextLine().trim();
                        try {
                            int idx = Integer.parseInt(sel);
                            if (idx == 0) break;
                            if (idx < 1 || idx > state.supporters.size()) {
                                TextEffect.typeWriter("Invalid supporter number.", 40);
                                break;
                            }
                            toggleSupporter(state, idx - 1);
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

    private static void displaySupporterMenuUI(GameState state) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                  👥  SUPPORTER MENU  👥                  ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║                                                          ║");
        System.out.println("║  [1] 👁️  View / Toggle Supporters                         ║");
        System.out.println("║  [0] 🚪 Exit                                             ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    private static void displaySupporterList(GameState state) {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        supporterLine(centerText("👥 SUPPORTER LIST 👥"));
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        supporterSpacer();

        for (int i = 0; i < state.supporters.size(); i++) {
            rpg.characters.Supporter supporter = state.supporters.get(i);
            String equippedIcon = supporter.isEquipped() ? "⭐" : "  ";
            String prefix = String.format("[%d] %s ", i + 1, equippedIcon);
            int remaining = Math.max(1, SUPPORTER_INNER_WIDTH - prefix.length());
            String details = truncate(supporter.getName() + " (" + supporter.getAbility() + ")", remaining);
            supporterLine(prefix + details);
        }

        supporterSpacer();
        supporterLine("Legend: ⭐ Equipped");
        supporterSpacer();
        System.out.println("╚══════════════════════════════════════════════════════════╝");
    }

    private static void supporterLine(String text) {
        if (text == null) {
            text = "";
        }
        String trimmed = truncateDisplayWidth(text, SUPPORTER_INNER_WIDTH);
        int displayWidth = getDisplayWidth(trimmed);
        StringBuilder sb = new StringBuilder(trimmed);
        while (displayWidth < SUPPORTER_INNER_WIDTH) {
            sb.append(' ');
            displayWidth++;
        }
        System.out.println("║  " + sb + " ║");
    }

    private static void supporterSpacer() {
        supporterLine("");
    }

    private static void toggleSupporter(GameState state, int index) {
        rpg.characters.Supporter s = state.supporters.get(index);
        if (!s.isRevived()) {
            TextEffect.typeWriter("That supporter is not revived yet.", 40);
            return;
        }
        
        boolean newState = !s.isEquipped();
        if (newState) {
            // Unequip all others
            for (rpg.characters.Supporter other : state.supporters) {
                if (other != null && other != s) other.setEquipped(false);
            }
        }
        s.setEquipped(newState);
        TextEffect.typeWriter((s.isEquipped() ? "✅ Equipped " : "❌ Unequipped ") + s.getName(), 40);
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (maxLength <= 0) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private static String centerText(String text) {
        if (text == null) {
            return "";
        }
        int displayWidth = getDisplayWidth(text);
        if (displayWidth >= SUPPORTER_INNER_WIDTH) {
            return truncateDisplayWidth(text, SUPPORTER_INNER_WIDTH);
        }
        int totalPadding = SUPPORTER_INNER_WIDTH - displayWidth;
        int left = totalPadding / 2;
        int right = totalPadding - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(text);
        for (int i = 0; i < right; i++) sb.append(' ');
        return sb.toString();
    }

    private static String truncateDisplayWidth(String text, int maxWidth) {
        if (text == null || maxWidth <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int width = 0;
        for (int i = 0; i < text.length();) {
            int codePoint = text.codePointAt(i);
            int glyphWidth = isWideGlyph(codePoint) ? 2 : 1;
            if (width + glyphWidth > maxWidth) {
                break;
            }
            sb.appendCodePoint(codePoint);
            width += glyphWidth;
            i += Character.charCount(codePoint);
        }
        return sb.toString();
    }

    private static int getDisplayWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        for (int i = 0; i < text.length();) {
            int codePoint = text.codePointAt(i);
            width += isWideGlyph(codePoint) ? 2 : 1;
            i += Character.charCount(codePoint);
        }
        return width;
    }

    private static boolean isWideGlyph(int codePoint) {
        return codePoint == 0x2B50 /* ⭐ */ || codePoint == 0x1F465 /* 👥 */;
    }
}