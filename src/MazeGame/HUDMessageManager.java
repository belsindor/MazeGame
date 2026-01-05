package MazeGame;

import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class HUDMessageManager {

    private static final List<Message> messages = new ArrayList<>();
    private static GamePanel panel;

    private static final int MESSAGE_LIFETIME_MS = 500;

    public static void init(GamePanel gamePanel) {
        panel = gamePanel;
    }

    public static void show(String text) {
        if (panel == null) return;

        Message msg = new Message(text, System.currentTimeMillis());
        messages.add(msg);

        panel.repaint();

        // таймер на удаление
        Timer timer = new Timer(MESSAGE_LIFETIME_MS, e -> {
            messages.remove(msg);
            panel.repaint();
        });
        timer.setRepeats(false);
        timer.start();

    }

    public static List<String> getActiveMessages() {
        List<String> result = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (Message m : messages) {
            if (now - m.time < MESSAGE_LIFETIME_MS) {
                result.add(m.text);
            }
        }
        return result;
    }

    private static class Message {
        String text;
        long time;

        Message(String text, long time) {
            this.text = text;
            this.time = time;
        }
    }
}
