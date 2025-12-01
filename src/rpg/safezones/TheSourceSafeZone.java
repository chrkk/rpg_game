package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class TheSourceSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;

        TextEffect.typeWriter("\n[System] > Entering Sector: THE_CHOICE.", 60);
        TextEffect.typeWriter("You stand at the edge of the simulation. The digital sky dissolves into pure white code.", 60);
        TextEffect.typeWriter("The hum of servers and the smell of ozone fill the air.", 60);

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 🗣️ NPC Interaction
        TextEffect.typeWriter("\nA familiar figure leans against a glitching wall of code. It's Tamago.", 60);
        TextEffect.typeWriter("He looks tired, but he's smiling.", 60);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        TextEffect.typeWriter("\n[Tamago] \"You made it. I knew you would.\"", 60);
        TextEffect.typeWriter("[Tamago] \"You've gathered powerful weapons.\"", 60);
        TextEffect.typeWriter("[Tamago] \"But it does not matter\"", 60);
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 🌟 THE ADVICE LINE
        TextEffect.typeWriter("\n[Tamago] \"Trust me, I've been exactly where you are now.\"", 70);
        TextEffect.typeWriter("[Tamago] \"You don't need a new weapon for this last one.\"", 70);
        TextEffect.typeWriter("[Tamago] \"All you need is YOU... and to CHOOSE.\"", 80);

        TextEffect.typeWriter("\n[System] > Vital Signs Stabilized. HP and Mana Fully Restored.", 60);
        TextEffect.typeWriter("[System] > Warning: You can only save your self", 60);
        TextEffect.typeWriter("There is no turning back now. Only the final decision remains.", 70);
    }
}