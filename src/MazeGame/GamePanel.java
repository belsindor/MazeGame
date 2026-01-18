package MazeGame;

import MazeGame.cards.CardCollection;
import MazeGame.cards.DeckCollectionWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GamePanel extends JPanel {

    private Image backgroundImage;
    private final Player player;
    private Monster pendingMonster = null;
    private final VisualMazeGame game;

    private Image inventoryIcon;
    private Image deckIcon;
    private final Rectangle inventoryIconBounds = new Rectangle();
    private final Rectangle deckIconBounds = new Rectangle();

    public GamePanel(Player player, VisualMazeGame game) {
        this.player = player;
        this.game = game;
        HUDMessageManager.init(this);

        // Загрузка иконок
        inventoryIcon = loadIcon("/images/inventory.png");
        deckIcon = loadIcon("/images/deckCollection.png");

        setFocusable(true);
        requestFocusInWindow();

        // Единый обработчик кликов по всей панели
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GameWindow.isBattleActive()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                Point p = e.getPoint();

                // Клик по монстру
                if (pendingMonster != null) {
                    Rectangle monsterBounds = getMonsterClickBounds();
                    if (monsterBounds != null && monsterBounds.contains(p)) {
                        System.out.println("Клик по монстру → запускаем бой!");
                        // Передаём ВСЕГДА текущее окно как owner (this — JFrame)
                        game.startBattle((JFrame) SwingUtilities.getWindowAncestor(GamePanel.this), pendingMonster);
                        clearPendingMonster();
                        return;
                    }
                }

                // Клик по иконкам
                int iconSize = 64;
                int margin = 24;
                int spacing = 16;

                int baseX = getWidth() - margin - iconSize;
                int baseY = getHeight() - margin - iconSize;

                Rectangle deckBounds = new Rectangle(baseX - iconSize - spacing, baseY, iconSize, iconSize);
                if (deckBounds.contains(p)) {
                    CardCollection collection = player.getCardCollection();
                    if (collection != null && !collection.getAllCards().isEmpty()) {
                        new DeckCollectionWindow(collection);
                    } else {
                        HUDMessageManager.show("Коллекция карт пуста", new Color(255, 180, 100), 22);
                    }
                    return;
                }

                Rectangle invBounds = new Rectangle(baseX, baseY, iconSize, iconSize);
                if (invBounds.contains(p)) {
                    new InventoryWindow(player);
                }
            }
        });
    }

    /**
     * Прямоугольник области монстра для клика
     * (увеличен запас для удобства)
     */
    private Rectangle getMonsterClickBounds() {
        if (pendingMonster == null) return null;

        int cardWidth = 320;
        int cardHeight = 480;
        int x = (getWidth() - cardWidth) / 2;
        int y = (getHeight() - cardHeight) / 2 - 40;

        // Запас +80 пикселей со всех сторон — кликать проще
        return new Rectangle(x - 80, y - 80, cardWidth + 160, cardHeight + 160);
    }

    private Image loadIcon(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("❌ Не найдена иконка: " + path);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    public void showPendingMonster(Monster m) {
        this.pendingMonster = m;
        repaint();
    }

    public void clearPendingMonster() {
        this.pendingMonster = null;
        repaint();
    }

    public void setImage(String imageName) {
        var url = getClass().getResource("/images/" + imageName);
        if (url == null) {
            System.err.println("❌ Картинка не найдена: " + imageName);
            backgroundImage = null;
        } else {
            backgroundImage = new ImageIcon(url).getImage();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        drawLeftHUD(g);
        drawRightHUD(g);
        drawCenterMessages(g);

        if (pendingMonster != null) {
            drawPendingMonster(g, pendingMonster);
        }

        drawBottomIcons(g);
    }

    private void drawPendingMonster(Graphics g, Monster monster) {
        int cardWidth = 320;
        int cardHeight = 480;
        int x = (getWidth() - cardWidth) / 2;
        int y = (getHeight() - cardHeight) / 2 - 40;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        String imgPath = monster.getImagePath();
        Image monsterImg = loadImage(imgPath);

        if (monsterImg != null) {
            double originalRatio = 832.0 / 1248.0;
            int imgHeight = 280;
            int imgWidth = (int)(imgHeight * originalRatio);

            int imgX = x + (cardWidth - imgWidth) / 2;
            int imgY = y + 70;

            g.drawImage(monsterImg, imgX, imgY, imgWidth, imgHeight, this);

            g.setColor(new Color(150, 150, 150, 100));
            g.drawRect(imgX - 2, imgY - 2, imgWidth + 4, imgHeight + 4);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        String name = monster.getName();
        int nameX = x + (cardWidth - fm.stringWidth(name)) / 2;
        g.drawString(name, nameX, y + 40);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lv. " + monster.getLevel(), x + 30, y + 80);
    }

    private Image loadImage(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            HUDMessageManager.show("НЕ НАЙДЕНА: " + path, new Color(255, 80, 80), 20);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    private void drawBottomIcons(Graphics g) {
        int iconSize = 64;
        int margin = 24;
        int spacing = 16;

        int baseX = getWidth() - margin - iconSize;
        int baseY = getHeight() - margin - iconSize;

        int deckX = baseX - iconSize - spacing;
        deckIconBounds.setBounds(deckX, baseY, iconSize, iconSize);

        if (deckIcon != null) {
            g.drawImage(deckIcon, deckX, baseY, iconSize, iconSize, this);
        }

        inventoryIconBounds.setBounds(baseX, baseY, iconSize, iconSize);

        if (inventoryIcon != null) {
            g.drawImage(inventoryIcon, baseX, baseY, iconSize, iconSize, this);
        }

        g.setColor(new Color(255, 255, 255, 100));
        g.drawRoundRect(deckX - 3, baseY - 3, iconSize + 6, iconSize + 6, 16, 16);
        g.drawRoundRect(baseX - 3, baseY - 3, iconSize + 6, iconSize + 6, 16, 16);
    }

    private void drawLeftHUD(Graphics g) {
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRoundRect(20, 20, 260, 110, 15, 15);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));

        g.drawString(player.getName(), 35, 45);
        g.drawString("Уровень: " + player.getLevel(), 35, 65);
        g.drawString("⚔ ATK " + player.getTotalAttack() + "   🛡 DEF " + player.getTotalDefense(), 35, 85);

        int barX = 35;
        int barY = 95;
        int barWidth = 220;
        int barHeight = 10;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);

        double expPercent = player.getExperience() / (double) player.getExperienceToNextLevel();
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int) (barWidth * expPercent), barHeight);
    }

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
        g.fillRect(barX, barY, (int) ((hp / (double) maxHp) * barWidth), barHeight);

        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);
        g.drawString("HP " + hp + " / " + maxHp, barX + 40, barY - 5);
    }

    private void drawCenterMessages(Graphics g) {
        List<HUDMessage> messages = HUDMessageManager.getActiveMessages();
        if (messages.isEmpty()) return;

        int centerX = getWidth() / 2;
        int startY = getHeight() / 2 - messages.size() * 20;

        for (HUDMessage msg : messages) {
            g.setFont(new Font("Arial", Font.BOLD, msg.fontSize()));
            FontMetrics fm = g.getFontMetrics();

            int textWidth = fm.stringWidth(msg.text());
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

            g.setColor(msg.color());
            g.drawString(msg.text(), centerX - textWidth / 2, startY);

            startY += textHeight + 15;
        }
    }
}