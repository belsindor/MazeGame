package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//+
public class CardPanel extends JPanel {

    public CardPanel(Card card) {
        setLayout(new BorderLayout());
        add(new JLabel(card.getImageIcon()), BorderLayout.CENTER);
        setOpaque(false);
    }
    public void setOnClick(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }
}



