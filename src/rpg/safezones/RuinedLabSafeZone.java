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

        if (!state.zone2IntroShown) {
            // 📖 FIRST TIME ENTRY: Story Sequence
            TextEffect.typeWriter("\n[System] > Entering Sector: RUINED_LAB.", 60);
            TextEffect.typeWriter("The air smells of burnt paper and destroyed PC's. Your stats have been restored.", 60);

            try { Thread.sleep(800); } catch (InterruptedException e) {}

            TextEffect.typeWriter("\n[System] > Decrypting Audio Log...", 60);
            TextEffect.typeWriter("[The Dean!] \"We pushed them too hard... The pressure... it broke them.\"", 60);
            
            TextEffect.typeWriter("\n[System] > Warning: The Guardian ahead is a manifestation of Academic Failure.", 60);
            TextEffect.typeWriter("[System] > Threat Identified: The Thesis Defense Panel.", 60);
            TextEffect.typeWriter("Only forged weapons can pierce the density of their critique.", 60);

            try { Thread.sleep(800); } catch (InterruptedException e) {}

            TextEffect.typeWriter("\nA scavenger emerges from the shadows of broken machinery.", 60);
            TextEffect.typeWriter("[Tamago] \"Name's Tamago. I've been trying to 'graduate', it feels like years.\"", 60);
            TextEffect.typeWriter("[Tamago] \"To beat the Panel, you need the Logic Blade. I have the blueprint.\"", 60);
            TextEffect.typeWriter("[Tamago] \"I deal in blueprints and scraps. Even in a simulation, you gotta pay to win.\"", 60);

            state.shopUnlocked = true;
            state.zone2IntroShown = true;

            System.out.print("Do you want to browse the shop now? (yes/no): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                ShopSystem.openShop(state, scanner);
            }
        } else {
            // 🔄 RECURRING ENTRY: Short Checkpoint
            TextEffect.typeWriter("[System] > Checkpoint Reached: Ruined Lab.", 60);
            TextEffect.typeWriter("Tamago is tinkering with a broken motherboard in the corner. Stats restored.", 60);
        }
    }
}