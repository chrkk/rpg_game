package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.systems.CraftingSystem;
import rpg.systems.ExplorationSystem;
import rpg.systems.StatusSystem;
import rpg.systems.ShopSystem;
import rpg.utils.TextEffect;
import java.util.List;
import java.util.ArrayList;

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
                // 🆕 Dynamic prompt - now includes "bag" and "supporter"
                if (state.inSafeZone) {
                    // ✅ Add shop option if zone > 1 (after defeating Zone 1 boss)
                    if (state.zone > 1) {
                        System.out.print("> (craft / search / status / bag / supporter / shop / move): ");
                    } else {
                        System.out.print("> (craft / search / status / bag / supporter / move): ");
                    }
                } else {
                    System.out.print("> (search / status / bag / move): ");
                }

                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "craft":
                        if (state.inSafeZone) {
                            try {
                                // Crafting menu
                                if (state.unlockedRecipes.isEmpty()) {
                                    TextEffect.typeWriter("You don't know any recipes yet.", 50);
                                    break;
                                }

                                TextEffect.typeWriter("⚒️ Available recipes:", 50);
                                List<String> recipes = new ArrayList<>(state.unlockedRecipes); // convert Set to List
                                for (int i = 0; i < recipes.size(); i++) {
                                    String recipe = recipes.get(i);
                                    boolean discovered = state.recipeItems.getOrDefault(recipe, false);
                                    TextEffect.typeWriter(
                                            (i + 1) + ". " + recipe + (discovered ? " (discovered)" : " (not found)"),
                                            40);
                                }
                                TextEffect.typeWriter("0. Cancel", 40);

                                System.out.print("> Choose a recipe number: ");
                                String choice = scanner.nextLine();

                                try {
                                    int option = Integer.parseInt(choice);
                                    if (option == 0) {
                                        TextEffect.typeWriter("Crafting cancelled.", 40);
                                    } else if (option > 0 && option <= recipes.size()) {
                                        String target = recipes.get(option - 1);
                                        state.crystals = CraftingSystem.craftWeapon(player, state.crystals, state,
                                                target);
                                    } else {
                                        TextEffect.typeWriter("Invalid choice.", 40);
                                    }
                                } catch (NumberFormatException e) {
                                    TextEffect.typeWriter("Invalid input. Please enter a number.", 40);
                                }

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
                            StatusSystem.showStatus(player, state);
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to display status right now.", 40);
                            System.err.println("Status error -> " + e.getMessage());
                        }
                        break;

                    // 🆕 NEW: Bag command
                    case "bag":
                        try {
                            StatusSystem.showBag(state);
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to open your bag right now.", 40);
                            System.err.println("Bag error -> " + e.getMessage());
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

                    case "supporter":
                        try {
                            rpg.systems.SafeZoneSystem.openSupporterMenu(player, state, scanner);
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to open the supporter menu right now.", 40);
                            System.err.println("Supporter menu error -> " + e.getMessage());
                        }
                        break;

                    case "move":
                        try {
                            // Enforce at-least-one-equipped supporter only if the player has supporters
                            if (state.inSafeZone && !state.supporters.isEmpty()) {
                                boolean anyEquipped = false;
                                for (rpg.characters.Supporter s : state.supporters) {
                                    if (s.isEquipped()) { anyEquipped = true; break; }
                                }
                                if (!anyEquipped) {
                                    TextEffect.typeWriter("You must equip at least one supporter before venturing out.", 50);
                                    // Open supporter menu to let player equip
                                    rpg.systems.SafeZoneSystem.openSupporterMenu(player, state, scanner);
                                    break;
                                }
                            }

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