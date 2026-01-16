package MazeGame.cards;

import javax.swing.*;
import java.awt.*;

public class SummonSelectDialog extends JDialog {

    private SummonCard selected;

    public SummonSelectDialog(Window owner, SummonDeck deck) {
        super(owner);
        setTitle("Выбор призыва");
        setModalityType(ModalityType.APPLICATION_MODAL);

        setSize(500, 300);
        setLocationRelativeTo(owner);
        setLayout(new FlowLayout());

        for (SummonCard card : deck.getCards()) {
            CardPanel panel = new CardPanel(card);
            panel.setOnClick(() -> {
                selected = card;
                dispose();
            });
            add(panel);
        }
    }

    public SummonCard getSelected() {
        return selected;
    }
}
