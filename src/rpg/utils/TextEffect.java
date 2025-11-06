package rpg.utils;

public class TextEffect {
    public static boolean fastMode = false; // 🆕 global toggle

    public static void typeWriter(String text, int delayMillis) {
        if (fastMode) {
            System.out.println(text);
            return;
        }
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
