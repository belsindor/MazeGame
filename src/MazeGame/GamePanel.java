package MazeGame;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private Image image;
    private final Player player;

    public GamePanel(Player player) {
        this.player = player;
        setDoubleBuffered(true); // 👍 убирает мерцание
    }

    public void setImage(String imageName) {
        var url = getClass().getResource("/images/" + imageName);

        if (url == null) {
            System.err.println("❌ Картинка не найдена: " + imageName);
            image = null;
            repaint();
            return;
        }

        image = new ImageIcon(url).getImage();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ===== КАРТИНКА ЛОКАЦИИ =====
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        // ===== HP BAR =====
        int maxHp = player.getMaxHealth();
        int hp = player.getHealth();

        int barWidth = 200;
        int barHeight = 20;
        int x = 1400;
        int y = 20;

        g.setColor(new Color(0, 0, 0, 150)); // полупрозрачный фон
        g.fillRoundRect(x - 5, y - 5, barWidth + 10, barHeight + 10, 10, 10);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        int hpWidth = (int) ((hp / (double) maxHp) * barWidth);
        g.setColor(Color.RED);
        g.fillRect(x, y, hpWidth, barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
        g.drawString(hp + " / " + maxHp, x + 65, y + 15);
    }
}
