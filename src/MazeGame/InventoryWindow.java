package MazeGame;

import javax.swing.*;
//+
public class InventoryWindow extends JFrame {

    public InventoryWindow(Player player) {
        setTitle("Инвентарь");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new InventoryPanel(player));

        setVisible(true);
    }
}
