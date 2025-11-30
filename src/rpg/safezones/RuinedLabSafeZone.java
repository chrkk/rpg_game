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
        TextEffect.typeWriter("\n[System] > Entering Sector: RUINED_LAB.", 60);
        TextEffect.typeWriter("The air smells of burnt paper and destroyed PC's. Your stats have been restored.", 60);

        if (!state.zone2IntroShown) {
            TextEffect.typeWriter("\n[Memory Echo - Prof. Ashiro] \"We pushed them too hard... The pressure... it broke them.\"", 60);
            TextEffect.typeWriter("[System] > Warning: The Guardian ahead is a manifestation of Academic Failure.", 60);
            TextEffect.typeWriter("Only forged weapons can pierce the density of their critique.", 60);

            TextEffect.typeWriter("\nA scavenger emerges from the shadows of broken machinery.", 60);
            TextEffect.typeWriter("[Scrapwright Kuro] \"Heh, the System glitching out on you too?\"", 60);
            TextEffect.typeWriter("[Kuro] \"I deal in blueprints and scraps. Even in a simulation, you gotta pay to win.\"", 60);

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