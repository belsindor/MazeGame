package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DeckCollectionWindow extends JFrame {

    public DeckCollectionWindow(CardCollection collection) {
        super("Коллекция карт");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel grid = new JPanel(new GridLayout(0, 5, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Проходим по всем картам и их количеству
        for (Map.Entry<Card, Integer> entry : collection.getAllCards().entrySet()) {
            Card card = entry.getKey();
            int count = entry.getValue();

            // Можно сделать красивую карточку с количеством
            CardPanel panel = new CardPanel(card);

            // Добавляем счётчик поверх карточки (пример)
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

        JScrollPane scroll = new JScrollPane(grid);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll);

        setVisible(true);
    }
}