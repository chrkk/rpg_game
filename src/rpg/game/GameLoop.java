package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.systems.CraftingSystem;
import rpg.systems.ExplorationSystem;
import rpg.systems.SafeZoneSystem;
import rpg.systems.StatusSystem;
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
            System.out.print("> (craft / search / status / move): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "craft":
                    state.crystals = CraftingSystem.craftWeapon(player, state.crystals);
                    break;

                case "search":
                    rpg.systems.SafeZoneSystem.searchSafeZone(player, state);
                    break;

                case "status":
                    StatusSystem.showStatus(player, state.crystals, state.meat);
                    break;

                case "move":
                    ExplorationSystem.handleMove(player, scanner, rand, state,
                        () -> rpg.systems.SafeZoneSystem.enterSafeZone(player, state));
                    break;

                default:
                    TextEffect.typeWriter("Unknown command.", 40);
            }
        }
    }
}
