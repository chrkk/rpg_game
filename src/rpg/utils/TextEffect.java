package rpg.utils;

public class TextEffect {

public static void typeWriter(String text, int delayMillis) {
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
