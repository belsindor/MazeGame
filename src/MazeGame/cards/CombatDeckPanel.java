package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CombatDeckPanel extends JPanel {

    public CombatDeckPanel(CombatDeck deck, Consumer<Card> onSelect) {
        setLayout(new FlowLayout());

        for (Card card : deck.getCards()) {
            CardPanel panel = new CardPanel(card);
            panel.setOnClick(() -> onSelect.accept(card));
            add(panel);
        }
    }
}

