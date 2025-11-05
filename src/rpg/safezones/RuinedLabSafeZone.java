package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;
import rpg.systems.ShopSystem;

public class RuinedLabSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;
        TextEffect.typeWriter("You step cautiously into the Ruined Lab. The air smells of rust and chemicals.", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);

        if (!state.zone2IntroShown) {
            TextEffect.typeWriter("[Professor Ashiro] These halls once birthed discovery… now they breed only danger.", 60);
            TextEffect.typeWriter("The guardian ahead is no mere beast — only forged weapons can pierce its hide.", 60);

            TextEffect.typeWriter("\nA scavenger emerges from the shadows of broken machinery.", 60);
            TextEffect.typeWriter("[Scrapwright Kuro] Heh, I deal in blueprints and scraps of the old world.", 60);
            TextEffect.typeWriter("From now on, you can visit my shop in any Safe Zone.", 60);

            state.shopUnlocked = true;
            state.zone2IntroShown = true;

            System.out.print("Do you want to browse the shop now? (yes/no): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                ShopSystem.openShop(state, scanner);
            }
        }
    }
}
