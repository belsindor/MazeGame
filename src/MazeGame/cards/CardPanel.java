package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class CardPanel extends JPanel {

    private static final int DEFAULT_WIDTH = 120;   // ширина карточки
    private static final int DEFAULT_HEIGHT = 180;  // высота карточки

    public CardPanel(Card card) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // красивая рамка

        // Безопасная загрузка иконки с уменьшением размера
        ImageIcon icon = getScaledImageIcon(card);
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    /**
     * Получает иконку карты и уменьшает её до нужного размера
     */
    private ImageIcon getScaledImageIcon(Card card) {
        if (card == null || card.getImagePath() == null || card.getImagePath().isEmpty()) {
            return createPlaceholderIcon();
        }

        var url = getClass().getResource(card.getImagePath());
        if (url == null) {
            System.err.println("Картинка не найдена: " + card.getImagePath());
            return createPlaceholderIcon();
        }

        // Загружаем оригинальную картинку
        ImageIcon original = new ImageIcon(url);

        // Уменьшаем до нужного размера, сохраняя пропорции
        Image scaled = original.getImage().getScaledInstance(
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Image.SCALE_SMOOTH  // плавное масштабирование
        );

        return new ImageIcon(scaled);
    }

    /**
     * Плейсхолдер, если картинки нет
     */
    private ImageIcon createPlaceholderIcon() {
        JLabel placeholder = new JLabel("Нет изображения");
        placeholder.setForeground(Color.GRAY);
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 12));

        // Создаём маленькую картинку-заглушку
        BufferedImage img = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        placeholder.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        placeholder.paint(g2);
        g2.dispose();

        return new ImageIcon(img);
    }


    /**
     * Безопасно получает иконку карты
     * Возвращает null, если путь пустой или null
     */
    private ImageIcon safeGetImageIcon(Card card) {
        if (card == null || card.getImagePath() == null || card.getImagePath().isEmpty()) {
            return null;
        }

        var url = getClass().getResource(card.getImagePath());
        if (url == null) {
            System.err.println("Картинка карты не найдена: " + card.getImagePath());
            return null;
        }

        return new ImageIcon(url);
    }

    /**
     * Плейсхолдер, если иконки нет
     */
    private Icon createPlaceholder() {
        JLabel placeholder = new JLabel("Нет изображения");
        placeholder.setForeground(Color.GRAY);
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                placeholder.setSize(getWidth(), getHeight());
                placeholder.paint(g);
            }

            @Override
            public int getIconWidth() {
                return 100;
            }

            @Override
            public int getIconHeight() {
                return 150;
            }
        };
    }

    // Если нужно кликабельность — добавь метод
    public void setOnClick(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }
}