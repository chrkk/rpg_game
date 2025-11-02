package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.systems.CraftingSystem;
import rpg.systems.ExplorationSystem;
import rpg.systems.StatusSystem;
import rpg.systems.ShopSystem; // ✅ import shop
import rpg.utils.TextEffect;

public class GameLoop {
    private final Player player;
    private final GameState state;
    private final Scanner scanner;
    private final Random rand;

    public GameLoop(Player player, GameState state, Scanner scanner, Random rand) {
        this.player = player;
        this.state = state;
        this.scanner = scanner;
        this.rand = rand;
    }

    public void start() {
        boolean running = true;

        while (running) {
            // 🆕 Dynamic prompt
            if (state.inSafeZone) {
                // ✅ Add shop option if zone > 1 (after defeating Zone 1 boss)
                if (state.zone > 1) {
                    System.out.print("> (craft / search / status / shop / move): ");
                } else {
                    System.out.print("> (craft / search / status / move): ");
                }
            } else {
                System.out.print("> (search / status / move): ");
            }

            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "craft":
                    if (state.inSafeZone) {
                        state.crystals = CraftingSystem.craftWeapon(player, state.crystals, state);
                    } else {
                        TextEffect.typeWriter("⚒️ You can only craft while inside a Safe Zone.", 50);
                    }
                    break;

                case "search":
                    if (state.inSafeZone) {
                        rpg.systems.SafeZoneSystem.searchSafeZone(player, state);
                    } else {
                        rpg.systems.SearchSystem.search(state);
                    }
                    break;

                case "status":
                    StatusSystem.showStatus(player, state.meat, state.shards);
                    break;

                case "shop":
                    if (state.inSafeZone && state.zone > 1) {
                        ShopSystem.openShop(state, scanner);
                    } else {
                        TextEffect.typeWriter("The shop is not available yet.", 50);
                    }
                    break;

                case "move":
                    ExplorationSystem.handleMove(
                            player, scanner, rand, state,
                            () -> rpg.systems.SafeZoneSystem.enterSafeZone(player, state, scanner));
                    break;

                default:
                    TextEffect.typeWriter("Unknown command.", 40);
            }
        }
    }
}
