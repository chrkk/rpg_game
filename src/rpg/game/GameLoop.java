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
            try {
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
                            try {
                                state.crystals = CraftingSystem.craftWeapon(player, state.crystals, state);
                            } catch (Exception e) {
                                TextEffect.typeWriter("Crafting failed. Try again.", 40);
                                System.err.println("Crafting error -> " + e.getMessage());
                            }
                        } else {
                            TextEffect.typeWriter("⚒️ You can only craft while inside a Safe Zone.", 50);
                        }
                        break;

                    case "search":
                        try {
                            if (state.inSafeZone) {
                                rpg.systems.SafeZoneSystem.searchSafeZone(player, state);
                            } else {
                                rpg.systems.SearchSystem.search(state);
                            }
                        } catch (Exception e) {
                            TextEffect.typeWriter("Search failed. Something feels off...", 40);
                            System.err.println("Search error -> " + e.getMessage());
                        }
                        break;

                    case "status":
                        try {
                            StatusSystem.showStatus(player, state.meat, state.shards);
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to display status right now.", 40);
                            System.err.println("Status error -> " + e.getMessage());
                        }
                        break;

                    case "shop":
                        if (state.inSafeZone && state.zone > 1) {
                            try {
                                ShopSystem.openShop(state, scanner);
                            } catch (Exception e) {
                                TextEffect.typeWriter("The shopkeeper seems confused... Try again later.", 40);
                                System.err.println("Shop error -> " + e.getMessage());
                            }
                        } else {
                            TextEffect.typeWriter("The shop is not available yet.", 50);
                        }
                        break;

                    case "move":
                        try {
                            ExplorationSystem.handleMove(
                                    player, scanner, rand, state,
                                    () -> rpg.systems.SafeZoneSystem.enterSafeZone(player, state, scanner));
                        } catch (Exception e) {
                            TextEffect.typeWriter("You stumble and fail to move properly.", 40);
                            System.err.println("Move error -> " + e.getMessage());
                        }
                        break;

                    case "exit":
                        TextEffect.typeWriter("Exiting game... Goodbye!", 40);
                        running = false;
                        break;

                    default:
                        TextEffect.typeWriter("Unknown command.", 40);
                }

            } catch (Exception e) {
                // 🌐 Global catch for unexpected runtime errors
                TextEffect.typeWriter("An unexpected error occurred. Please try again.", 40);
                System.err.println("GameLoop error -> " + e.getMessage());
            } finally {
                // This runs every loop iteration (good place for logging or state checks)
            }
        }
    }
}
