package rpg.safezones;

import java.util.Scanner;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.game.GameState;

public class CityRuinsSafeZone implements SafeZone {
    @Override
    public void enter(Player player, GameState state, Scanner scanner) {
        player.healFull();
        state.inSafeZone = true;
        
        // First-time entry event
        if (!state.zone3IntroShown) {
            firstTimeEntry(player, state, scanner);
            state.zone3IntroShown = true;
        } else {
            // Regular entry
            TextEffect.typeWriter("You return to the makeshift shelter in the City Ruins.", 60);
            TextEffect.typeWriter("The fire still crackles. Your stats have been restored.", 60);
        }
    }
    
    private void firstTimeEntry(Player player, GameState state, Scanner scanner) {
        TextEffect.typeWriter("\n🏙️ You step into what remains of the city center...", 70);
        TextEffect.typeWriter("Collapsed buildings line the streets like broken teeth.", 70);
        TextEffect.typeWriter("Smoke rises from scattered fires that never seem to die.", 70);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\nThen you see it—a makeshift shelter built from rubble.", 70);
        TextEffect.typeWriter("Someone has been living here.", 70);
        
        // Meet the Scavenger
        TextEffect.typeWriter("\n[???] \"Hold it right there.\"", 60);
        TextEffect.typeWriter("A figure emerges from the shadows, weapon drawn.", 60);
        TextEffect.typeWriter("[Scavenger] \"Another survivor? Or another statue waiting to happen?\"", 60);
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\nYou raise your hands slowly, showing you're human.", 60);
        TextEffect.typeWriter("The scavenger lowers their weapon, eyes scanning you carefully.", 60);
        
        TextEffect.typeWriter("\n[Scavenger] \"...You made it through the lab. Impressive.\"", 60);
        TextEffect.typeWriter("\"Name's Raze. I've been mapping the ruins, trying to find The Source.\"", 60);
        TextEffect.typeWriter("\"That pillar of light in the distance—it's where everything started.\"", 70);
        
        TextEffect.typeWriter("\n[Raze] \"But there's something guarding it. Something big.\"", 70);
        TextEffect.typeWriter("\"The Screaming Billboard. It corrupts minds, drives people mad.\"", 70);
        TextEffect.typeWriter("\"You'll need to defeat it before you can reach The Source.\"", 70);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Offer info/help
        TextEffect.typeWriter("\n[Raze] \"Listen... I've seen things. Learned things.\"", 60);
        TextEffect.typeWriter("\"The light didn't just petrify people—it's feeding on them somehow.\"", 70);
        TextEffect.typeWriter("\"Every statue is a battery. And you... you're the only one who can stop it.\"", 70);
        
        TextEffect.typeWriter("\nRaze tosses you a worn map.", 60);
        TextEffect.typeWriter("[Raze] \"This shelter is yours now. I'll keep searching for survivors.\"", 60);
        TextEffect.typeWriter("\"But I'll be watching your progress. Good luck out there.\"", 60);
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\nRaze disappears into the ruins, leaving you alone by the fire.", 60);
        TextEffect.typeWriter("Your stats have been fully restored.", 60);
        
        // Optional: Give small reward for reaching this far
        TextEffect.typeWriter("\n✨ You found supplies in the shelter: +2 Crystals, +1 Revival Potion", 60);
        state.crystals += 2;
        state.revivalPotions += 1;
    }
}