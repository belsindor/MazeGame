package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.function.Consumer;

public class CombatDeckPanel extends JPanel {

    public CombatDeckPanel(CombatDeck deck, Consumer<Card> onSelect) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setOpaque(false);

        // Получаем активные карты из CombatDeck
        Map<TypeEffect, Card> activeCards = deck.getActiveCards();

        if (activeCards.isEmpty()) {
            JLabel emptyLabel = new JLabel("Боевая колода пуста");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            emptyLabel.setForeground(Color.GRAY);
            add(emptyLabel);
        } else {
            for (Card card : activeCards.values()) {
                CardPanel panel = new CardPanel(card);

                // Клик по карточке вызывает переданный обработчик
                panel.setOnClick(() -> {
                    if (onSelect != null) {
                        onSelect.accept(card);
                    }
                });

                // Подсветка при наведении (для красоты)
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

                add(panel);
            }
        }
    }

    // Перегруженный конструктор без обработчика клика (просто просмотр)
    public CombatDeckPanel(CombatDeck deck) {
        this(deck, null);
    }
}