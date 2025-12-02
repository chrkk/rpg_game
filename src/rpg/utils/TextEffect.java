package rpg.utils;

import java.io.IOException;

public class TextEffect {
    public static boolean fastMode = false; // 🆕 global toggle
    private static volatile boolean skipLine = false;

    public static void typeWriter(String text, int delayMillis) {
        if (fastMode) {
            System.out.println(text);
            return;
        }
        skipLine = false;
        char[] chars = text.toCharArray();
        int effectiveDelay = Math.max(5, delayMillis / 2);
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            char current = chars[i];
            System.out.print(current);
            System.out.flush();

            if (current == '\n' || current == '\r') {
                currentLine.setLength(0);
            } else {
                currentLine.append(current);
            }

            if (skipLine || checkSkipRequested()) {
                flushRemainder(chars, i, currentLine);
                break;
            }

            try {
                Thread.sleep(effectiveDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println();
    }

    private static void flushRemainder(char[] chars, int currentIndex, StringBuilder currentLine) {
        rewindToCurrentLine(currentLine.length());
        if (currentLine.length() > 0) {
            System.out.print(currentLine);
        }
        for (int j = currentIndex + 1; j < chars.length; j++) {
            System.out.print(chars[j]);
        }
    }

    private static void rewindToCurrentLine(int lineLength) {
        if (lineLength <= 0) {
            // Still clear the blank line introduced by Enter
            System.out.print("\u001B[1A\r\u001B[2K");
            return;
        }
        System.out.print("\u001B[1A\r\u001B[2K");
    }

    private static boolean checkSkipRequested() {
        try {
            while (System.in.available() > 0) {
                int ch = System.in.read();
                if (ch == '\n' || ch == '\r') {
                    skipLine = true;
                }
            }
        } catch (IOException ignored) {
            // Non-critical; simply fall back to normal behavior
        }
        return skipLine;
    }
}
