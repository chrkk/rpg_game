package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.game.GameState;
import rpg.systems.ShopSystem;
import rpg.utils.TextEffect;

public class ObservationDeckSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;

        if (!state.zone4IntroShown) {
            firstTimeEntry(state, scanner);
            state.zone4IntroShown = true;
        } else {
            TextEffect.typeWriter("You step back onto the Observation Deck as the wind howls between broken beams.", 60);
            TextEffect.typeWriter("Lightning flickers across the clouds, but within the deck you feel a fragile calm.", 60);
            TextEffect.typeWriter("Your stats have been restored.", 60);
        }
    }

    private void firstTimeEntry(GameState state, Scanner scanner) {
        TextEffect.typeWriter("\n🌩️ You emerge onto the shattered Observation Deck...", 70);
        TextEffect.typeWriter("Glassless windows open to the storm. The sky bridge groans beneath distant thunder.", 70);
        TextEffect.typeWriter("Far above, the pillar of light — The Source — pierces the clouds. You are closer than ever.", 70);

        pause(800);

        TextEffect.typeWriter("You pace across cracked tiles, half suspended over open air.", 70);
        TextEffect.typeWriter("Every gust of wind tastes like static. The ascent has only just begun.", 70);

        pause(600);

        TextEffect.typeWriter("At the deck's center, emergency generators still pulse with residual energy.", 70);
        TextEffect.typeWriter("You salvage conductive coils and spend time steadying your breath.", 70);
        TextEffect.typeWriter("✨ Supplies recovered: +3 Crystals", 60);
        state.crystals += 3;

        pause(600);

        TextEffect.typeWriter("A familiar rasp echoes from a maintenance alcove.", 60);
        TextEffect.typeWriter("[Scrapwright Kuro] 'Told you business would keep following you upward.'", 60);
        TextEffect.typeWriter("[Kuro] 'New storms, new toys. I've bottled the lightning for a spear you'd better learn to wield.'", 60);
        TextEffect.typeWriter("Kuro has set up a compact shop here. The Thunder Spear blueprint (25 shards) is now for sale.", 60);

        System.out.print("Browse Kuro's wares now? (yes/no): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("yes")) {
            ShopSystem.openShop(state, scanner);
        } else {
            TextEffect.typeWriter("You postpone shopping and simply take in the view.", 60);
        }
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
