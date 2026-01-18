package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DeckCollectionWindow extends JFrame {

    public DeckCollectionWindow(CardCollection collection) {
        super("Коллекция карт");
        setSize(1000, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Заголовок
        JLabel title = new JLabel("Коллекция карт", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Панель с карточками
        JPanel grid = new JPanel(new GridLayout(0, 6, 10, 10)); // 6 колонок
        grid.setOpaque(false);

        if (collection == null || collection.getAllCards().isEmpty()) {
            JLabel emptyLabel = new JLabel("У вас пока нет карт");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 24));
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            grid.add(emptyLabel);
        } else {
            for (Map.Entry<Card, Integer> entry : collection.getAllCards().entrySet()) {
                Card card = entry.getKey();
                int count = entry.getValue();

                // Защита от null-карты
                if (card == null) continue;

                // Создаём карточку с безопасной загрузкой изображения
                CardPanel panel = new CardPanel(card);  // теперь CardPanel сам обрабатывает null

                // Счётчик количества
                if (count > 1) {
                    JLabel countLabel = new JLabel("×" + count, SwingConstants.CENTER);
                    countLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    countLabel.setForeground(Color.WHITE);
                    countLabel.setOpaque(true);
                    countLabel.setBackground(new Color(0, 0, 0, 180));
                    panel.setLayout(new BorderLayout());
                    panel.add(countLabel, BorderLayout.SOUTH);
                }

                grid.add(panel);
            }
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}