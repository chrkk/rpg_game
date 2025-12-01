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
            TextEffect.typeWriter("[System] > Checkpoint Reached: City Ruins Shelter.", 60);
            TextEffect.typeWriter("The ground here is stable... for now. Stats restored.", 60);
        }
    }
    
    private void firstTimeEntry(Player player, GameState state, Scanner scanner) {
        TextEffect.typeWriter("\n[System] > Entering Sector: CITY_RUINS.", 70);
        TextEffect.typeWriter("The streets are buckled and cracked. Buildings lean at dangerous angles.", 70);
        TextEffect.typeWriter("It looks like a massive earthquake hit this place minutes ago.", 70);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\nYou spot a makeshift shelter reinforced with rebar.", 70);
        TextEffect.typeWriter("Someone is hiding inside.", 70);
        
        // Meet the Scavenger
        TextEffect.typeWriter("\n[???] \"Don't move! The ground is sensitive!\"", 60);
        TextEffect.typeWriter("A figure emerges, testing the floor before stepping.", 60);
        TextEffect.typeWriter("[Random Person] \"You're not a statue? Good. Walk light.\"", 60);
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\n[Random Person] \"Name's Raze. I've been trying to map the fault lines.\"", 60);
        TextEffect.typeWriter("\"You feel those tremors? That's not nature. That's HIM.\"", 60);
        
        TextEffect.typeWriter("\n[Auralim] \"Yanig, the Earth-Shaker. He's blocking the path to the Source.\"", 70);
        TextEffect.typeWriter("\"He's made of the very concrete that crushed this city.\"", 70);
        TextEffect.typeWriter("\"You'll need something heavy to break him.\"", 70);
        
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Offer info/help
        TextEffect.typeWriter("\n[Auralim] \"The System told me you were coming.\"", 60);
        TextEffect.typeWriter("\"It said you're the only one who can stabilize the simulation.\"", 70);
        
        TextEffect.typeWriter("\nAuralim tosses you a worn map.", 60);
        TextEffect.typeWriter("[Auralim] \"Take this shelter. If the shaking starts again... hide.\"", 60);
        
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        TextEffect.typeWriter("\nAuralim disappears into the ruins.", 60);
        TextEffect.typeWriter("[System] > Stats Fully Restored.", 60);
        
        // Optional: Give small reward for reaching this far
        TextEffect.typeWriter("\n✨ [System] > Supplies Recovered: +2 Crystals, +1 Revival Potion", 60);
        state.crystals += 2;
        state.revivalPotions += 1;

        TextEffect.typeWriter("\n[Tamago] \"Psst! Over here!\"", 60);
        TextEffect.typeWriter("[Tamago] \"I found a spot that isn't shaking. Got new blueprints for you.\"", 60);
    }
}