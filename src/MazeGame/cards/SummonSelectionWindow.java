package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Optional;

public class SummonSelectionWindow extends JDialog {

    private SummonCard selected = null;

    public SummonSelectionWindow(SummonDeck deck) {
        super((Frame) null, "Выберите призываемое существо", true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        System.out.println("SummonSelectionWindow открыт");

        // Заголовок
        JLabel title = new JLabel("Выберите призыв для боя", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Панель с карточками
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Collection<SummonCard> available = deck.getActiveSummons().values();
        System.out.println("Доступно активных суммонов: " + available.size());

        if (available.isEmpty()) {
            JLabel empty = new JLabel("У вас нет доступных призывов");
            empty.setFont(new Font("Arial", Font.PLAIN, 20));
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            cardsPanel.add(empty);
        } else {
            System.out.println("Суммоны для отображения:");
            for (SummonCard card : available) {
                System.out.println(" - " +" (ID: " + card.getId() + ", Rarity: " + card.getRarity() + ")");

                CardPanel panel = new CardPanel(card);

                // Клик по карточке → выбор + закрытие
                panel.setOnClick(() -> {
                    selected = card;
                    System.out.println("Выбран суммон: " );
                    dispose();
                });

                // Подсветка при наведении
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

                // Выделяем уже выбранный
                if (card == deck.getSelectedSummon()) {
                    panel.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 0), 5));
                }

                cardsPanel.add(panel);
            }
        }

        // Скролл (на всякий случай)
        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Кнопка "Без призыва"
        JButton noSummonBtn = new JButton("Без призыва");
        noSummonBtn.setFont(new Font("Arial", Font.BOLD, 16));
        noSummonBtn.setPreferredSize(new Dimension(180, 50));
        noSummonBtn.addActionListener(e -> {
            selected = null;
            System.out.println("Выбор отменён (без призыва)");
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottom.add(noSummonBtn);
        add(bottom, BorderLayout.SOUTH);

        // Закрытие крестиком = отмена
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                selected = null;
                System.out.println("Окно закрыто крестиком → отмена");
                dispose();
            }
        });
    }

    public Optional<SummonCard> showAndWait() {
        setUndecorated(true);
        setVisible(true);
        return Optional.ofNullable(selected);
    }

    public SummonCard getSelectedCard() {
        setVisible(true);
        return selected;
    }

}