package MazeGame;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {

    private Image image;
    private final Player player;

    public GamePanel(Player player) {
        this.player = player;
        HUDMessageManager.init(this);

    }

    public void setImage(String imageName) {
        var url = getClass().getResource("/images/" + imageName);

        if (url == null) {
            System.err.println("❌ Картинка не найдена: " + imageName);
            image = null;
        } else {
            image = new ImageIcon(url).getImage();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ===== ФОН =====
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        drawLeftHUD(g);
        drawRightHUD(g);
        drawCenterMessages(g);
    }

    // ===== ЛЕВАЯ ПАНЕЛЬ =====
    private void drawLeftHUD(Graphics g) {
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(20, 20, 260, 110, 15, 15);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString(player.getName(), 35, 45);
        g.drawString("Уровень: " + player.getLevel(), 35, 65);
        g.drawString("⚔ ATK " + player.getTotalAttack() + "   🛡 DEF " + player.getTotalDefense(), 35, 85);

        // EXP BAR
        int barX = 35;
        int barY = 95;
        int barWidth = 220;
        int barHeight = 10;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        double expPercent =
                player.getExperience() / (double) player.getExperienceToNextLevel();

        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int) (barWidth * expPercent), barHeight);
    }

    // ===== ПРАВАЯ ПАНЕЛЬ (HP) =====
    private void drawRightHUD(Graphics g) {
        int x = getWidth() - 240;
        int y = 20;

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(x, y, 200, 60, 15, 15);

        int maxHp = player.getMaxHealth();
        int hp = player.getHealth();

        int barX = x + 20;
        int barY = y + 30;
        int barWidth = 160;
        int barHeight = 15;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        g.setColor(Color.RED);
        g.fillRect(barX, barY,
                (int) ((hp / (double) maxHp) * barWidth),
                barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);
        g.drawString("HP " + hp + " / " + maxHp, barX + 40, barY - 5);
    }

    // ===== ЦЕНТРАЛЬНЫЕ СООБЩЕНИЯ =====
    private void drawCenterMessages(Graphics g) {
        List<HUDMessage> messages = HUDMessageManager.getActiveMessages();
        if (messages.isEmpty()) return;

        int centerX = getWidth() / 2;
        int startY = getHeight() / 2 - messages.size() * 20;

        for (HUDMessage msg : messages) {

            g.setFont(new Font("Arial", Font.BOLD, msg.fontSize));
            FontMetrics fm = g.getFontMetrics();

            int textWidth = fm.stringWidth(msg.text);
            int textHeight = fm.getHeight();

            g.setColor(new Color(0, 0, 0, 180));
            g.fillRoundRect(
                    centerX - textWidth / 2 - 20,
                    startY - textHeight + 10,
                    textWidth + 40,
                    textHeight + 10,
                    15,
                    15
            );

            g.setColor(msg.color);
            g.drawString(msg.text, centerX - textWidth / 2, startY);

            startY += textHeight + 15;
        }
    }


}
