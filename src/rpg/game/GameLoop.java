package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.systems.CraftingSystem;
import rpg.systems.ExplorationSystem;
import rpg.systems.StatusSystem;
import rpg.systems.ShopSystem;
import rpg.utils.TextEffect;
// import rpg.systems.BagSystem; --> deleted

import rpg.systems.SafeZoneSystem;
import rpg.ui.UIDesign; 

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
                if (state.inSafeZone) {
                    if (!state.safeZoneMenuShown) {
                        SafeZoneSystem.displaySafeZoneMenu(state);
                        state.safeZoneMenuShown = true;
                        System.out.print("> ");
                    } else {
                        System.out.print("> (craft / search / status / bag / shop / supporter / move): ");
                    }
                    
                } else {
                    state.safeZoneMenuShown = false;
                    System.out.print("> (search / status / bag / move): ");
                }

                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "craft":
                        if (state.inSafeZone) {
                            try {
                                // 🆕 UPDATED: Display only the weapon relevant to the CURRENT ZONE
                                String targetWeapon = "";
                                if (state.zone == 1) targetWeapon = "Pencil Blade";
                                else if (state.zone == 2) targetWeapon = "Logic Blade";
                                else if (state.zone == 3) targetWeapon = "Aftershock Hammer";
                                else if (state.zone == 4) targetWeapon = "Trident of Storms";
                                else targetWeapon = "Unknown";

                                if (targetWeapon.equals("Unknown")) {
                                    TextEffect.typeWriter("No crafting available in this zone.", 40);
                                    break;
                                }

                                // Check status for display
                                boolean hasBlueprint = state.unlockedRecipes.contains(targetWeapon) || targetWeapon.equals("Pencil Blade");
                                boolean hasRecipe = state.recipeItems.getOrDefault(targetWeapon, false);
                                
                                String statusTag;
                                if (!hasBlueprint) statusTag = " [Blueprint Required]";
                                else if (!hasRecipe) statusTag = " [Recipe Missing]";
                                else statusTag = " [Ready to Craft]";

                                TextEffect.typeWriter("⚒️  Zone " + state.zone + " Forge:", 50);
                                TextEffect.typeWriter("1. " + targetWeapon + statusTag, 40);
                                TextEffect.typeWriter("0. Cancel", 40);

                                System.out.print("> Choose a recipe number: ");
                                String choice = scanner.nextLine();

                                try {
                                    int option = Integer.parseInt(choice);
                                    if (option == 0) {
                                        TextEffect.typeWriter("Crafting cancelled.", 40);
                                    } else if (option == 1) {
                                        // Try to craft the target weapon
                                        state.crystals = CraftingSystem.craftWeapon(player, state.crystals, state, targetWeapon);
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
                            if (state.inSafeZone && !state.supporters.isEmpty()) {
                                boolean anyEquipped = false;
                                for (rpg.characters.Supporter s : state.supporters) {
                                    if (s.isEquipped()) { anyEquipped = true; break; }
                                }
                                if (!anyEquipped) {
                                    TextEffect.typeWriter("You must equip at least one supporter before venturing out.", 50);
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
                TextEffect.typeWriter("An unexpected error occurred. Please try again.", 40);
                System.err.println("GameLoop error -> " + e.getMessage());
            } finally {
            }
        }
    }
}