package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.systems.StatusSystem;
import rpg.items.Weapon;
import rpg.characters.Enemy;
import rpg.systems.TutorialCombatSystem;

// new imports for meat consumable
import rpg.items.Consumable;
import rpg.game.GameState;

public class Tutorial {
    private final Scanner scanner;
    private final Random rand;
    private final GameState state;
    private final Player player;

    public Tutorial(Player player, GameState state, Scanner scanner, Random rand) {
        this.player = player;
        this.state = state;
        this.scanner = scanner;
        this.rand = rand;
    }

    public void start() {
        TextEffect.typeWriter("🏫 [Narrator] You awaken on the School Rooftop — your first Safe Zone.", 80);
        TextEffect.typeWriter("The world below is silent, statues of your classmates frozen in time.", 80);
        TextEffect.typeWriter("You feel weak. You need food and a weapon.", 80);
        TextEffect.typeWriter("Objective: Find food + find a weapon.", 80);

        TextEffect.typeWriter("[Narrator] Remember: crafting can only be done while inside a Safe Zone.", 80);

        boolean hasCrystal = false;
        boolean hasPencil = false;
        boolean awake = true;

        while (awake) {
            try {
                System.out.print("> What will you do? (craft / search / status / move): ");
                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "search":
                        try {
                            if (!hasCrystal && !hasPencil) {
                                TextEffect.typeWriter("You search the school rooftop...", 60);
                                TextEffect.typeWriter("You found: 1 Crystal, 1 Crystal Shard, and a Pencil.", 60);

                                TextEffect.typeWriter(
                                        "[Narrator] Crystals are rare crafting materials used to forge weapons.", 70);
                                TextEffect.typeWriter(
                                        "[Narrator] Crystal Shards, on the other hand, are a form of currency. You'll be able to spend them in shops which unlocks further.",
                                        70);

                                hasCrystal = true;
                                hasPencil = true;

                                state.crystals += 1;
                                state.shards += 1;
                            } else {
                                TextEffect.typeWriter("You already searched here. Nothing else useful.", 60);
                            }
                        } catch (Exception e) {
                            TextEffect.typeWriter("Something went wrong while searching.", 40);
                            System.err.println("Search error -> " + e.getMessage());
                        }
                        break;

                    case "craft":
                        try {
                            if (hasCrystal && hasPencil && player.getWeapon() == null) {
                                state.crystals -= 1;
                                player.equipWeapon(new Weapon("Pencil Blade", 8, 12, 0.05, 1.5));
                                TextEffect.typeWriter("You combined a Pencil and a Crystal into a Pencil Blade!", 60);
                                state.stage1WeaponCrafted = true;
                            } else {
                                TextEffect.typeWriter("You can’t craft anything new right now.", 60);
                            }
                        } catch (Exception e) {
                            TextEffect.typeWriter("Crafting failed unexpectedly.", 40);
                            System.err.println("Crafting error -> " + e.getMessage());
                        }
                        break;

                    case "move":
                        try {
                            if (player.getWeapon() == null) {
                                TextEffect.typeWriter("You can’t leave yet. You need a weapon first.", 60);
                            } else {

                                TextEffect.typeWriter("🌌 You leave the safety of the School Rooftop behind...", 80);
                                TextEffect.typeWriter(
                                        "The path ahead winds through shattered classrooms and broken halls.", 80);
                                TextEffect.typeWriter(
                                        "🎯 Goal: Continue forward and get stronger until you face the Fractured Logo to unlock the Ruined Lab.",
                                        80);
                                state.forwardSteps++;

                                TutorialCombatSystem tutorialCombat = new TutorialCombatSystem(state);
                                Enemy tutorialEnemy = new Enemy("Wild Beast", 20, 5, 8, 15);

                                boolean win = tutorialCombat.startTutorialCombat(player, tutorialEnemy);

                                if (win) {
                                    TextEffect.typeWriter("✅ You overcame the Wild Beast. The path forward is clear.",
                                            80);
                                    TextEffect.typeWriter("⚔️ Remember: the Wild Beast was only an obstacle.", 80);
                                    TextEffect.typeWriter(
                                            "🎯 Your true goal is to keep moving forward get stronger until you face the Fractured Logo.",
                                            80);
                                    TextEffect.typeWriter(
                                            "Defeat it to unlock the next destination — the Ruined Lab.", 80);

                                    awake = false;
                                    GameLoop loop = new GameLoop(player, state, scanner, rand);
                                    loop.start();
                                }
                            }
                        } catch (Exception e) {
                            TextEffect.typeWriter("Something went wrong while trying to move.", 40);
                            System.err.println("Move error -> " + e.getMessage());
                        }
                        break;

                    case "status":
                        try {
                            StatusSystem.showStatus(player, state);
                            
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to display status right now.", 40);
                            System.err.println("Status error -> " + e.getMessage());
                        }
                        break;

                    case "bag":
                        try {
                            StatusSystem.showBag(state);
                        } catch (Exception e) {
                            TextEffect.typeWriter("Unable to display bag right now.", 40);
                            System.err.println("Status error -> " + e.getMessage());
                        }
                        break;

                    default:
                        TextEffect.typeWriter("Unknown command. Try craft / search / status / move.", 40);
                }

            } catch (Exception e) {
                TextEffect.typeWriter("An unexpected error occurred during the tutorial.", 40);
                System.err.println("Tutorial loop error -> " + e.getMessage());
            } finally {
                // Runs every loop iteration — could be used for logging or state checks
            }
        }
    }
}
