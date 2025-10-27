package rpg.game;

import java.util.Scanner;
import java.util.Random;
import rpg.characters.Player;
import rpg.utils.TextEffect;
import rpg.systems.StatusSystem;
import rpg.items.Weapon;
import rpg.characters.Enemy;
import rpg.systems.TutorialCombatSystem;

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
        TextEffect.typeWriter("[Narrator] You feel weak. You need food and a weapon.", 80);
        TextEffect.typeWriter("Objective: Find food + find a weapon.", 80);

        boolean hasCrystal = false;
        boolean hasPencil = false;
        boolean awake = true;

        while (awake) {
            System.out.print("> What will you do? (craft / search / status / move): ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "search":
                    if (!hasCrystal && !hasPencil) {
                        TextEffect.typeWriter("You search the ruined classroom...", 60);
                        TextEffect.typeWriter("You found: 1 Crystal Shard and a Pencil.", 60);
                        hasCrystal = true;
                        hasPencil = true;
                        state.crystals += 1;
                    } else {
                        TextEffect.typeWriter("You already searched here. Nothing else useful.", 60);
                    }
                    break;

                case "craft":
                    if (hasCrystal && hasPencil && player.getWeapon() == null) {
                        state.crystals -= 1;
                        player.equipWeapon(new Weapon("Pencil Blade", 10));
                        TextEffect.typeWriter("You combined a Pencil and a Crystal Shard into a Pencil Blade!", 60);
                    } else {
                        TextEffect.typeWriter("You can’t craft anything new right now.", 60);
                    }
                    break;

                case "move":
                    if (player.getWeapon() == null) {
                        TextEffect.typeWriter("You can’t leave yet. You need a weapon first.", 60);
                    } else {
                        TextEffect.typeWriter("Armed with your " + player.getWeapon().getName() +
                                ", you step forward into the unknown...", 80);

                        // --- First scripted tutorial fight ---
                        Enemy tutorialEnemy = new Enemy("Wild Beast", 20, 5, 8, 15); // weaker stats for tutorial
                        TutorialCombatSystem tutorialCombat = new TutorialCombatSystem();
                        boolean win = tutorialCombat.startTutorialCombat(player, tutorialEnemy);

                        if (win) {
                            awake = false;
                            GameLoop loop = new GameLoop(player, state, scanner, rand);
                            loop.start();
                        }
                    }
                    break;

                case "status":
                    StatusSystem.showStatus(player, state.crystals, state.meat);
                    break;

                default:
                    TextEffect.typeWriter("Unknown command. Try craft / search / status / move.", 40);
            }
        }
    }
}
