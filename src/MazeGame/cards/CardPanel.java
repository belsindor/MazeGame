package MazeGame.cards;

import MazeGame.MonsterTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class CardPanel extends JPanel {

    private static final int DEFAULT_WIDTH = 120;   // ширина карточки
    private static final int DEFAULT_HEIGHT = 180;  // высота карточки

    private static final Map<String, ImageIcon> IMAGE_CACHE = new HashMap<>();


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

        // Добавляем tooltip при наведении
        setToolTipText(buildTooltip(card));
    }

    /**
     * Создаёт красивый HTML-tooltip для карточки
     */
    private String buildTooltip(Card card) {
        if (card == null) {
            return "<html><b>Нет карты</b></html>";
        }

        StringBuilder sb = new StringBuilder("<html>");
        sb.append("<b>").append(card.getName() != null ? card.getName() : "Без названия").append("</b><br>");

        // Тип и редкость
        sb.append("<font color='gray'>")
                .append(card.getType() != null ? card.getType().name() : "?")
                .append(" • ")
                .append(card.getRarity() != null ? card.getRarity().name() : "?")
                .append("</font><br>");

        // Дополнительная информация (зависит от типа карты)
        if (card instanceof SummonCard summon) {
            MonsterTemplate tmpl = summon.getMonsterTemplate();
            sb.append("Уровень: ").append(tmpl.level()).append("<br>");
            sb.append("Здоровье: ").append(tmpl.maxHealth()).append("<br>");
            sb.append("Атака: ").append(tmpl.attack()).append(" • Защита: ").append(tmpl.defense()).append("<br>");
            sb.append("Тип: ").append(tmpl.unitType().name()).append("<br>");
        } else {
            // Для обычных карт — можно добавить свои поля, если есть
            sb.append("Эффект: ").append(card.getEffect() != null ? card.getEffect().name() : "—").append("<br>");
        }

        sb.append("</html>");
        return sb.toString();
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

        ImageIcon original = new ImageIcon(url);
        Image scaled = original.getImage().getScaledInstance(
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Image.SCALE_SMOOTH
        );
        System.out.println("Загрузка изображения для карты: " + (card != null ? card.getName() + " (" + card.getImagePath() + ")" : "null"));
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

        BufferedImage img = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        placeholder.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        placeholder.paint(g2);
        g2.dispose();

        return new ImageIcon(img);
    }

    // Кликабельность (если нужно)
    public void setOnClick(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }
}