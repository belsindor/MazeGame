package MazeGame.cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Optional;

public class SummonSelectionWindow extends JDialog {

    private SummonCard selected;

    public SummonSelectionWindow(Collection<SummonCard> summons) {
        // UI список
    }

    public Optional<SummonCard> showAndWait() {
        // если 1 карта — сразу выбрать
        // иначе ждать клика
        return Optional.ofNullable(selected);
    }

    public SummonSelectionWindow(SummonDeck deck) {
        super((Frame) null, "Выберите суммон", true);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Выберите призываемое существо", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (SummonCard card : deck.getCards()) {
            CardPanel panel = new CardPanel(card);
            panel.setOnClick(() -> {
                selected = card;
                dispose();
            });
            grid.add(panel);
        }

        add(new JScrollPane(grid), BorderLayout.CENTER);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                selected = null; // отмена выбора
                dispose();
            }
        });
    }

    public SummonCard getSelectedCard() {
        setVisible(true);
        return selected;
    }
}
