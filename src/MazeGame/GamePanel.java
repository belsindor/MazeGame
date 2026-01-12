package MazeGame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GamePanel extends JPanel {

    private Image image;
    private final Player player;
    private Monster pendingMonster = null;
    private Image monsterImage;
    private VisualMazeGame game;
    private Image inventoryIcon;
    private final Rectangle inventoryIconBounds;

    public GamePanel(Player player, VisualMazeGame game) {
        this.player = player;
        this.game = game;
        HUDMessageManager.init(this);

        // Загружаем иконку инвентаря один раз
        var iconUrl = getClass().getResource("/images/inventory.png");
        if (iconUrl != null) {
            inventoryIcon = new ImageIcon(iconUrl).getImage();
        } else {
            System.err.println("❌ Не найдена иконка инвентаря: /images/inventory.png");
            inventoryIcon = null;
        }

        // Размер и положение иконки (можно потом вынести в константы)
        int iconSize = 64;
        inventoryIconBounds = new Rectangle(0, 0, iconSize, iconSize);

        // Обработчик клика мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Проверяем, попал ли клик в область иконки
                if (inventoryIconBounds.contains(e.getPoint())) {
                    if (GameWindow.isBattleActive()) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    new InventoryWindow(player);
                }
            }
        });

        // Чтобы клик работал, включаем получение фокуса и событий мыши
        setFocusable(true);
        requestFocusInWindow();

        // Добавляем MouseListener для клика по монстру
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pendingMonster == null) return;

                // Рассчитываем текущую область монстра (динамически, на случай ресайза)
                int size = Math.min(getWidth(), getHeight()) / 4;  // 1/4 от мин. стороны
                int x = getWidth() / 2 - size / 2;
                int y = getHeight() / 2 - size / 2;
                Rectangle monsterRect = new Rectangle(x, y, size, size);

                if (monsterRect.contains(e.getPoint())) {
                    game.startBattle(pendingMonster);  // Запускаем бой
                    clearPendingMonster();             // Очищаем после клика
                }
            }
        });
    }


    // Показать монстра
    public void showPendingMonster(Monster m) {
        this.pendingMonster = m;
        repaint();
    }

    // Очистить монстра
    public void clearPendingMonster() {
        this.pendingMonster = null;
        repaint();
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

        if (pendingMonster != null) {
            drawPendingMonster(g, pendingMonster);
        }

        drawInventoryIcon(g);
    }

    private void drawPendingMonster(Graphics g, Monster monster) {
        // Размеры и положение — подберите под ваш дизайн
        int cardWidth = 320;
        int cardHeight = 480;
        int x = (getWidth() - cardWidth) / 2;
        int y = (getHeight() - cardHeight) / 2 - 40;  // немного выше центра

        // Фон карты (можно сделать полупрозрачным или с рамкой)
        g.setColor(new Color(30, 30, 50, 220));
        g.fillRoundRect(x, y, cardWidth, cardHeight, 24, 24);

        g.setColor(new Color(180, 40, 40));
        g.drawRoundRect(x, y, cardWidth, cardHeight, 24, 24);
        g.drawRoundRect(x+1, y+1, cardWidth-2, cardHeight-2, 22, 22);

        // Изображение монстра
        String imgPath = monster.getImagePath();
        Image monsterImg = loadImage(imgPath);

        if (monsterImg != null) {
            int imgSize = 240;
            int imgX = x + (cardWidth - imgSize) / 2;
            int imgY = y + 60;
            g.drawImage(monsterImg, imgX, imgY, imgSize, imgSize, this);
        }

        // Имя
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();
        String name = monster.getName();
        int nameX = x + (cardWidth - fm.stringWidth(name)) / 2;
        g.drawString(name, nameX, y + 40);

        // Уровень
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lv. " + monster.getLevel(), x + 30, y + 80);

        // HP
        g.setColor(Color.RED);
        g.fillRect(x + 30, y + cardHeight - 80, 260, 30);
        g.setColor(Color.WHITE);
        g.drawString("HP: " + monster.getHealth() + " / " + monster.getMaxHealth(),
                x + 40, y + cardHeight - 58);
    }

    private Image loadImage(String path) {
        var url = getClass().getResource(path);

        if (url == null) {
            // Специальное долгое сообщение об ошибке
            HUDMessageManager.show(
                    "НЕ НАЙДЕНА: " + path,
                    new Color(255, 80, 80),
                    20
            );

            // Делаем это сообщение "липким" на 8 секунд
            HUDMessage msg = new HUDMessage("НЕ НАЙДЕНА: " + path, new Color(255, 80, 80), 20);
            HUDMessageManager.messages.add(msg);
            HUDMessageManager.panel.repaint();

            new Timer(8000, e -> {  // ← 8 секунд
                HUDMessageManager.messages.remove(msg);
                HUDMessageManager.panel.repaint();
            }).start();

            return null;
        }

        return new ImageIcon(url).getImage();
    }

    private void drawInventoryIcon(Graphics g) {
        if (inventoryIcon == null) return;

        int iconSize = 64;
        int margin = 20;

        // Обновляем координаты (делаем это здесь, чтобы адаптировалось при изменении размера окна)
        int x = getWidth() - iconSize - margin;
        int y = getHeight() - iconSize - margin;

        inventoryIconBounds.setBounds(x, y, iconSize, iconSize);

        // Сама иконка
        g.drawImage(inventoryIcon, x, y, iconSize, iconSize, this);

        // Опционально: лёгкая обводка/тень при наведении (можно реализовать позже через MouseMotionListener)
        g.setColor(new Color(255, 255, 255, 80));
        g.drawRoundRect(x - 2, y - 2, iconSize + 4, iconSize + 4, 12, 12);
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
