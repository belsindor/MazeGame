package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Optional;

public class SummonSelectionWindow extends JDialog {

    private SummonCard selected = null;

    /**
     * Конструктор с SummonDeck (рекомендуемый)
     */
    public SummonSelectionWindow(SummonDeck deck) {
        super((Frame) null, "Выберите призываемое существо", true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Заголовок
        JLabel title = new JLabel("Выберите призыв для боя", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Панель с карточками
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Collection<SummonCard> available = deck.getActiveSummons().values();

        if (available.isEmpty()) {
            JLabel empty = new JLabel("У вас нет доступных призывов");
            empty.setFont(new Font("Arial", Font.PLAIN, 20));
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            cardsPanel.add(empty);
        } else {
            // Если только один суммон — сразу выбираем его автоматически
            if (available.size() == 1) {
                selected = available.iterator().next();
            } else {
                // Несколько — показываем выбор
                for (SummonCard card : available) {
                    CardPanel panel = new CardPanel(card);

                    // Клик по карточке → выбор + закрытие
                    panel.setOnClick(() -> {
                        selected = card;
                        dispose();
                    });

                    // Красивая подсветка при наведении
                    panel.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {
                            panel.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4));
                        }

                        @Override
                        public void mouseExited(java.awt.event.MouseEvent e) {
                            panel.setBorder(null);
                        }
                    });

                    // Выделяем уже выбранный суммон
                    if (card == deck.getSelectedSummon()) {
                        panel.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 0), 5));
                    }

                    cardsPanel.add(panel);
                }
            }
        }

        // Скролл, если карточек много
        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Кнопка "Без призыва" (отмена)
        JButton noSummonBtn = new JButton("Без призыва");
        noSummonBtn.setFont(new Font("Arial", Font.BOLD, 16));
        noSummonBtn.setPreferredSize(new Dimension(180, 50));
        noSummonBtn.addActionListener(e -> {
            selected = null;
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottom.add(noSummonBtn);
        add(bottom, BorderLayout.SOUTH);

        // При закрытии окна крестиком — считаем как отмену
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                selected = null;
                dispose();
            }
        });
    }

    /**
     * Показать окно и дождаться выбора (блокирующий метод)
     */
    public Optional<SummonCard> showAndWait() {
        setVisible(true);
        return Optional.ofNullable(selected);
    }

    /**
     * Устаревший/альтернативный метод (для совместимости)
     */
    public SummonCard getSelectedCard() {
        setVisible(true);
        return selected;
    }
}