package MazeGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUDMessageManager {

    private static final int MESSAGE_LIFETIME_MS = 2000;

    private static final List<HUDMessage> messages = new ArrayList<>();
    private static JPanel panel;

    public static void init(JPanel gamePanel) {
        panel = gamePanel;
    }

    public static List<HUDMessage> getActiveMessages() {
        return List.copyOf(messages);
    }

    // ===== БАЗОВОЕ СООБЩЕНИЕ =====
    public static void show(String text) {
        show(text, Color.WHITE, 22);
    }

    public static void show(String text, Color color, int fontSize) {
        HUDMessage msg = new HUDMessage(text, color, fontSize);
        messages.add(msg);
        panel.repaint();

        Timer timer = new Timer(MESSAGE_LIFETIME_MS, e -> {
            messages.remove(msg);
            panel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ===== ЛЕЧЕНИЕ (+1 HP) =====
    public static void showHeal(String text) {
        show(text, Color.GREEN, 22);
    }

    // ===== ИНФО (лабиринт, выход) =====
    public static void showInfo(String text) {
        show(text, Color.WHITE, 28);
    }

    // ===== АТАКА: крупно, красно, мигает =====
    public static void showAttack(String text) {

        HUDMessage attackMsg =
                new HUDMessage(text, Color.RED, 66); // x3 размер

        Timer timer = new Timer(300, null);
        final int[] count = {0};

        timer.addActionListener(e -> {

            if (count[0] % 2 == 0) {
                messages.add(attackMsg);
            } else {
                messages.remove(attackMsg);
            }

            panel.repaint();
            count[0]++;

            if (count[0] >= 6) { // 3 мигания
                messages.remove(attackMsg);
                panel.repaint();
                timer.stop();
            }
        });

        timer.start();
    }

}
