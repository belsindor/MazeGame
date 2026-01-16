package MazeGame.cards;

import javax.swing.*;
import java.awt.*;

public class DeckCollectionWindow extends JFrame {

    public DeckCollectionWindow(CardCollection collection) {
        setTitle("Коллекция карт");
        setSize(800, 600);

        JPanel grid = new JPanel(new GridLayout(0, 5, 10, 10));

        for (Card card : collection.getCards()) {
            grid.add(new CardPanel(card)); // без onClick
        }

        add(new JScrollPane(grid));
        setVisible(true);
    }
}
