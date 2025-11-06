package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;

public interface SafeZone {
    void enter(Player player, GameState state, Scanner scanner);
}
