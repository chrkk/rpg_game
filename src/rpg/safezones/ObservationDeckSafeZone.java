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
            TextEffect.typeWriter("[System] > Checkpoint Reached: Observation Deck.", 60);
            TextEffect.typeWriter("The glass rattles from the wind outside, but it holds. Stats restored.", 60);
        }
    }

    private void firstTimeEntry(GameState state, Scanner scanner) {
        TextEffect.typeWriter("\n🌩️ [System] > Entering Sector: FRACTURED_SKY.", 70);
        TextEffect.typeWriter("Rain lashes against the glassless windows. The wind howls like a living thing.", 70);
        TextEffect.typeWriter("[System] > Weather Alert: Typhoon Signal No. 5 in effect.", 70);

        pause(800);

        TextEffect.typeWriter("You pace across cracked tiles, high above the clouds.", 70);
        TextEffect.typeWriter("Far above, The Source is barely visible through the swirling eye of the storm.", 70);

        pause(600);

        TextEffect.typeWriter("At the deck's center, emergency generators flicker.", 70);
        TextEffect.typeWriter("You salvage conductive coils to prepare for the storm ahead.", 70);
        TextEffect.typeWriter("✨ [System] > Supplies Recovered: +3 Crystals", 60);
        state.crystals += 3;

        pause(600);

        TextEffect.typeWriter("A familiar rasp echoes from a maintenance alcove.", 60);
        TextEffect.typeWriter("[Kuro] 'Bit wet up here, isn't it?'", 60);
        TextEffect.typeWriter("[Kuro] 'That thing outside... Bagyong Tino. It's not just a storm.'", 60);
        TextEffect.typeWriter("[Kuro] 'It's pure rage. You'll need the Trident of Storms to pierce the eye.'", 60);
        TextEffect.typeWriter("Kuro has set up a compact shop here. The Trident of Storms blueprint is now for sale.", 60);

        System.out.print("Browse Kuro's wares now? (yes/no): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("yes")) {
            ShopSystem.openShop(state, scanner);
        } else {
            TextEffect.typeWriter("You postpone shopping and brace yourself against the wind.", 60);
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