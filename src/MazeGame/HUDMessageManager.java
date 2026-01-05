package MazeGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUDMessageManager {

    private static final List<HUDMessage> messages = new ArrayList<>();
    private static JPanel panel;

    public static void init(JPanel gamePanel) {
        panel = gamePanel;
    }

    public static List<HUDMessage> getActiveMessages() {
        return messages;
    }

    // ===== Обычное сообщение (например +1 HP) =====
    public static void showHeal(String text) {
        HUDMessage msg = new HUDMessage(text, Color.GREEN, 22);
        showOnce(msg, 2000);
    }

    // ===== АТАКА: крупное, красное, мигает =====
    public static void showAttack(String text) {

        Timer timer = new Timer(500, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            if (count[0] % 2 == 0) {
                messages.add(new HUDMessage(text, Color.RED, 33)); // x3 размер
            } else {
                messages.clear();
            }

            panel.repaint();
            count[0]++;

            if (count[0] >= 6) { // 3 появления
                messages.clear();
                panel.repaint();
                timer.stop();
            }
        });

        timer.start();
    }

    private static void showOnce(HUDMessage msg, int lifetimeMs) {
        messages.add(msg);
        panel.repaint();

        new Timer(lifetimeMs, e -> {
            messages.remove(msg);
            panel.repaint();
        }) {{
            setRepeats(false);
            start();
        }};
    }
    // ===== ИНФО-СООБЩЕНИЯ (лабиринт, выход и т.п.) =====
    public static void showInfo(String text) {
        HUDMessage msg = new HUDMessage(text, Color.WHITE, 28);
        showOnce(msg, 2000);
    }

}
