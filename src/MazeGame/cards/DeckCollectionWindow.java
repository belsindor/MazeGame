package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Map;

public class DeckCollectionWindow extends JFrame {

    // Константы размеров карточек (те же, что в CardPanel)
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 180;

    public DeckCollectionWindow(CardCollection collection) {
        super("Коллекция карт");
        setSize(1000, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Тёмный фон
        getContentPane().setBackground(new Color(30, 30, 40));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 40));

        // Заголовок
        JLabel title = new JLabel("Коллекция карт", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        title.setForeground(new Color(220, 220, 255));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Панель с карточками
        JPanel grid = new JPanel(new GridLayout(0, 6, 12, 12));
        grid.setOpaque(false);

        if (collection == null || collection.getAllCards().isEmpty()) {
            JLabel emptyLabel = new JLabel("У вас пока нет карт", SwingConstants.CENTER);
            emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
            emptyLabel.setForeground(new Color(120, 120, 140));
            grid.add(emptyLabel);
        } else {
            // Сортировка по id
            collection.getAllCards().entrySet().stream()
                    .sorted(Comparator.comparingInt(e -> e.getKey().getId()))
                    .forEach(entry -> {
                        Card card = entry.getKey();
                        int count = entry.getValue();

                        if (card == null) return;

                        CardPanel panel = new CardPanel(card);

                        // Если count > 1 — накладываем счётчик поверх картинки
                        if (count > 1) {
                            JLabel countLabel = new JLabel("×" + count);
                            countLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                            countLabel.setForeground(Color.WHITE);
                            countLabel.setOpaque(true);
                            countLabel.setBackground(new Color(0, 0, 0, 200));
                            countLabel.setHorizontalAlignment(SwingConstants.CENTER);
                            countLabel.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(new Color(150, 150, 255, 180), 1),
                                    BorderFactory.createEmptyBorder(2, 8, 2, 8)
                            ));
                            countLabel.setPreferredSize(new Dimension(60, 30));

                            // JLayeredPane для наложения
                            JLayeredPane layered = new JLayeredPane();
                            layered.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
                            layered.setLayout(null);

                            // Берём компонент с картинкой (обычно это JLabel в CENTER)
                            Component pictureComponent = panel.getComponent(0); // первый (и единственный) компонент
                            pictureComponent.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

                            // Счётчик в правом нижнем углу
                            countLabel.setBounds(DEFAULT_WIDTH - 70, DEFAULT_HEIGHT - 40, 60, 30);

                            layered.add(pictureComponent, Integer.valueOf(0));
                            layered.add(countLabel, Integer.valueOf(1));

                            // Заменяем содержимое panel
                            panel.removeAll();
                            panel.setLayout(new BorderLayout());
                            panel.add(layered, BorderLayout.CENTER);
                        }

                        grid.add(panel);
                    });
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.getViewport().setBackground(new Color(30, 30, 40));

        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);
        setUndecorated(true);

        setVisible(true);
    }
}