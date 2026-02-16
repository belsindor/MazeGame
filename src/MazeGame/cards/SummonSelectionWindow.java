package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public class SummonSelectionWindow extends JDialog {

    private SummonCard selected = null;

    public SummonSelectionWindow(SummonDeck deck) {
        super((Frame) null, "Выбор призыва", true);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setUndecorated(true);

        // ---------- Заголовок ----------
        JLabel title = new JLabel("Выберите призыв для боя", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ---------- Карточки ----------
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Collection<SummonCard> summons = deck.getActiveSummons().values();

        if (summons.isEmpty()) {
            JLabel empty = new JLabel("У вас нет доступных призывов");
            empty.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
            cardsPanel.add(empty);
        } else {
            for (SummonCard card : summons) {
                CardPanel panel = new CardPanel(card, false, false); // ❗ БЕЗ drag

                panel.setOnClick(() -> {
                    selected = card;
                    dispose();
                });

                panel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        panel.setBorder(BorderFactory.createLineBorder(
                                new Color(255, 215, 0), 4));
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                    }
                });

                if (card == deck.getSelectedSummon()) {
                    panel.setBorder(BorderFactory.createLineBorder(
                            new Color(0, 180, 0), 5));
                }

                cardsPanel.add(panel);
            }
        }

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // ---------- Кнопка "Без призыва" ----------
        JButton noSummon = new JButton("Без призыва");
        noSummon.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        noSummon.setPreferredSize(new Dimension(200, 50));
        noSummon.addActionListener(e -> {
            selected = null;
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        bottom.add(noSummon);
        add(bottom, BorderLayout.SOUTH);
    }

    public Optional<SummonCard> showAndWait() {
        setVisible(true);
        return Optional.ofNullable(selected);
    }
}
