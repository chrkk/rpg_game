package rpg.utils;

import rpg.game.GameState; // assuming you have a central state

public class TextEffect {

    public static void typeWriter(String text, int delayMillis) {
        // 🆕 Fast mode check
        if (GameState.current != null && GameState.current.fastMode) {
            System.out.println(text);
            return;
        }

        // Normal typewriter effect
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }
}
